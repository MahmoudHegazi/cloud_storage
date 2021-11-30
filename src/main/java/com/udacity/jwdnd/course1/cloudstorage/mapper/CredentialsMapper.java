package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialsMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE user_id= #{user_id}")
    List<Credential> getUserCredentials(Integer user_id);

    @Select("SELECT * FROM CREDENTIALS WHERE credentials_id = #{credentials_id}")
    Credential getUserCredential(Integer credentials_id, Integer user_id);

    @Insert("INSERT INTO CREDENTIALS (uname, url, secret_key, password, user_id) VALUES(#{uname}, #{url}, #{secret_key}, #{password}, #{user_id});")
    @Options(useGeneratedKeys = true, keyProperty = "credentials_id")
    void insert(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentials_id = #{credentials_id}")
    void remove(Integer credentials_id);


    @Update("UPDATE CREDENTIALS SET uname=#{uname}, url=#{url}, secret_key=#{secret_key}, password=#{password}, user_id=#{user_id} WHERE credentials_id = #{credentials_id}")
    @Options(useGeneratedKeys = true, keyProperty = "credentials_id")
    void update(Credential credential);
}
