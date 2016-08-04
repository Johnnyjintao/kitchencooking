package utils;

import android.content.Context;

/**
 * 设置缓存工具类
 * 
 * @author Kevin
 * 
 */
public class CacheUtils {

	/**
	 * 设置缓存，key是url，value是csv文件
	 */
	public static void setCache(String key, String value, Context ctx) {
		PrefUtils.setString(ctx, key, value);
	}

	/**
	 * 读取缓存
	 */
	public static String getCache(String key, Context ctx) {
		return PrefUtils.getString(ctx, key, null);
	}
}
