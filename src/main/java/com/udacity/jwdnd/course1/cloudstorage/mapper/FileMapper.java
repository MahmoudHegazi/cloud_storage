package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import org.apache.ibatis.annotations.*;

import java.sql.Blob;
import java.util.List;


@Mapper
public interface FileMapper {
    @Select("SELECT file_id, file_name, content_type, file_size, user_id, file_data FROM FILES WHERE file_id = #{file_id} LIMIT 1")
    FileViewer getFileById(Integer file_id);


    @Insert("INSERT INTO FILES (file_name, content_type, file_size, user_id, file_data) VALUES(#{file_name}, #{content_type}, #{file_size}, #{user_id}, #{file_data})")
    void insert(String file_name, String content_type, String file_size, Integer user_id, Blob file_data);


    @Delete("DELETE FROM FILES WHERE file_id = #{file_id}")
    void delete(Integer file_id);


    @Select("SELECT file_data FROM FILES WHERE user_id=#{user_id}")
    List<byte[]> getUserFilesData(Integer user_id);

    @Select("SELECT content_type FROM FILES WHERE user_id = #{user_id}")
    List<String> getFilesDataContentType(Integer user_id);


    @Select("SELECT file_name FROM FILES WHERE user_id = #{user_id}")
    List<String> getFilesDataNames(Integer user_id);

    @Select("SELECT user_id FROM FILES WHERE user_id = #{user_id}")
    List<Integer> getFilesDataUserId(Integer user_id);

    @Select("SELECT file_id FROM FILES WHERE user_id = #{user_id}")
    List<Integer> getFilesIds(Integer user_id);
}
