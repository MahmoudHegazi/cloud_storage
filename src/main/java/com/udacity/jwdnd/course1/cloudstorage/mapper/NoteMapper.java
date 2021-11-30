package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE note_id = #{note_id}")
    Note getUserNoteBydId(Integer note_id);

    @Select("SELECT * FROM NOTES WHERE user_id= #{user_id}")
    List<Note> getLoggedUserNotes(Integer user_id);

    @Insert("INSERT INTO NOTES (note_title, note_description, user_id ) VALUES(#{note_title}, #{note_description}, #{user_id})")
    @Options(useGeneratedKeys = true, keyProperty = "note_id")
    int insert(Note note);

    @Delete("DELETE FROM NOTES WHERE note_id = #{note_id}")
    void remove(Integer note_id);


    @Update("UPDATE NOTES SET note_title=#{note_title}, note_description=#{note_description} WHERE note_id = #{note_id}")
    @Options(useGeneratedKeys = true, keyProperty = "note_id")
    void update(Note note);
}


