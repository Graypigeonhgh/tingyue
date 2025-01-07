package com.hgh.tingyue.service;

import io.reactivex.Flowable;
import java.nio.ByteBuffer;

public interface AudioService {
    /**
     * 识别音频文件
     *
     * @param audioPath 音频文件路径或URL
     * @return 识别结果文本
     */
    String recognizeAudio(String audioPath);

    /**
     * 识别音频流
     *
     * @param audioSource 音频数据流
     * @param callback    回调接口
     */
    void recognizeStreamAudio(Flowable<ByteBuffer> audioSource, RecognitionCallback callback);

    /**
     * 开始录音识别
     *
     * @param durationMillis 录音时长（毫秒）
     * @param callback       回调接口
     */
    void startRecognition(long durationMillis, RecognitionCallback callback);
}