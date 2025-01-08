package com.hgh.tingyue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgh.tingyue.entity.Transcription;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TranscriptionMapper {
    int insert(Transcription transcription);

    Transcription selectById(Long id);

    List<Transcription> selectByAudioFileId(Long audioFileId);

    /**
     * 获取音频文件最新的转写记录
     *
     * @param audioFileId 音频文件ID
     * @return 转写记录
     */
    Transcription selectLatestByAudioFileId(@Param("audioFileId") Long audioFileId);
}