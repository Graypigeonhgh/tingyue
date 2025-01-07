/**
 * 翻译控制器
 *
 * @author Gray
 * @date 2024/1/9
 */
package com.hgh.tingyue.controller;

import com.hgh.tingyue.service.TranslationService;
import com.hgh.tingyue.util.LanguageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "翻译管理", description = "文本翻译相关接口")
@RestController
@RequestMapping("/api/translation")
@RequiredArgsConstructor
public class TranslationController {
    private final TranslationService translationService;

    @Operation(summary = "文本翻译", description = "将文本从源语言翻译为目标语言")
    @PostMapping("/translate")
    public ResponseEntity<?> translate(
            @Parameter(description = "待翻译文本", required = true) @RequestParam String text,
            @Parameter(description = "源语言代码，如：zh、en", required = true) @RequestParam String sourceLanguage,
            @Parameter(description = "目标语言代码，如：en、zh", required = true) @RequestParam String targetLanguage) {
        String translatedText = translationService.translate(text, sourceLanguage, targetLanguage);
        return ResponseEntity.ok(translatedText);
    }

    @Operation(summary = "获取支持的语言列表", description = "获取系统支持的所有语言列表")
    @GetMapping("/languages")
    public ResponseEntity<?> getSupportedLanguages() {
        return ResponseEntity.ok(LanguageUtils.getSupportedLanguages());
    }
}