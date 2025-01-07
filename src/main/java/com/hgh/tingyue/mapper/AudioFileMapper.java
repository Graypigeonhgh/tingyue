package com.hgh.tingyue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgh.tingyue.entity.AudioFile;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AudioFileMapper extends BaseMapper<AudioFile> {
    List<AudioFile> findByUserId(Long userId);
}