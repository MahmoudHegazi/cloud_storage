package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.FileViewer;
import com.udacity.jwdnd.course1.cloudstorage.model.Filea;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileServices;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.*;
import java.sql.Blob;

@Controller
public class FilesController {


    private static com.udacity.jwdnd.course1.cloudstorage.model.Filea file;
    private final UserService userService;
    private final FileServices fileServices;
    private static EntityManager entityManager;


    public FilesController(UserService userService, FileServices fileServices, EntityManager entityManager) {
        this.userService = userService;
        this.fileServices = fileServices;
        FilesController.entityManager = entityManager;
    }

    @GetMapping(path = "/file/view/{file_id}")
    public ResponseEntity<?> viewFile (Authentication authentication, @PathVariable int file_id){
        String loggedUserName = authentication.getName();
        User loggedUser = userService.getUser(authentication.getName());
        if (loggedUser == null){
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-type", "text/html");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).body("<h1>Page Not Found</h1> <p>Back to <a href='/home'>Home Page</a></p>");
        }
        Integer loggedUserId = loggedUser.getUserId();

        FileViewer x = fileServices.getFileById(file_id);
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

    @GetMapping(path = "/file/download/{file_id}")
    public ResponseEntity<?> downloadFile (Authentication authentication, @PathVariable int file_id){
        String loggedUserName = authentication.getName();
        User loggedUser = userService.getUser(authentication.getName());
        if (loggedUser == null){
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-type", "text/html");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).body("<h1>Page Not Found</h1> <p>Back to <a href='/home'>Home Page</a></p>");
        }
        Integer loggedUserId = loggedUser.getUserId();

        FileViewer getMainFileData = fileServices.getFileById(file_id);
        ResponseEntity<?> response = null;
        if (getMainFileData != null){
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-type", getMainFileData.getContent_type());
            // attachment means it will downloaded inline attachment (inline) means it can be viewed
            headers.set("Content-Disposition","attachment; filename=" + getMainFileData.getFile_name()); // to view in browser change attachment to inline
            response = ResponseEntity.status(HttpStatus.OK).headers(headers).body(getMainFileData.getFile_data());
        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-type", "text/html");
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).headers(headers).body("<h1>Page Not Found</h1> <p>Back to <a href='/home'>Home Page</a></p>");
        }
        return response;

    }


    @PostMapping(path = "/file/delete/{file_id}")
    public String removeFile (Authentication authentication, @PathVariable int file_id, Model model){
        String loggedUserName = authentication.getName();
        User loggedUser = userService.getUser(authentication.getName());
        if (loggedUser == null){
            return "login";
        }
        Integer loggedUserId = loggedUser.getUserId();
        FileViewer getMainFileData = fileServices.getFileById(file_id);
        if (getMainFileData == null){
            model.addAttribute("error_message", "file not found");
            return "result";
        }

        boolean remove_file = fileServices.remove(getMainFileData.getFile_id());
        System.out.println(remove_file);
        if (!remove_file) {
            model.addAttribute("error_message", "file could not be removed");
            return "result";
        }

        model.addAttribute("success_message", "file deleted successfully");
        return "result";

    }

    @PostMapping("upload_file")
    public String uploadNewBlobFile(Model model, Authentication authentication, @RequestParam("fileUpload") MultipartFile fileUpload) throws IOException {

        String loggedUserName = authentication.getName();
        User loggedUser = userService.getUser(authentication.getName());
        if (loggedUser == null){
            return "login";
        }
        Integer loggedUserId = loggedUser.getUserId();

        InputStream fis = fileUpload.getInputStream();
        java.io.File staticFolder = new java.io.File("src/main/resources/static/temp/");

        Blob blob =
                Hibernate.getLobCreator(entityManager.unwrap(Session.class))
                        .createBlob(fis, fileUpload.getSize());

        fileServices.add(fileUpload.getOriginalFilename(), fileUpload.getContentType(), String.valueOf(fileUpload.getSize()), loggedUserId, blob);
        model.addAttribute("success_message", "it work fine byte view , blob insert");
        return "result";
    }
}
