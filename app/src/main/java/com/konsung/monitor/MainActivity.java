package com.konsung.monitor;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.konsung.monitor.service.KonsungBinderService;
import com.konsung.monitor.util.BroadcastConstant;
import com.konsung.monitor.util.GlobalConstant;
import com.konsung.monitor.util.LogUtils;

public class MainActivity extends AppCompatActivity {
KonsungBinderService.MsgBinder binder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initService();
    }

    /**
     * 初始化服务
     */
    private void initService() {
        Intent intent = new Intent(BroadcastConstant.ACTION_SERVICE);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (KonsungBinderService.MsgBinder) service;
            binder.setSendMsgToPresent(new KonsungBinderService.SendMsgToPresent() {
                @Override
                public void sendWave(int param, byte[] bytes) {
                    LogUtils.e(GlobalConstant.APPDEVICE,"param "+param);
                }

                @Override
                public void sendTrend(int param, int value) {

                }

                @Override
                public void sendConfig(int param, int value) {

                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
