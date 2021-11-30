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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Controller
public class NotesController {

    public final UserService userService;
    public final NoteService noteService;

    public NotesController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    public String createErrorMsg(List<String> errorMessages, StringBuilder message) {
        for (int i = 0; i < errorMessages.size(); i++) {
            if (i < (errorMessages.size() - 1)) {
                message.append(errorMessages.get(i)).append(", Error num: ").append(i).append(": ");
            } else {
                message.append(errorMessages.get(i)).append(".");
            }
        }
        return message.toString();
    }

    public String createSuccessMsg(List<String> successMessages, StringBuilder message) {
        for (int i = 0; i < successMessages.size(); i++) {
            if (i < (successMessages.size() - 1)) {
                message.append(successMessages.get(i)).append(", And ");
            } else {
                message.append(successMessages.get(i)).append(".");
            }
        }
        return message.toString();
    }


    // Create New Note
    @PostMapping("/notes/add")
    public String addNote(@ModelAttribute("NoteFormAdd") NoteFormAdd noteFormAdd, @ModelAttribute("noteFormEdit") NoteFormEdit noteFormEdit, Authentication authentication, Model model) throws SQLException {
        String loggedUserName = authentication.getName();
        User loggedUser = userService.getUser(authentication.getName());
        if (loggedUser == null){
            return "login";
        }
        Integer loggedUserId = loggedUser.getUserId();

        Note newUserNote = new Note(null, noteFormAdd.getNoteTitle(), noteFormAdd.getNoteDescription(), loggedUser.getUserId());
        int newNoteId = noteService.addNote(newUserNote, loggedUser.getUserId());

        if (newNoteId > 0) {
            newUserNote.setNote_id(newNoteId);
            model.addAttribute("success_message", "Successfully Added New Message");
            model.addAttribute("newNote", newUserNote);
        } else {
            model.addAttribute("error_message", "Could not add new Note");
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
        String loggedUserName = authentication.getName();
        User loggedUser = userService.getUser(authentication.getName());
        if (loggedUser == null){
            return "login";
        }
        Integer loggedUserId = loggedUser.getUserId();


        Note getNoteUser = noteService.getNoteById(note_id);

        if (getNoteUser.getUser_id().equals(loggedUser.getUserId())) {
            // apply delete
            Response newDeleteResponse = noteService.removeNote(note_id);
            model.addAttribute("delete_note_status", newDeleteResponse.isSuccess());
            model.addAttribute("logged", loggedUser.getUsername());

            if (newDeleteResponse.isSuccess()) {
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


    @PostMapping("/notes/{note_id}/update")
    public String updateNote(@ModelAttribute("noteFormEdit") NoteFormEdit noteFormEdit, @PathVariable int note_id, Authentication authentication, Model model) throws SQLException {
        String newTitle = noteFormEdit.getNoteEditTitle();
        String newDesc = noteFormEdit.getNoteEditDescription();
        Note noteExist = noteService.getNoteById(note_id);
        String loggedUserName = authentication.getName();
        User loggedUser = userService.getUser(authentication.getName());
        if (loggedUser == null){
            return "login";
        }
        Integer loggedUserId = loggedUser.getUserId();

        if (noteExist == null){
            model.addAttribute("error_message", "Note With Id" + note_id + " not found.");
            return "result";
        }
        if (Objects.equals(noteExist.getUser_id(), loggedUser.getUserId())){
            Note note = new Note(noteExist.getNote_id(), newTitle, newDesc, noteExist.getUser_id());
            noteService.editNote(note);
            model.addAttribute("success_message", "Things Edited");
        } else {
            model.addAttribute("error_message", "You are not the owner of this note you can only edit your owner of this note");
        }
        return "result";

    }
}
