package com.hgh.tingyue.service;

public interface RecognitionCallback {
    /**
     * 实时识别结果回调
     * 
     * @param text    识别的文本
     * @param isFinal 是否为最终结果
     */
    void onResult(String text, boolean isFinal);

    /**
     * 识别错误回调
     */
    void onError(Throwable error);
}