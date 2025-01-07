/**
 * 翻译服务实现类
 *
 * @author Gray
 * @date 2024/1/9
 */
package com.hgh.tingyue.service.impl;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.generation.models.QwenParam;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.hgh.tingyue.service.TranslationService;
import com.hgh.tingyue.util.LanguageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TranslationServiceImpl implements TranslationService {

    @Value("${aliyun.dashscope.api-key}")
    private String apiKey;

    @Override
    public String translate(String text, String sourceLanguage, String targetLanguage) {
        // 参数验证
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException("翻译文本不能为空");
        }
        if (!LanguageUtils.isValidLanguageCode(sourceLanguage)) {
            throw new IllegalArgumentException("无效的源语言代码: " + sourceLanguage);
        }
        if (!LanguageUtils.isValidLanguageCode(targetLanguage)) {
            throw new IllegalArgumentException("无效的目标语言代码: " + targetLanguage);
        }

        try {
            log.info("开始翻译文本，源语言：{}，目标语言：{}", sourceLanguage, targetLanguage);

            // 检查API密钥
            if (!StringUtils.hasText(apiKey)) {
                throw new RuntimeException("未配置阿里云API密钥");
            }

            Generation gen = new Generation();
            List<Message> messages = new ArrayList<>();

            // 构建系统提示语
            Message systemMsg = Message.builder()
                    .role(Role.SYSTEM.getValue())
                    .content(String.format("你是一个专业的翻译助手。请将以下%s文本准确翻译成%s，保持原文的语气和风格。只需要返回翻译结果，不要添加任何解释或其他内容。",
                            LanguageUtils.getLanguageName(sourceLanguage),
                            LanguageUtils.getLanguageName(targetLanguage)))
                    .build();

            // 构建用户消息
            Message userMsg = Message.builder()
                    .role(Role.USER.getValue())
                    .content(text)
                    .build();

            messages.add(systemMsg);
            messages.add(userMsg);

            QwenParam param = QwenParam.builder()
                    .model(Generation.Models.QWEN_TURBO)
                    .messages(messages)
                    .apiKey(apiKey)
                    .resultFormat("message") // 指定返回格式
                    .build();

            log.debug("发送翻译请求，文本长度：{}", text.length());
            GenerationResult result = gen.call(param);
            log.debug("收到翻译响应：{}", result);

            if (result == null) {
                throw new RuntimeException("翻译服务返回结果为空");
            }

            if (result.getOutput() == null) {
                throw new RuntimeException("翻译服务返回的输出为空");
            }

            if (result.getOutput().getChoices() == null || result.getOutput().getChoices().isEmpty()) {
                log.error("翻译服务返回的完整结果：{}", result);
                throw new RuntimeException("翻译服务返回的选项为空");
            }

            String translatedText = result.getOutput().getChoices().get(0).getMessage().getContent();
            if (!StringUtils.hasText(translatedText)) {
                throw new RuntimeException("翻译结果为空");
            }

            log.info("翻译完成，原文长度：{}，译文长度：{}", text.length(), translatedText.length());
            return translatedText;

        } catch (Exception e) {
            log.error("翻译失败，原因：", e);
            String errorMessage = e.getMessage();
            if (e instanceof RuntimeException && errorMessage.contains("InvalidApiKey")) {
                throw new RuntimeException("API密钥无效，请检查配置");
            } else if (e instanceof RuntimeException && errorMessage.contains("QuotaExceeded")) {
                throw new RuntimeException("API调用次数已达上限");
            }
            throw new RuntimeException("翻译失败：" + errorMessage, e);
        }
    }
}