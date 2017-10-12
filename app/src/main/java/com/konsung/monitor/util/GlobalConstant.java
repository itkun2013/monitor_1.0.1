package com.konsung.monitor.util;

/**
 * Created by liangkun on 2017/10/12 0012.
 * 全局变量
 */

public class GlobalConstant {
    public static final String APPDEVICE = "AppDevice"; //ip
    //探头脱落状态
    public static int LEFF_OFF = -1000;
    // 网络端口号
    public static final int PORT = 6613;
    // 无效趋势值
    public static final int INVALID_TREND_DATA = -1000;
    // 网络数据包最大长度
    public static final int MAX_PACKET_LEN = 1024;
    /**
     * 网络命令字
     */
    public static final byte NET_PATIENT_CONFIG = 0x11; //病人信息配置包
    public static final byte PARA_STATUS = 0x12; //参数激活状态包
    public static final byte NET_ECG_CONFIG = 0x21; //ECG配置包
    public static final byte NET_RESP_CONFIG = 0x22; //RESP配置包
    public static final byte NET_TEMP_CONFIG = 0x23; //TEMP体温配置包
    public static final byte NET_SPO2_CONFIG = 0x24; //SPO2配置包
    public static final byte NET_NIBP_CONFIG = 0x25; //NIBP配置包
    public static final byte NET_TREND = 0x51; //趋势数据包
    public static final byte NET_WAVE = 0x52; //波形数据包
    public static final byte NET_DEVICE_CONFIG = 0x70; //设备配置包
    public static final byte NET_SERVER_CONFIG = 0x71; //服务端ip 或者端口命令字
    public static final byte NET_CONNECT_CENTRAL = 0x72; //中央站连接命令
    public static final short NET_DEVICE_INITIALIZE_CONFIG = 0x00;   // 设备配置指令
    /**
     * 启动aidl和appdevice
     */
    public static final String ACTION_SERVICE = "com.konsung.aidlService";
    public static final String ACTION_RESTART = "com.konsung.AppDevice.start";
    /**
     * 其他
     */

    public static final int ECG_NUM = 5;    //ECG导联数，默认为三，在设置中可以设置
    public static final String CRASHLOGPATH = "/data/data/com.konsung.monitor/log/";  //奔溃储存的位置
    /**
     * 设备配置信息
     */
    public static final String SYS_CONFIG = "sys_config"; //系统设置的tag
    public static final String DEVICE_CONFIG_TAG = "device_config"; //记录系统信息的tag
    public static final int DEVICE_CONFIG = 0xFFFF;
    public static final String APP_CONFIG= "app_config"; //参数版版本信息
    public static final String PARA_BOARDNAME= "paraBoardName"; //参数版版本名称
    public static final String PARABOARD_VERSION= "paraBoardName"; //参数版版本号

//    /**
//     * 数据库储存的key
//     */
//    public static final int SERVER_IP_CONFIG = 190101; //ip
//    public static final int SERVER_PORT_CONFIG = 190102; //端口
//    public static final int SERVER_DEVICE_CONFIG = 200001; //设备号
//
//    public static final int ACTION_CREAT_USER = 1000; //创建新用户
//    public static final int ECG_WAVE_GAIN = 110106; //心电波形增益
//    public static final int ECG_WAVE_SMOOTH = 110107; //滤波方式
//    public static final int ECG_WAVE_SPEED = 110108; //心电波形速度
//
//    public static final int ECG_DL = 110101; //心电导联类型
//    public static final int ECG_ST = 110104; //心电ST分析
//
//    public static final int ECG_ONOFF = 110201; //心率开关
//    public static final int ECG_UP = 110202; //心率上限
//    public static final int ECG_DOWN = 110203; //心率下限
//
//    public static final int PVC_ONOFF = 110204; //PVC开关
//    public static final int PVC_UP = 110205; //PVC上限
//    public static final int PVC_DOWN = 110206; //PVC下限
//    public static final int LEAD_MOLD = 105; //导联类型
//    public static final int ST_ONOFF = 110207; //ST开关
//    public static final int ST_UP = 110208; //ST上限
//    public static final int ST_DOWN = 110209; //ST下限
//
//    public static final int SPO_UP = 120202; //SPO上限
//    public static final int SPO_DOWN = 120203; //SPO上限
//
//    public static final int SPO_BPM_UP = 120205; //SPO脉率上限
//    public static final int SPO_BPM_DOWN = 120206; //SPO脉率上限
//
//    public static final int BREATHE_UP = 130202; //呼吸率上限
//    public static final int BREATHE_DOWN = 130203; //呼吸率下限
//
//    public static final int PACE_MAKING = 110102; //起搏开关
//    public static final int LEAD_COUNT = 110109; //计算导联


    /**
     * 中央站的连接状态标志
     */
    public static final int CENTRAL_CONNECT_SUCCESS = 1; //成功
    public static final int CENTRAL_CONNECT_FAIL = 0; //失败
}
