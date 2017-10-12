package com.konsung.monitor.netty;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.konsung.monitor.util.GlobalConstant;

import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * Created by liangkun on 2017/10/12 0012.
 * netty 服务器编码器
 * 此类主要是进行编码操作
 * 发送数据给AppDevice
 */

public class EchoServerDecoder extends ByteToMessageDecoder {
    private Handler handler;
    private Message message;

    /**
     * 带数据处理的构造器
     * @param handler 数据处理
     */
    public EchoServerDecoder(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf
            , List<Object> objects) throws Exception {

        while (byteBuf.isReadable()) {

            byteBuf.markReaderIndex();
            short head = byteBuf.readUnsignedByte();
            if (head != 0x00FF) { // 寻找包头
                System.out.printf("Head is error, %X\n", head);
                continue;
            }

            int len = byteBuf.order(ByteOrder.LITTLE_ENDIAN)
                    .readUnsignedShort();     // 长度
            if (byteBuf.readableBytes() + 3 < len) {
                byteBuf.resetReaderIndex();
                return;
            }

            byte cmdId = byteBuf.readByte();            // 命令字
            byte[] mingling = new byte[1];
            mingling[0] = cmdId;
            int serialNO = byteBuf.order(ByteOrder.LITTLE_ENDIAN).readUnsignedShort(); // 序列号
            int checkSum = byteBuf.order(ByteOrder.LITTLE_ENDIAN).readUnsignedShort(); // 校验和
            if (checkSum(byteBuf, len) != checkSum) {
                System.out.printf("Checksum is error, %X\n", checkSum);
                byteBuf.resetReaderIndex();
                byteBuf.readChar();
                continue;
            }

            ByteBuf data = byteBuf.order(ByteOrder.LITTLE_ENDIAN).readBytes(len - 8);
            switch (cmdId) {
                // 趋势数据
                case GlobalConstant.NET_TREND:
                    handleTrendPkg(data);
                    break;
                // 波形数据
                case GlobalConstant.NET_WAVE:

                    handleWavePkg(data);
                    break;
                case GlobalConstant.PARA_STATUS:
                    int param = data.order(ByteOrder.LITTLE_ENDIAN).readUnsignedShort();
                    short isActive = data.readUnsignedByte();
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(new byte[10]);

                    //获取参数模块名称和版本号
                    Bundle paraBundle = new Bundle();
                    byte[] temp = new byte[32];
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(temp);
                    for (int i = 0; i < temp.length; i++) {
                        if ((temp[i] & 0xFF) == 0) {
                            byte[] paraName = Arrays.copyOf(temp, i);
                            paraBundle.putByteArray("paraBoardName", paraName);
                            break;
                        }
                    }
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(temp);
                    for (int i = 0; i < temp.length; i++) {
                        if ((temp[i] & 0xFF) == 0) {
                            byte[] paraVersion = Arrays.copyOf(temp, i);
                            paraBundle.putByteArray("paraBoardVersion",
                                    paraVersion);
                            break;
                        }
                    }

                    message = new Message();
                    message.what = GlobalConstant.PARA_STATUS;
                    message.arg1 = param;
                    message.arg2 = isActive;
                    message.setData(paraBundle);
                    handler.sendMessage(message);
                    break;
                case GlobalConstant.NET_ECG_CONFIG:
                case GlobalConstant.NET_SPO2_CONFIG:
                case GlobalConstant.NET_NIBP_CONFIG:
                    //添加体温命令
                case GlobalConstant.NET_TEMP_CONFIG:
                    message = new Message();
                    message.what = cmdId;
                    message.arg1 = data.order(ByteOrder.LITTLE_ENDIAN)
                            .readShort();
                    message.arg2 = data.order(ByteOrder.LITTLE_ENDIAN)
                            .readInt();
                    handler.sendMessage(message);
                    break;
                case GlobalConstant.NET_PATIENT_CONFIG:
                    message = new Message();
                    message.what = cmdId;
                    Bundle bundle = new Bundle();
                    byte[] bytes = new byte[64];

                    byte[] born = new byte[7];
                    byte[] entry = new byte[7];
                    byte[] idcard = new byte[18];
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(new byte[64]);
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(new byte[64]);
                    byte type = data.order(ByteOrder.LITTLE_ENDIAN).readByte();
                    byte sex = data.order(ByteOrder.LITTLE_ENDIAN).readByte();
                    bundle.putByte("type", type);
                    bundle.putByte("sex", sex);
                    byte blood = data.order(ByteOrder.LITTLE_ENDIAN).readByte();
                    int weight = data.order(ByteOrder.LITTLE_ENDIAN).readInt();
                    int height = data.order(ByteOrder.LITTLE_ENDIAN).readInt();
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(new byte[2]);
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(born);
                    bundle.putByteArray("born", born);
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(entry);
                    byte isbo = data.order(ByteOrder.LITTLE_ENDIAN).readByte();
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(idcard)
                            .readBytes(new byte[46]);
                    data.order(ByteOrder.LITTLE_ENDIAN).readBytes(bytes);
                    for (int i = 0; i < bytes.length; i++) {
                        if ((bytes[i] & 0xFF) == 0) {
                            byte[] name = Arrays.copyOf(bytes, i);
                            bundle.putByteArray("name", name);
                            break;
                        }
                    }
                    /*String id=new String(bytes,"UTF-8");*/
                    bundle.putByteArray("idcard", idcard);

                    message.setData(bundle);
                    handler.sendMessage(message);
                    break;
                case GlobalConstant.NET_CONNECT_CENTRAL:
                    //中央站解析
                    message = new Message();
                    message.what = cmdId;
                    message.arg1 = data.order(ByteOrder.LITTLE_ENDIAN).readByte();
                    handler.sendMessage(message);
                    break;
                default:
                    message = new Message();
                    message.what = cmdId;
                    message.arg1 = data.order(ByteOrder.LITTLE_ENDIAN)
                            .readShort();
                    message.arg2 = data.order(ByteOrder.LITTLE_ENDIAN)
                            .readInt();
                    handler.sendMessage(message);
                    break;
            }
        }
    }

