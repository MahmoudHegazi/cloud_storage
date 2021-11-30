package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.Response;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Service
public class NoteService {
    private static NoteMapper noteMapper;
    private static Response response;

    public NoteService(NoteMapper noteMapper){
        NoteService.noteMapper = noteMapper;
    }

    // add new note or return 0 for error
    public List<Note> getAllUserNotes(Integer user_id) throws SQLException {
        return noteMapper.getLoggedUserNotes(user_id);
    }

    // add new note or return 0 for error
    public Integer addNote(Note note, Integer user_id) throws SQLException {
        int newNoteId = noteMapper.insert(note);
        return Math.max(newNoteId, 0);
    }

    // add new note or return 0 for error
    public Note getNoteById(Integer note_id) throws SQLException {
        return noteMapper.getUserNoteBydId(note_id);
    }

    // remove note for user and return response object with message and success status
    public Response removeNote(Integer note_id) throws SQLException {
        noteMapper.remove(note_id);
        Note checkNote = noteMapper.getUserNoteBydId(note_id);
        if (checkNote == null){
            return new Response(true, "Note Removed Successfully");
        } else {
            return new Response(false, "Could Not Remove The Note");
        }

    }


    // remove note for user and return response object with message and success status
    public Response editNote(Note note) throws SQLException {
        // edit
        noteMapper.update(note);
        // confirm edit done professionally
        Note checkNote = noteMapper.getUserNoteBydId(note.getNote_id());
        if (checkNote == null){
            return new Response(false, "error");
        }
        boolean equalTitle = Objects.equals(checkNote.getNote_title(), note.getNote_title());
        boolean equalDesc = Objects.equals(checkNote.getNote_description(), note.getNote_description());
        String errorMsg = "There Are problem in edit valid title edits:" + equalTitle + ", valid description edit:" + equalDesc;
        if (!equalTitle || !equalDesc) {
            return new Response(false, errorMsg);
        } else {
            return new Response(true, "Note Edited Successfully");
        }

    }
}
