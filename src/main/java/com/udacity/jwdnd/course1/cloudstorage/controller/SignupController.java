package com.udacity.jwdnd.course1.cloudstorage.controller;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

import static com.udacity.jwdnd.course1.cloudstorage.CloudStorageApplication.SignupTokens;

@Controller()
@RequestMapping("/signup")
public class SignupController {
    private final UserService userService;
    private String signupToken = UUID.randomUUID().toString().toLowerCase();
    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String signupView() {
        return "signup";
    }

    @PostMapping
    public Object signupUser(@ModelAttribute User user, Model model) {
        String signupError = null;

        if (!userService.isUsernameAvailable(user.getUsername())) {
            signupError = "The username already exists.";
        }

        if (signupError == null) {
            int rowsAdded = userService.createUser(user);
            if (rowsAdded < 0) {
                signupError = "There was an error signing you up. Please try again.";
            }
        }

        if (signupError == null) {
            RedirectView redirectView = new RedirectView();
            setSignupToken(UUID.randomUUID().toString().toLowerCase());
            String newUserToken = getSignupToken();
            SignupTokens.add(newUserToken);
            redirectView.setUrl("/login?signup_token="+newUserToken);
            return redirectView;
        } else {
            model.addAttribute("signupError", signupError);
        }
        return "signup";
    }

    public String getSignupToken() {
        return signupToken;
    }

    public void setSignupToken(String signupToken) {
        this.signupToken = signupToken;
    }
}
