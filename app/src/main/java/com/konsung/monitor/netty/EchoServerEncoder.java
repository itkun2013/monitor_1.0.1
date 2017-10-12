package com.konsung.monitor.netty;

import com.konsung.monitor.util.GlobalConstant;
import com.konsung.monitor.util.LogUtils;

import java.nio.ByteOrder;
import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by liangkun on 2017/10/12 0012.
 * 发送数据给AppDevice * netty 服务器编码器
 * 此类主要是进行编码操作
 * 此类事服务器端
 */

public class EchoServerEncoder extends ChannelInboundHandlerAdapter {
    // mBuf包含帧头和帧体的每包数据
    private static ArrayList<ByteBuf> _cmds = new ArrayList<>();
    // 帧体
    private static ByteBuf mBodyBuf;

    // 序列号，从0开始递增
    private static int mSerialNo = 0;
    private static final int HEAD_SIZE = 8;

    /**
     * 发送网络数据
     * mBuf.clear() 只是将readerIndex和writerIndex 置为0，并不清空数据
     * @param cmdId 命令字ID,传递16进制数据
     * @param len 长度
     */
    private static void sendData(byte cmdId, int len) {
        ByteBuf buff = Unpooled.buffer();
        // 帧头
        buff.writeByte(0xFF);
        buff.order(ByteOrder.LITTLE_ENDIAN).writeShort(HEAD_SIZE + len);
        buff.writeByte(cmdId);
        buff.order(ByteOrder.LITTLE_ENDIAN).writeShort(mSerialNo++);

        // 将读写指针放在校验位,将校验位补为0
        mBodyBuf.resetReaderIndex();
        buff.markWriterIndex();
        buff.order(ByteOrder.LITTLE_ENDIAN).writeShort(0);
        // 在校验位后加上帧体内容
        int sum = checkSum(buff.writeBytes(mBodyBuf));
        // 将写指针重置到校验位重新赋值
        buff.resetWriterIndex();
        buff.order(ByteOrder.LITTLE_ENDIAN).writeShort(sum);
        // 重新赋帧体内容
        mBodyBuf.resetReaderIndex();
        buff.writeBytes(mBodyBuf);

        _cmds.add(buff);
    }

    /**
     * 向网络发送ip 端口变化
     * @param ip ip地址
     * @param port 端口
     */
    public static void setServerAddress(String ip, short port) {
        mBodyBuf = Unpooled.buffer();
        mBodyBuf.markReaderIndex();
        ////协议String必须是64长度
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeBytes(getStringBytes(ip, new byte[64]));
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeShort(port);
        sendData(GlobalConstant.NET_SERVER_CONFIG, mBodyBuf.readableBytes());
    }

    /**
     * 得到将String转为协议的bytess数组
     * @param ip 需要转的
     * @param string64 得到的
     * @return 得到bytess数组
     */
    private static byte[] getStringBytes(String ip, byte[] string64) {
        byte[] stringIp = ip.getBytes();
        for (int i = 0; i < string64.length; i++) {
            //字节数少于ip长度，赋值，否者补0
            if (i < stringIp.length) {
                string64[i] = stringIp[i];
            } else {
                string64[i] = 0;
            }
        }
        return string64;
    }

    /**
     * 发送病人信息包
     * @param cycle 形态（成人，小儿，新生儿）
     * @param sex 性别
     * @param blood 血型
     * @param weight 体重
     * @param height 身高
     * @param isbegin 起搏
     * @param medicalRecord 病例号
     * @param patientSurname 病人姓
     * @param patientName 病人名
     * @param doctorName 医生姓名
     * @param office 科室
     * @param creatTime 用户id
     * @param bedNum 床号
     */
    public static void setPatientConfig(short cycle, short sex, short blood, float weight, float
            height, short isbegin, String medicalRecord, String patientSurname, String
            patientName, String doctorName, String office, String creatTime, short bedNum) {
        mBodyBuf = Unpooled.buffer();
        mBodyBuf.markReaderIndex();
        //得到设备号
//        String deviceId = DPUtils.getStringBySortAttrId(UIUtils.getContext(), GlobalConstant
//                .SERVER_DEVICE_CONFIG);
        String deviceId = "";
        //出生时间和入院时间
        byte[] time = new byte[7];
        for (byte i : time) {
            i = 0;
        }
        //设备ID
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeBytes(getStringBytes(deviceId, new byte[64]));
        //用户id默认空
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeBytes(getStringBytes(creatTime, new byte[64]));
        //床号
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeShort(bedNum);
        //病人类型
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeByte(cycle);
        //病人性别
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeByte(sex);
        //病人血型
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeByte(blood);
        //病人体重
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeInt((int) weight * 10);
        //病人身高
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeInt((int) height * 10);
        //出生时间
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeBytes(time);
        //入院时间
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeBytes(time);
        //起搏
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeByte(isbegin);
        //病历号
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeBytes(getStringBytes(medicalRecord, new
                byte[64]));
        //病人姓
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeBytes(getStringBytes(patientSurname, new
                byte[64]));
        //病人名
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeBytes(getStringBytes(patientName, new
                byte[64]));
        //医生姓名
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeBytes(getStringBytes(doctorName, new
                byte[64]));
        //科室
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeBytes(getStringBytes(office, new
                byte[64]));

        sendData(GlobalConstant.NET_PATIENT_CONFIG, mBodyBuf.readableBytes());
    }