    /**
     * 校验和
     * @param buf byte容器
     * @param len 长度
     * @return 校验和
     */
    private int checkSum(ByteBuf buf, int len) {
        int sum = 0;
        for (int i = 0; i < len; i++) {
            // 校验位以及头不参与计算
            if (i == 0 || i == 6 || i == 7) {
                continue;
            }
            sum += buf.getUnsignedByte(i);
        }
        return sum & 0xFFFF;
    }

    /**
     * 读取趋势数据
     * @param buf 网络字节
     * 每一对参数类型与参数值的字节数为6个
     */
    private void handleTrendPkg(ByteBuf buf) {
        // 参数数量
        netTime(buf.readBytes(7));
        buf.order(ByteOrder.LITTLE_ENDIAN).readShort();      // 保留字节
        while (buf.isReadable()) {
            int param = buf.order(ByteOrder.LITTLE_ENDIAN).readUnsignedShort(); // 参数类型
            int value = buf.order(ByteOrder.LITTLE_ENDIAN).readInt();
            // 参数数值

            // message 数据载体
            message = new Message();
            message.what = GlobalConstant.NET_TREND;
            message.arg1 = param;
            message.arg2 = value;
            handler.sendMessage(message);
        }
    }

    /**
     * 读取波形数据
     * @param buf 网络字节
     */
    private void handleWavePkg(ByteBuf buf) {

        int param = buf.order(ByteOrder.LITTLE_ENDIAN).readUnsignedShort();
        // 参数类型
        buf.readBytes(9);
        int waveSize = buf.order(ByteOrder.LITTLE_ENDIAN).readUnsignedShort();
        Bundle temp = new Bundle();
        temp.putByteArray(String.valueOf(param), buf.readBytes(waveSize)
                .array());
        message = new Message();
        message.what = GlobalConstant.NET_WAVE;
        message.setData(temp);

        handler.sendMessage(message);
    }

    /**
     * 读取网络时间
     * @param buf 网络字节
     */
    private void netTime(ByteBuf buf) {
        // 时间戳为7个字节
        if (buf.capacity() != 7) {
            return;
        }
        short year = buf.order(ByteOrder.LITTLE_ENDIAN).readShort();     // 年
        byte month = buf.readByte();        // 月
        byte day = buf.readByte();          // 日
        byte hour = buf.readByte();         // 时
        byte minute = buf.readByte();       // 分
        byte second = buf.readByte();       // 秒
    }
}
