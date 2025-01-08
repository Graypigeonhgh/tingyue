package com.hgh.tingyue.dto.response;

import com.hgh.tingyue.entity.AudioFile;
import com.hgh.tingyue.entity.Transcription;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "音频文件详情响应")
public class AudioFileDetailResponse {
    @Schema(description = "音频文件信息")
    private AudioFile audioFile;

    @Schema(description = "转写记录")
    private Transcription transcription;
}