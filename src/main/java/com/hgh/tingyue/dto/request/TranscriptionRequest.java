package com.hgh.tingyue.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "音频转写请求")
public class TranscriptionRequest {

    @Schema(description = "是否使用流式转写", defaultValue = "false")
    private boolean stream = false;

    @Schema(description = "录音时长(毫秒)", defaultValue = "30000")
    private long duration = 30000;
}