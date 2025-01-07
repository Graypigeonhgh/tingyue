package com.hgh.tingyue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgh.tingyue.entity.Transcription;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface TranscriptionMapper {
    int insert(Transcription transcription);

    Transcription selectById(Long id);

    List<Transcription> selectByAudioFileId(Long audioFileId);
}