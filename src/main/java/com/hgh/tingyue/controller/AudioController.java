/**
 * 音频处理控制器
 *
 * @author Gray
 * @date 2024/1/9
 */
package com.hgh.tingyue.controller;

import com.hgh.tingyue.dto.request.TranscriptionRequest;
import com.hgh.tingyue.dto.response.TranscriptionResponse;
import com.hgh.tingyue.entity.AudioFile;
import com.hgh.tingyue.entity.Transcription;
import com.hgh.tingyue.service.*;
import com.hgh.tingyue.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/audio")
@Tag(name = "音频处理", description = "音频转写、实时语音识别等接口")
@RequiredArgsConstructor
public class AudioController {

    private final AudioService audioService;
    private final OssService ossService;
    private final AudioFileService audioFileService;
    private final TranscriptionService transcriptionService;

    @Operation(summary = "开始实时语音识别")
    @PostMapping("/recognize/stream")
    public SseEmitter startStreamRecognition(
            @RequestParam(required = false, defaultValue = "30000") long duration) {

        SseEmitter emitter = new SseEmitter(duration + 5000); // 比录音时间多5秒

        audioService.startRecognition(duration, new RecognitionCallback() {
            @Override
            public void onResult(String text, boolean isFinal) {
                try {
                    emitter.send(SseEmitter.event()
                            .data(new TranscriptionResponse(text))
                            .id(UUID.randomUUID().toString())
                            .name(isFinal ? "final" : "partial"));
                } catch (IOException e) {
                    log.error("发送识别结果失败", e);
                    emitter.completeWithError(e);
                }
            }

            @Override
            public void onError(Throwable error) {
                log.error("语音识别错误", error);
                emitter.completeWithError(error);
            }
        });

        emitter.onCompletion(() -> log.info("语音识别完成"));
        emitter.onTimeout(() -> log.info("语音识别超时"));
        emitter.onError(error -> log.error("语音识别错误", error));

        return emitter;
    }

    @Operation(summary = "上传本地音频文件并转写")
    @PostMapping(value = "/upload-and-transcribe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TranscriptionResponse> uploadAndTranscribeAudio(
            @RequestPart("file") MultipartFile file) {
        try {
            // 验证用户登录状态
            SecurityUtils.validateUser();

            log.info("接收到音频文件：{}，大小：{} bytes", file.getOriginalFilename(), file.getSize());

            // 验证文件
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(new TranscriptionResponse("文件为空"));
            }

            // 验证文件格式
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("audio/")) {
                return ResponseEntity.badRequest().body(new TranscriptionResponse("不支持的文件格式：" + contentType));
            }

            // 获取当前用户ID
            Long userId = SecurityUtils.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new TranscriptionResponse("请先登录"));
            }

            // 上传文件并保存记录
            AudioFile audioFile = audioFileService.uploadAndSave(file, userId);

            // 调用音频识别服务
            String transcription = audioService.recognizeAudio(audioFile.getFileUrl());
            log.info("音频转写完成，文本长度：{}", transcription.length());

            // 保存转写记录
            transcriptionService.save(audioFile.getId(), transcription, userId);

            return ResponseEntity.ok(new TranscriptionResponse(transcription));

        } catch (Exception e) {
            log.error("音频处理失败", e);
            String errorMessage;
            if (e instanceof RuntimeException && e.getMessage().contains("用户未登录")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new TranscriptionResponse("请先登录"));
            } else if (e instanceof IOException) {
                errorMessage = "音频文件处理失败: " + e.getMessage();
            } else {
                errorMessage = "音频转写失败: " + e.getMessage();
            }
            return ResponseEntity.internalServerError().body(new TranscriptionResponse(errorMessage));
        }
    }
}