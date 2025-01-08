package com.hgh.tingyue.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "音频文件更新请求")
public class AudioFileUpdateRequest {
    @Size(max = 255, message = "文件名长度不能超过255个字符")
    @Schema(description = "文件名", example = "新的文件名.mp3")
    private String fileName;
}