    /**
     * 向网络发送ECG配置包
     * @param cmdId 命令字ID
     * @param value 值
     */
    public static void setEcgConfig(short cmdId, int value) {
        setConfig(GlobalConstant.NET_ECG_CONFIG, cmdId, value);
        setConfig(GlobalConstant.NET_ECG_CONFIG, cmdId, value);
    }

    /**
     * 向网络发送NIBP配置包
     * @param cmdId 命令字ID
     * @param value 值
     */
    public static void setNibpConfig(short cmdId, int value) {
        setConfig(GlobalConstant.NET_NIBP_CONFIG, cmdId, value);
        setConfig(GlobalConstant.NET_NIBP_CONFIG, cmdId, value);
    }

    /**
     * 向网络发送TEMP配置包e
     * @param cmdId 命令字ID
     * @param value 值
     */
    public static void setTempConfig(short cmdId, int value) {
        setConfig(GlobalConstant.NET_TEMP_CONFIG, cmdId, value);
        setConfig(GlobalConstant.NET_TEMP_CONFIG, cmdId, value);
    }

    /**
     * 向网络发送Resp配置包
     * @param cmdId 命令字ID
     * @param value 值
     */
    public static void setRespConfig(short cmdId, int value) {
        setConfig(GlobalConstant.NET_RESP_CONFIG, cmdId, value);
        setConfig(GlobalConstant.NET_RESP_CONFIG, cmdId, value);
    }

    /**
     * 向网络发送设备配置包
     * @param cmdId 命令字ID
     * @param value 值
     */
    public static void setDeviceConfig(short cmdId, int value) {
        setConfig(GlobalConstant.NET_DEVICE_CONFIG, cmdId, value);
        setConfig(GlobalConstant.NET_DEVICE_CONFIG, cmdId, value);
    }

    /**
     * 向socket发送配置包
     * @param cmdId 命令字id
     * @param configType 配置类型
     * @param configValue 配置值
     */
    public static void setConfig(byte cmdId, short configType, int configValue) {
        mBodyBuf = Unpooled.buffer();
        mBodyBuf.markReaderIndex();
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeShort(configType);
        mBodyBuf.order(ByteOrder.LITTLE_ENDIAN).writeInt(configValue);
        sendData(cmdId, mBodyBuf.readableBytes());
    }

    /**
     * 校验和
     * @return sum
     */
    private static int checkSum(ByteBuf buf) {
        int sum = 0;
        for (int i = 0; i < buf.capacity(); i++) {
            // 校验位以及头不参与计算
            if (i == 0 || i == 6 || i == 7) {
                continue;
            }
            sum += buf.getUnsignedByte(i);
        }
        return sum;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        if (_cmds.isEmpty()) {
            return;
        }

        for (int i = 0; i < _cmds.size(); i++) {
            ctx.writeAndFlush(_cmds.remove(i));
        }
        _cmds.clear();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        //Channel是活跃状态（连接到某个远端），可以收发数据
        LogUtils.e(GlobalConstant.APPDEVICE,"channelActive1111111111111111");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
//        Channel未连接到远端
        LogUtils.e(GlobalConstant.APPDEVICE,"channelInactive11222222222222222222");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LogUtils.e(GlobalConstant.APPDEVICE,"channelRead33333333333333");
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        LogUtils.e(GlobalConstant.APPDEVICE,"exceptionCaught44444444444444444");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        LogUtils.e(GlobalConstant.APPDEVICE,"channelRegistered5555555555555555");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        LogUtils.e(GlobalConstant.APPDEVICE,"channelUnregistered6666666666666666666666");
    }
}
