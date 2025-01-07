package com.hgh.tingyue.service.impl;

import com.alibaba.dashscope.audio.asr.recognition.Recognition;
import com.alibaba.dashscope.audio.asr.recognition.RecognitionParam;
import com.alibaba.dashscope.audio.asr.transcription.Transcription;
import com.alibaba.dashscope.audio.asr.transcription.TranscriptionParam;
import com.alibaba.dashscope.audio.asr.transcription.TranscriptionQueryParam;
import com.alibaba.dashscope.audio.asr.transcription.TranscriptionResult;
import com.alibaba.dashscope.audio.asr.transcription.TranscriptionTaskResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hgh.tingyue.service.AudioService;
import com.hgh.tingyue.service.RecognitionCallback;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AudioServiceImpl implements AudioService {

    @Value("${aliyun.dashscope.api-key}")
    private String apiKey;

    @Override
    public String recognizeAudio(String audioPath) {
        try {
            log.info("开始语音识别，路径：{}", audioPath);

            // 检查API密钥
            if (!StringUtils.hasText(apiKey)) {
                throw new RuntimeException("未配置阿里云API密钥");
            }

            // 构建参数
            TranscriptionParam param = TranscriptionParam.builder()
                    .apiKey(apiKey)
                    .model("paraformer-v2")
                    .fileUrls(Collections.singletonList(audioPath)) // 直接使用URL或本地路径
                    .parameter("language_hints", new String[] { "zh", "en" })
                    .build();

            // 创建转写客户端
            Transcription transcription = new Transcription();

            // 提交转写请求
            log.debug("提交转写请求");
            TranscriptionResult result = transcription.asyncCall(param);
            String taskId = result.getTaskId();
            log.info("转写任务已提交，TaskId: {}", taskId);

            // 等待转写完成
            log.debug("等待转写完成");
            result = transcription.wait(
                    TranscriptionQueryParam.FromTranscriptionParam(param, taskId));

            // 获取转写结果
            List<TranscriptionTaskResult> taskResultList = result.getResults();
            if (taskResultList == null || taskResultList.isEmpty()) {
                throw new RuntimeException("未获取到转写结果");
            }

            // 获取转写结果的URL
            String transcriptionUrl = taskResultList.get(0).getTranscriptionUrl();
            log.debug("获取转写结果URL: {}", transcriptionUrl);

            // 通过HTTP获取结果
            HttpURLConnection connection = (HttpURLConnection) new URL(transcriptionUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // 读取结果
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String rawJson = reader.lines().collect(Collectors.joining("\n"));
                log.debug("收到原始JSON响应: {}", rawJson);

                JsonObject jsonResult = new Gson().fromJson(rawJson, JsonObject.class);

                // 检查JSON结构
                if (jsonResult == null) {
                    throw new RuntimeException("转写结果为空");
                }

                // 获取transcripts数组中的text字段
                if (jsonResult.has("transcripts")) {
                    JsonArray transcripts = jsonResult.getAsJsonArray("transcripts");
                    if (transcripts != null && transcripts.size() > 0) {
                        String recognizedText = transcripts.get(0)
                                .getAsJsonObject()
                                .get("text")
                                .getAsString();
                        log.info("转写完成，文本长度：{}", recognizedText.length());
                        return recognizedText;
                    }
                }

                // 如果找不到任何文本内容，打印完整的JSON结构
                log.error("无法解析的JSON结构: {}", new GsonBuilder().setPrettyPrinting().create().toJson(jsonResult));
                throw new RuntimeException("无法从转写结果中获取文本内容，请检查日志中的JSON结构");
            }

        } catch (Exception e) {
            log.error("语音识别失败", e);
            throw new RuntimeException("语音识别失败：" + e.getMessage(), e);
        }
    }

    @Override
    public void recognizeStreamAudio(Flowable<ByteBuffer> audioSource, RecognitionCallback callback) {
        try {
            if (!StringUtils.hasText(apiKey)) {
                throw new RuntimeException("未配置阿里云API密钥");
            }

            Recognition recognizer = new Recognition();
            RecognitionParam param = RecognitionParam.builder()
                    .model("paraformer-realtime-v2")
                    .format("pcm")
                    .sampleRate(16000)
                    .apiKey(apiKey)
                    .build();

            recognizer.streamCall(param, audioSource)
                    .subscribe(
                            result -> {
                                String text = result.getSentence().getText();
                                boolean isFinal = result.isSentenceEnd();
                                callback.onResult(text, isFinal);
                                log.debug("识别结果: {}, isFinal: {}", text, isFinal);
                            },
                            error -> {
                                log.error("实时识别错误", error);
                                callback.onError(error);
                            });

        } catch (Exception e) {
            log.error("实时识别初始化失败", e);
            callback.onError(e);
        }
    }

    @Override
    public void startRecognition(long durationMillis, RecognitionCallback callback) {
        Flowable<ByteBuffer> audioSource = Flowable.create(
                emitter -> {
                    new Thread(() -> {
                        try {
                            AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, false);
                            TargetDataLine targetDataLine = AudioSystem.getTargetDataLine(audioFormat);
                            targetDataLine.open(audioFormat);
                            targetDataLine.start();

                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            long start = System.currentTimeMillis();

                            while (System.currentTimeMillis() - start < durationMillis) {
                                int read = targetDataLine.read(buffer.array(), 0, buffer.capacity());
                                if (read > 0) {
                                    buffer.limit(read);
                                    emitter.onNext(buffer);
                                    buffer = ByteBuffer.allocate(1024);
                                    Thread.sleep(20);
                                }
                            }

                            targetDataLine.stop();
                            targetDataLine.close();
                            emitter.onComplete();

                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    }).start();
                },
                BackpressureStrategy.BUFFER);

        recognizeStreamAudio(audioSource, callback);
    }
}