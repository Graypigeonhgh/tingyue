package com.hgh.tingyue.util;

import org.springframework.web.multipart.MultipartFile;
import java.util.Arrays;
import java.util.List;

public class AudioUtils {
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            "aac", "amr", "avi", "flac", "flv", "m4a", "mkv", "mov",
            "mp3", "mp4", "mpeg", "ogg", "opus", "wav", "webm", "wma", "wmv");

    public static boolean isValidAudioFile(MultipartFile file) {
        String extension = getFileExtension(file.getOriginalFilename());
        return ALLOWED_EXTENSIONS.contains(extension.toLowerCase());
    }

    public static String getFileExtension(String filename) {
        if (filename == null)
            return "";
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1)
            return "";
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }
}