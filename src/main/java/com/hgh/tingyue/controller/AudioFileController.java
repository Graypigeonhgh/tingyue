package com.hgh.tingyue.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hgh.tingyue.entity.AudioFile;
import com.hgh.tingyue.service.AudioFileService;
import com.hgh.tingyue.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Tag(name = "音频文件管理", description = "音频文件的上传、删除、查询等接口")
@RestController
@RequestMapping("/api/audio-files")
@RequiredArgsConstructor
public class AudioFileController {
    private final AudioFileService audioFileService;

    @Operation(summary = "上传音频文件")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AudioFile> uploadFile(
            @Parameter(description = "音频文件", required = true)
            @RequestPart("file") MultipartFile file) {
        Long userId = SecurityUtils.getCurrentUserId();
        AudioFile audioFile = audioFileService.uploadAndSave(file, userId);
        return ResponseEntity.ok(audioFile);
    }

    @Operation(summary = "删除音频文件")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteFile(
            @Parameter(description = "文件ID", required = true)
            @PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        boolean success = audioFileService.deleteFile(id, userId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("success", success);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "获取音频文件列表")
    @GetMapping
    public ResponseEntity<IPage<AudioFile>> getFileList(
            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量", example = "10")
            @RequestParam(defaultValue = "10") int pageSize) {
        Long userId = SecurityUtils.getCurrentUserId();
        IPage<AudioFile> files = audioFileService.getUserFiles(userId, page, pageSize);
        return ResponseEntity.ok(files);
    }

    @Operation(summary = "获取音频文件详情")
    @GetMapping("/{id}")
    public ResponseEntity<AudioFile> getFileDetail(
            @Parameter(description = "文件ID", required = true)
            @PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        AudioFile file = audioFileService.getFileDetail(id, userId);
        return ResponseEntity.ok(file);
    }
} 