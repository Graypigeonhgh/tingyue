/**
 * 转写记录实体类
 * 
 * @author Gray
 * @date 2024/1/9
 */
package com.hgh.tingyue.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Transcription {
    /**
     * 转写记录ID
     */
    private Long id;

    /**
     * 音频文件ID
     */
    private Long audioFileId;

    /**
     * 转写文本内容
     */
    private String content;

    /**
     * 转写状态（进行中/完成/失败）
     */
    private String status;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}