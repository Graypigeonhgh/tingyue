package com.hgh.tingyue.model;

import lombok.Data;
import java.util.List;

@Data
public class TranscriptionResult {
    private String fileUrl;
    private AudioProperties properties;
    private List<Transcript> transcripts;

    @Data
    public static class AudioProperties {
        private String audioFormat;
        private List<Integer> channels;
        private Integer originalSamplingRate;
        private Integer originalDurationInMilliseconds;
    }

    @Data
    public static class Transcript {
        private Integer channelId;
        private Integer contentDurationInMilliseconds;
        private String text;
        private List<Sentence> sentences;
    }

    @Data
    public static class Sentence {
        private Integer beginTime;
        private Integer endTime;
        private String text;
    }
}