package com.konsung.monitor.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.konsung.monitor.R;
import com.konsung.monitor.netty.EchoServer;
import com.konsung.monitor.util.BroadcastConstant;
import com.konsung.monitor.util.GlobalConstant;
import com.konsung.monitor.util.KParamType;
import com.konsung.monitor.util.SpUtils;
import com.konsung.monitor.util.UiUtils;

/**
 * Created by liangkun on 2017/10/12 0012.
 * 采用binder服务对appdevice的数据处理
 */

public class KonsungBinderService extends Service {
    // 将趋势数据存储进List,用于数据缓存
    // 保存进List集合的原因是连续数据需要过滤
    // 如果是点测数据值则不需要加入集合进行过滤,而是直接使用即可
    private SendMsgToPresent sendMsg;
    /**
     * 心电波形12导的数组
     */
    private byte[] spo2Wave = null;
    private byte[] irtempWave = null;
    private byte[] ecgIIWave = null;
    private byte[] ecgIWave = null;
    private byte[] ecgIIIWave = null;
    private byte[] ecgAVRWave = null;
    private byte[] ecgAVLWave = null;
    private byte[] ecgAVFWave = null;
    private byte[] ecgV1Wave = null;
    private byte[] ecgV2Wave = null;
    private byte[] ecgV3Wave = null;
    private byte[] ecgV4Wave = null;
    private byte[] ecgV5Wave = null;
    private byte[] ecgV6Wave = null;
    private MsgBinder msgBinder; //消息绑定对象

    //bindservice代理对象
    public class MsgBinder extends Binder {
        public void setSendMsgToPresent(SendMsgToPresent obj) {
            sendMsg = obj;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        msgBinder = new MsgBinder();
        // 开启线程处理网络数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EchoServer.getEchoServerInstance(GlobalConstant.PORT, mHandler).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return msgBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 发送信息数据的接口
     */
    public interface SendMsgToPresent {
        void sendWave(int param, byte[] bytes);

        void sendTrend(int param, int value);

        void sendConfig(int param, int value);
    }

    /**
     * Handler 处理数据
     * 使用HandlerThread的looper对象创建Handler，如果使用默认的构造方法，很有可能阻塞UI线程
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case GlobalConstant.PARA_STATUS:

                        Bundle paraStatusBundle = msg.getData();
                        byte[] paraBoardName = paraStatusBundle.getByteArray("paraBoardName");
                        byte[] paraBoardVersion = paraStatusBundle
                                .getByteArray("paraBoardVersion");
                        String boardName = new String(paraBoardName, "UTF-8");
                        String boardVersion = new String(paraBoardVersion,
                                "UTF-8");

                        // 把KSM5的版本号作为多参模块版本号
                        if (boardName.equals(UiUtils.getString(R.string.ksm5))) {
                            SpUtils.saveToSp(getApplicationContext(), GlobalConstant.APP_CONFIG,
                                    GlobalConstant.PARA_BOARDNAME, boardName);
                            SpUtils.saveToSp(getApplicationContext(),
                                    GlobalConstant.APP_CONFIG, GlobalConstant.PARABOARD_VERSION,
                                    boardVersion);
                        }

                        break;
                    case GlobalConstant.NET_TREND:
                        //趋势数据包
                        if (sendMsg != null) {
                            sendMsg.sendTrend(msg.arg1, msg.arg2);
                        }

                        break;
                    //波形数据
                    case GlobalConstant.NET_WAVE:
                        Bundle data = msg.getData();
                        if (data.containsKey(String.valueOf(KParamType
                                .SPO2_WAVE))) {
                            spo2Wave = data.getByteArray(String.valueOf(KParamType.SPO2_WAVE));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.SPO2_WAVE,
                                        spo2Wave);
                            }
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_II))) {
                            ecgIIWave = data.getByteArray(String.valueOf(KParamType.ECG_II));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_II, ecgIIWave);
                            }
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_I))) {
                            ecgIWave = data.getByteArray(String.valueOf(KParamType.ECG_I));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_I, ecgIWave);
                            }
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_III))) {
                            ecgIIIWave = data.getByteArray(String.valueOf(KParamType.ECG_III));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_III,
                                        ecgIIIWave);
                            }
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_AVR))) {
                            ecgAVRWave = data.getByteArray(String.valueOf(KParamType.ECG_AVR));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_AVR,
                                        ecgAVRWave);
                            }
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_AVL))) {
                            ecgAVLWave = data.getByteArray(String.valueOf(KParamType.ECG_AVL));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_AVL,
                                        ecgAVLWave);
                            }
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_AVF))) {
                            ecgAVFWave = data.getByteArray(String.valueOf(KParamType.ECG_AVF));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_AVF,
                                        ecgAVFWave);
                            }
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_V1))) {
                            ecgV1Wave = data.getByteArray(String.valueOf(KParamType.ECG_V1));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_V1, ecgV1Wave);
                            }
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_V2))) {
                            ecgV2Wave = data.getByteArray(String.valueOf(KParamType.ECG_V2));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_V2, ecgV2Wave);
                            }
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_V3))) {
                            ecgV3Wave = data.getByteArray(String.valueOf(KParamType.ECG_V3));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_V3, ecgV3Wave);
                            }
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_V4))) {
                            ecgV4Wave = data.getByteArray(String.valueOf(KParamType.ECG_V4));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_V4, ecgV4Wave);
                            }
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_V5))) {
                            ecgV5Wave = data.getByteArray(String.valueOf(KParamType.ECG_V5));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_V5, ecgV5Wave);
                            }
                        } else if (data.containsKey(String.valueOf(KParamType
                                .ECG_V6))) {
                            ecgV6Wave = data.getByteArray(String.valueOf(KParamType.ECG_V6));
                            if (sendMsg != null) {
                                sendMsg.sendWave(KParamType.ECG_V6, ecgV6Wave);
                            }
                        }
                        break;
                    case GlobalConstant.NET_NIBP_CONFIG:
                        if (sendMsg != null) {
                            sendMsg.sendConfig(msg.arg1, msg.arg2);
                        }
                        break;
                    case GlobalConstant.NET_SPO2_CONFIG:
                        if (msg.arg1 == 0x05) {
                            GlobalConstant.LEFF_OFF = msg.arg2;
                        }
                        if (sendMsg != null) {
                            sendMsg.sendConfig(msg.arg1, msg.arg2);
                        }
                        break;
                    case GlobalConstant.NET_ECG_CONFIG:
                        if (sendMsg != null) {
                            sendMsg.sendConfig(msg.arg1, msg.arg2);
                        }
                        break;
                    case GlobalConstant.NET_RESP_CONFIG:
                        if (sendMsg != null) {
                            sendMsg.sendConfig(msg.arg1, msg.arg2);
                        }
                        break;
                    //体温
                    case GlobalConstant.NET_TEMP_CONFIG:
                        if (sendMsg != null) {
                            sendMsg.sendConfig(msg.arg1, msg.arg2);
                        }
                        break;
                    case GlobalConstant.NET_CONNECT_CENTRAL:
                        //中央站解析
                        int centralState = msg.arg1;
                        sendCentralBoardCast(centralState);

                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 发送连接状态广播
     * @param centralState 中央站状态
     */
    private void sendCentralBoardCast(int centralState) {
        Intent intent = new Intent(BroadcastConstant.ACTION_CENTRAL_STATE);
        intent.putExtra(BroadcastConstant.CENTRAL_STATE, centralState);
        sendBroadcast(intent);
    }
}
