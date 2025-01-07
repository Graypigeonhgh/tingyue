/**
 * 音频文件实体类
 * 
 * @author Gray
 * @date 2024/1/9
 */
package com.hgh.tingyue.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AudioFile {
    private Long id;
    private String fileName;
    private String fileUrl;
    private Long fileSize;
    private String fileType;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}