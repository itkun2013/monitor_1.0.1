package com.konsung.monitor.util;

import android.content.Context;

import com.konsung.monitor.netty.EchoServerEncoder;

/**
 * Created by liangkun on 2017/10/12 0012.
 * 常用工具类
 */

public class UiUtils {
    private static Context mContext;

    public static void initData(Context context) {
        mContext = context;
    }

    /**
     * 初始化设备配置
     */
    public static void initDeviceConfig() {
        int value = 0;
        //注意：需要优先进行设备配置初始化
        value = SpUtils.getSpInt(mContext, GlobalConstant.SYS_CONFIG,
                GlobalConstant.DEVICE_CONFIG_TAG, GlobalConstant.DEVICE_CONFIG);
        //当收到AppDeivce发来的数据后，才发生设备配置命令。
        EchoServerEncoder.setDeviceConfig(GlobalConstant.NET_DEVICE_INITIALIZE_CONFIG, value);
    }

    /**
     * 获取xmlStrings的数据
     * @param codingRepeat
     * @return
     */
    public static String getString(int codingRepeat) {
        return mContext.getString(codingRepeat);
    }

}
