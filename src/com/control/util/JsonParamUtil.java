package com.control.util;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
/**
 * 将字符串json参数转成map对象
 * @author caij
 *
 */
public class JsonParamUtil {
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getJsonObject(String param) {
		Map<String, Object> map = null;
		try {
			map = (Map<String, Object>) JSONObject.parse(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
