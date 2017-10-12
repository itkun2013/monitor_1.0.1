package com.konsung.monitor.util;

/**
 * Created by liangkun on 2017/10/12 0012.
 * 所有广播的常量
 */

public class BroadcastConstant {
    //病人信息改变
    public static final String PAIENT_CHANGE = "com.konsung.patientChange.receiver";
    //中央站连接广播
    public static final String ACTION_CENTRAL_STATE = "com.konsung.central.change";
    //中央站连接广播状态
    public static final String CENTRAL_STATE = "state";
    //服务动作
    public static final String ACTION_SERVICE = "com.konsung.konsungService";
}
