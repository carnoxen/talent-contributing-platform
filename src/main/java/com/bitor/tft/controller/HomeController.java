package com.bitor.tft.controller;

import java.sql.Date;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bitor.tft.configuration.TimezoneConfiguration;
import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.Member;
import com.bitor.tft.form.LectureForm;
import com.bitor.tft.service.ManageService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class HomeController {
    private final ManageService manageService;
    private final TimezoneConfiguration timezoneConfiguration;

    @GetMapping
    String home(Model model, @AuthenticationPrincipal Member you) {
        model.addAttribute("you", you);
        return "home";
    }

    @GetMapping("/login")
    public String login(@AuthenticationPrincipal Member you) {
        if (you != null) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/notfound")
    String notfound() {
        return "404";
    }

    @GetMapping("/create-lecture") 
    String createLecture(Model model, @AuthenticationPrincipal Member you) {
        LectureForm lectureCreateForm = new LectureForm();
        Date next_day = Date.valueOf(timezoneConfiguration.getNowLocalDate().plusDays(1));

        model.addAttribute("form", lectureCreateForm);
        model.addAttribute("next_day", next_day);

        return "create-lecture";
    }

    @PostMapping("/create-lecture")
    String post_create_lecture(@AuthenticationPrincipal Member you, LectureForm lectureForm) {
        Lecture lecture = lectureForm.toLecture(you);
        lecture = manageService.saveLecture(lecture);
        manageService.saveSchedules(lecture, lectureForm.getWeekdayForms());
        manageService.saveCategories(lecture, lectureForm.getCategories());
        return String.format("redirect:/manage/%d", lecture.getId());
    }
}
