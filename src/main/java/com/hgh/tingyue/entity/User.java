/**
 * 用户实体类
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
@TableName("user")
@Schema(description = "用户信息")
public class User {
    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "test123")
    private String username;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱", example = "test@example.com")
    private String email;

    /**
     * 密码哈希值
     */
    @Schema(description = "密码哈希值", example = "$2a$10$XXXXXX")
    private String passwordHash;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}