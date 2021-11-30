package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileServices;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

@Controller
public class HomeController {

    private static UserService userService;
    private static NoteService noteService;
    private static CredentialService credentialService;
    private static FileServices fileServices;

    public HomeController(UserService userService, NoteService noteService, CredentialService credentialService, FileServices fileServices){
        HomeController.userService = userService;
        HomeController.noteService = noteService;
        HomeController.credentialService = credentialService;
        HomeController.fileServices = fileServices;
    }


    @GetMapping(path = "/downloadFile")
    public ResponseEntity<?> downloadFile (Authentication authentication){



        String loggedUserName = authentication.getName();
        User loggedUser = userService.getUser(authentication.getName());
        if (loggedUser == null){
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-type", "text/html");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).body("<h1>Page Not Found</h1> <p>Back to <a href='/home'>Home Page</a></p>");
        }
        Integer loggedUserId = loggedUser.getUserId();

        FileViewer x = fileServices.getFileById(2);
        ResponseEntity<?> response = null;
        if (x != null){
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-type", x.getContent_type());
            // attachment means it will downloaded inline attachment (inline) means it can be viewed
            headers.set("Content-Disposition","inline; filename=\"nice.jpg\""); // to view in browser change attachment to inline

            response = ResponseEntity.status(HttpStatus.OK).headers(headers).body(x.getFile_data());
        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-type", "text/html");
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).body("<h1>Page Not Found</h1> <p>Back to <a href='/home'>Home Page</a></p>");
        }
        return response;

    }

    @GetMapping("/home")
    public String getHome(@ModelAttribute("noteFormAdd") NoteFormAdd noteFormAdd, @ModelAttribute("noteFormEdit") NoteFormEdit noteFormEdit, @ModelAttribute("CredentialsForm") CredentialsForm credentialsForm, @ModelAttribute("editCredentialModal") CredentialsEditForm credentialsEditForm,  Authentication authentication, Model model) throws SQLException, IOException {



        //System.out.println(x.getFile_name());


        String loggedUserName = authentication.getName();
        User loggedUser = userService.getUser(authentication.getName());
        if (loggedUser == null){
            return "login";
        }
        Integer loggedUserId = loggedUser.getUserId();




        List<Credential> AllUserCredentials = credentialService.getUserCredentials(loggedUserId);


        List<Note>  allUserNotes = noteService.getAllUserNotes(loggedUserId);
        List<String> userFilesNames = fileServices.getUserFilesNames(loggedUserId);
        List<Integer> userFilesIds = fileServices.getAllUserFilesIds(loggedUserId);

        FilesData  AllFilesMainData = fileServices.getAllFilesMainData(loggedUserId);

        if (AllFilesMainData != null){
            model.addAttribute("files", AllFilesMainData);
        }

        if (userFilesNames != null){
            model.addAttribute("nothing_to_do_so_idid_this", userFilesNames);
        }

        if (userFilesIds != null){
            model.addAttribute("user_ids", userFilesIds);
        }

        if (AllUserCredentials != null){
            model.addAttribute("credentials", AllUserCredentials);
        }

        if (allUserNotes != null){
            model.addAttribute("notes", allUserNotes);
        }

        model.addAttribute("logged", loggedUserName);
        return "home";
    }
}
