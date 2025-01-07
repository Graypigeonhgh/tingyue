/**
 * 转写记录实体类
 * 
 * @author Gray
 * @date 2024/1/9
 */
package com.hgh.tingyue.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Transcription {
    /**
     * 转写记录ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 音频文件ID
     */
    private Long audioFileId;

    /**
     * 转写文本内容
     */
    private String textContent;

    /**
     * 语言
     */
    private String language;

    /**
     * 转写状态（进行中/完成/失败）
     */
    private String status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}