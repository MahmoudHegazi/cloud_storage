package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteFormAdd;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.sql.SQLException;
import java.util.List;

@Controller
public class HomeController {

    private static UserService userService;
    private static NoteService noteService;

    public HomeController(UserService userService, NoteService noteService){
        HomeController.userService = userService;
        HomeController.noteService = noteService;
    }

    @GetMapping("/home")
    public String getHome(@ModelAttribute("noteFormAdd") NoteFormAdd noteFormAdd, Authentication authentication, Model model) throws SQLException {
        String loggedUserName = authentication.getName();
        Integer loggedUserId = userService.getUser(authentication.getName()).getUserId();
        List<Note>  allUserNotes = noteService.getAllUserNotes(loggedUserId);
        model.addAttribute("notes", allUserNotes);
        model.addAttribute("logged", loggedUserName);
        return "home";
    }
}
