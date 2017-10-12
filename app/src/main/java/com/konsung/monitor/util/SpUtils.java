package com.konsung.monitor.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by liangkun on 2017/10/12 0012.
 * SharedPreferences工具类
 */

public class SpUtils {
    /**
     * 私有构造
     */
    private SpUtils() {

    }
    /**
     * 获取指定key的int
     *
     * @param mContext mContext
     * @param name     name
     * @param key      key
     * @param defValue defValue
     * @return 返回值
     */
    public static synchronized int getSpInt(
            Context mContext, String name,
            String key,
            int defValue) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context
                .MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }
    /**
     * 保存字符串
     *
     * @param mContext mContext
     * @param name     name
     * @param key      key
     * @param value    value
     */
    public static synchronized void saveToSp(
            Context mContext, String name,
            String key,
            String value) {
        SharedPreferences sp = mContext.getSharedPreferences(name, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
