package com.bitor.tft.security;

import java.io.IOException;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bitor.tft.entity.Member;
import com.bitor.tft.form.MemberForm;
import com.siot.IamportRestClient.exception.IamportResponseException;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignupController {
    private final SignupService signupService;

    @GetMapping
    public String get_signup(@AuthenticationPrincipal Member you, MemberForm signupForm, Model model) {
        if (you != null) {
            return "redirect:/my";
        }
        
        model.addAttribute("form", signupForm);
        return "signup";
    }

    @PostMapping
    public String post_signup(@Validated MemberForm signupForm, BindingResult bindingResult, Model model) throws IamportResponseException, IOException {
        if (!signupForm.passwordConfirmed() || bindingResult.hasErrors()) {
            model.addAttribute("form", signupForm);
            return "signup";
        }

        this.signupService.registerMember(signupForm);

        return "redirect:/login";
    }
}
