/**
 * 语言工具类
 *
 * @author Gray
 * @date 2024/1/9
 */
package com.hgh.tingyue.util;

import java.util.HashMap;
import java.util.Map;

public class LanguageUtils {
    private static final Map<String, String> LANGUAGE_CODES = new HashMap<>();

    static {
        LANGUAGE_CODES.put("zh", "中文");
        LANGUAGE_CODES.put("en", "英语");
        LANGUAGE_CODES.put("ja", "日语");
        LANGUAGE_CODES.put("ko", "韩语");
        LANGUAGE_CODES.put("fr", "法语");
        LANGUAGE_CODES.put("es", "西班牙语");
        LANGUAGE_CODES.put("ru", "俄语");
        LANGUAGE_CODES.put("de", "德语");
        LANGUAGE_CODES.put("it", "意大利语");
        LANGUAGE_CODES.put("pt", "葡萄牙语");
    }

    /**
     * 检查语言代码是否有效
     *
     * @param languageCode 语言代码
     * @return 是否有效
     */
    public static boolean isValidLanguageCode(String languageCode) {
        return languageCode != null && LANGUAGE_CODES.containsKey(languageCode.toLowerCase());
    }

    /**
     * 获取语言名称
     *
     * @param languageCode 语言代码
     * @return 语言名称
     */
    public static String getLanguageName(String languageCode) {
        return languageCode == null ? "未知语言" : LANGUAGE_CODES.getOrDefault(languageCode.toLowerCase(), "未知语言");
    }

    /**
     * 获取支持的语言列表
     *
     * @return 语言代码和名称的映射
     */
    public static Map<String, String> getSupportedLanguages() {
        return new HashMap<>(LANGUAGE_CODES);
    }
}