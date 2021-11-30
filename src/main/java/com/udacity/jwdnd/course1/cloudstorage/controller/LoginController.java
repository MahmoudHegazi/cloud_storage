package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.hibernate.query.QueryParameter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

import static com.udacity.jwdnd.course1.cloudstorage.CloudStorageApplication.FinalSignupTokens;
import static com.udacity.jwdnd.course1.cloudstorage.CloudStorageApplication.SignupTokens;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String loginView(Authentication authentication, Model model, @RequestParam(required = false, name="signup_token") String signup_token)  {

        //  this way securely show the success message one time only and without use of database or space
        // plus it works very effective some famous website not prefers deal with it and repeat the message but now success
        // signup message will be rendered one time only and can not auto render check signup and main
        if (signup_token != null && SignupTokens.contains(signup_token)){
            System.out.println(SignupTokens);
            model.addAttribute("success_signup_message", "You successfully signed up!");
            // this solution for space and make server always fast now I do not need to save the token any more full (one time process)
            SignupTokens.remove(signup_token);
        }

        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("logged", authentication.getName());
        } else {
            model.addAttribute("logged", false);
        }
        return "login";
    }


}
