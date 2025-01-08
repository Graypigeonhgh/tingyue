/**
 * 音频文件实体类
 * 
 * @author Gray
 * @date 2024/1/9
 */
package com.hgh.tingyue.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName(value = "audio_files", autoResultMap = true)
@Schema(description = "音频文件信息")
public class AudioFile {
    @TableId(type = IdType.AUTO)
    @Schema(description = "文件ID")
    private Long id;

    @Schema(description = "文件名称", example = "recording.mp3")
    @TableField(value = "file_name")
    private String fileName;

    @Schema(description = "文件URL", example = "https://example.com/audio/xxx.mp3")
    @TableField(value = "file_url")
    private String fileUrl;

    @Schema(description = "文件大小(字节)", example = "1024000")
    @TableField(value = "file_size")
    private Long fileSize;

    @Schema(description = "文件类型", example = "audio/mpeg")
    @TableField(value = "file_type")
    private String fileType;

    @Schema(description = "用户ID", example = "1")
    @TableField(value = "user_id")
    private Long userId;

    @Schema(description = "创建时间")
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @Schema(description = "备注")
    @TableField(value = "remark")
    private String remark;
}