package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.SQLException;
import java.util.List;


@Controller
public class NotesController {

    private static UserService userService;
    private static NoteService noteService;

    public NotesController(UserService userService, NoteService noteService){
        NotesController.userService = userService;
        NotesController.noteService = noteService;
    }

    // Create New Note
    @PostMapping("/notes/add")
    public String addNote(@ModelAttribute("NoteFormAdd") NoteFormAdd noteFormAdd, Authentication authentication, Model model) throws SQLException {
        User loggedUser = userService.getUser(authentication.getName());
        Note newUserNote = new Note(null,noteFormAdd.getNoteTitle(),noteFormAdd.getNoteDescription(),loggedUser.getUserId());
        int newNoteId = noteService.addNote(newUserNote, loggedUser.getUserId());

        if (newNoteId > 0) {
            newUserNote.setNote_id(newNoteId);
            model.addAttribute("success_message","Successfully Added New Message");
            model.addAttribute("newNote",newUserNote);
        } else {
            model.addAttribute("error_message","Could not add new Note");
        }

        List<Note> allUserNotes = noteService.getAllUserNotes(loggedUser.getUserId());
        model.addAttribute("notes", allUserNotes);
        model.addAttribute("logged", loggedUser.getUsername());
        model.addAttribute("add_note_status", newNoteId > 0);
        model.addAttribute("logged", loggedUser.getUserId());
        return "result";

    }

    // delete existing Note
    @PostMapping("/notes/{note_id}/delete")
    public String deleteNote(@PathVariable int note_id, Authentication authentication, Model model) throws SQLException {
        User loggedUser = userService.getUser(authentication.getName());
        Note getNoteUser = noteService.getNoteById(note_id);
        System.out.println(note_id);


        if (getNoteUser.getUser_id().equals(loggedUser.getUserId())){
            // apply delete
            Response newDeleteResponse = noteService.removeNote(note_id);
            model.addAttribute("delete_note_status", newDeleteResponse.isSuccess());
            model.addAttribute("logged", loggedUser.getUsername());

            if (newDeleteResponse.isSuccess()){
                model.addAttribute("success_message", newDeleteResponse.message);

            } else {
                model.addAttribute("error_message", newDeleteResponse.message);
            }

            System.out.println(newDeleteResponse.message);
            System.out.println(newDeleteResponse.isSuccess());
        } else {
            model.addAttribute("success_message", "You have No Permission to delete this note");
            model.addAttribute("delete_note_status", false);
        }


        return "result";

    }

    // update Note
    @PostMapping("/notes/{id}/update")
    public String updateNote(Authentication authentication, Model model){
        return "result";
    }
}
