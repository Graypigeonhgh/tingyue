/**
 * 翻译服务接口
 *
 * @author Gray
 * @date 2024/1/9
 */
package com.hgh.tingyue.service;

public interface TranslationService {
    /**
     * 文本翻译
     *
     * @param text           原文本
     * @param sourceLanguage 源语言
     * @param targetLanguage 目标语言
     * @return 翻译后的文本
     */
    String translate(String text, String sourceLanguage, String targetLanguage);
}