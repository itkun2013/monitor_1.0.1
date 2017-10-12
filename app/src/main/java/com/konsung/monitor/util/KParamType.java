package com.konsung.monitor.util;

/**
 * Created by liangkun on 2017/10/12 0012.
 * 参数类型
 * 这些参数与AppDevice保持一致
 * 参数数值由协议指定
 * 目前只写了程序中使用到的部分趋势子参数的值
 * 后期可以在本类中增加相应的子参数
 * 这些值必须与协议保持一致
 */

public class KParamType {
    public static final int ECG_I = 1;
    public static final int ECG_II = 2;
    public static final int ECG_III = 3;
    public static final int ECG_AVR = 4;
    public static final int ECG_AVL = 5;
    public static final int ECG_AVF = 6;
    public static final int ECG_V1 = 7;
    public static final int ECG_V2 = 8;
    public static final int ECG_V3 = 9;
    public static final int ECG_V4 = 10;
    public static final int ECG_V5 = 11;
    public static final int ECG_V6 = 12;

    // ECG趋势子参数
    public static final int ECG_HR = 14;
    public static final int ECG_PVC = 15;
    public static final int ECG_ST = 18;


    // resp趋势子参数
    public static final int RESP_RR = 102;

    // spo2趋势子参数
    public static final int SPO2_WAVE = 201;
    public static final int SPO2_TREND = 202;
    public static final int SPO2_PR = 203;

    // temp趋势子参数
    public static final int TEMP_T1 = 301;
    public static final int TEMP_T2 = 302;
    public static final int TEMP_TD = 303;

    // irTemp趋势子参数
    public static final int IRTEMP_TREND = 401;  //红外耳温

    // nibp趋势子参数
    public static final int NIBP_SYS = 501;
    public static final int NIBP_DIA = 502;
    public static final int NIBP_MAP = 503;
    public static final int NIBP_PR = 504;

    // bloodGlu趋势子参数
    public static final int BLOODGLU_BEFORE_MEAL = 901;
    public static final int BLOODGLU_AFTER_MEAL = 902;

    // urineRt趋势子参数
    public static final int URINERT_LEU = 1201;
    public static final int URINERT_NIT = 1202;
    public static final int URINERT_UBG = 1203;
    public static final int URINERT_PRO = 1204;
    public static final int URINERT_PH = 1205;
    public static final int URINERT_BLD = 1206;
    public static final int URINERT_SG = 1207;
    public static final int URINERT_BIL = 1208;
    public static final int URINERT_KET = 1209;
    public static final int URINERT_GLU = 1210;
    public static final int URINERT_VC = 1211;
    public static final int URINERT_ALB = 1212;
    public static final int URINERT_ASC = 1213;
    public static final int URINERT_CRE = 1214;
    public static final int URINERT_CA = 1215;
}
