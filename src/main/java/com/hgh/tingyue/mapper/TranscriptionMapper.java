package com.hgh.tingyue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgh.tingyue.entity.Transcription;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface TranscriptionMapper extends BaseMapper<Transcription> {
    List<Transcription> findByAudioFileId(Long audioFileId);
}