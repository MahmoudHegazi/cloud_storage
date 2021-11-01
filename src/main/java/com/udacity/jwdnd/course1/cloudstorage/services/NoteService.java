package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.Response;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class NoteService {
    private static NoteMapper noteMapper;
    private static Response response;

    public NoteService(NoteMapper noteMapper){
        NoteService.noteMapper = noteMapper;
    }

    // add new note or return 0 for error
    public List<Note>  getAllUserNotes(Integer user_id) throws SQLException {
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
}
