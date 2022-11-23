package com.bitor.tft.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bitor.tft.entity.Appraisal;
import com.bitor.tft.entity.Category;
import com.bitor.tft.entity.Curriculum;
import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.Member;
import com.bitor.tft.entity.Notice;
import com.bitor.tft.entity.Schedule;
import com.bitor.tft.form.AppraisalForm;
import com.bitor.tft.service.LectureService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/lecture/{lecture_id}")
@RequiredArgsConstructor
public class LectureController {
    private final LectureService lectureService;
    
    @GetMapping({"", "/{sub}"})
    String get_common(@PathVariable Optional<Long> lecture_id, @PathVariable Optional<String> sub, Model model, @AuthenticationPrincipal Member you) {
        Optional<Lecture> oplec = lectureService.lectureOrNot(lecture_id.get());

        Lecture lec = oplec.get();
        List<Schedule> lsc = lectureService.getSchedulesByLecture(lec);
        List<Category> lca = lectureService.getCategoriesByLecture(lec);

        if (sub.isPresent()) {
            String subs = sub.get();
            if (subs.equals("appraisal")) {
                List<Appraisal> lap = lectureService.getAppraisalsByLecture(lec);
                model.addAttribute("appraisals", lap);
            }
            else if (subs.equals("curriculum")) {
                List<Curriculum> lcu = lectureService.getCurriculumsByLecture(lec);
                model.addAttribute("curriculums", lcu);
            }
            else if (subs.equals("notice")) {
                List<Notice> lno = lectureService.getNoticesByLecture(lec);
                model.addAttribute("notices", lno);
            }
        }

        model.addAttribute("you", you);
        model.addAttribute("lecture", lec);
        model.addAttribute("lecture_id", lecture_id.get());
        model.addAttribute("schedules", lsc);
        model.addAttribute("categories", lca);

        return String.format("lecture/%s", sub.orElse("index"));
    }

    @PostMapping("/appraisal") 
    String post_appraisal(AppraisalForm appraisalForm, @AuthenticationPrincipal Member you, @PathVariable Long lecture_id) {
        Optional<Lecture> oplec = lectureService.lectureOrNot(lecture_id);
        
        lectureService.saveAppraisal(you, oplec.get(), appraisalForm);
        return "redirect:/my/appraisal";
    }

    @PostMapping("/participate")
    String post_participate_create(@AuthenticationPrincipal Member you, @PathVariable Long lecture_id) {
        Lecture lec = lectureService.lectureOrNot(lecture_id).get();
        lectureService.saveParticipation(lec, you);
        return "redirect:/my/participation";
    }
}
