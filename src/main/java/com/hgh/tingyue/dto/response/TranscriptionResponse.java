package com.hgh.tingyue.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "音频转写响应")
public class TranscriptionResponse {

    @Schema(description = "转写文本")
    private String text;
}