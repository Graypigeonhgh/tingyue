package com.hgh.tingyue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgh.tingyue.entity.AudioFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AudioFileMapper extends BaseMapper<AudioFile> {
    /**
     * 插入音频文件记录
     *
     * @param audioFile 音频文件信息
     * @return 影响的行数
     */
    int insert(AudioFile audioFile);

    /**
     * 根据ID查询音频文件
     *
     * @param id 文件ID
     * @return 音频文件信息
     */
    AudioFile selectById(@Param("id") Long id);

    /**
     * 查询用户的音频文件列表
     *
     * @param userId 用户ID
     * @return 音频文件列表
     */
    List<AudioFile> selectByUserId(@Param("userId") Long userId);
}