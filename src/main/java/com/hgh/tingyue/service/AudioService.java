package com.hgh.tingyue.service;

import io.reactivex.Flowable;
import java.nio.ByteBuffer;

public interface AudioService {
    /**
     * 文件语音识别
     *
     * @param audioFilePath 音频文件路径
     * @return 识别出的文本
     */
    String recognizeAudio(String audioFilePath);

    /**
     * 实时语音识别
     *
     * @param audioSource 音频数据流
     * @param callback    识别结果回调
     */
    void recognizeStreamAudio(Flowable<ByteBuffer> audioSource, RecognitionCallback callback);

    /**
     * 开始实时录音识别
     * 
     * @param durationMillis 录音时长(毫秒)
     * @param callback       识别结果回调
     */
    void startRecognition(long durationMillis, RecognitionCallback callback);
}