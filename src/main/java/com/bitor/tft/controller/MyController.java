package com.bitor.tft.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bitor.tft.entity.Appraisal;
import com.bitor.tft.entity.Attendance;
// import com.bitor.tft.entity.Curriculum;
import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.Member;
import com.bitor.tft.entity.Participation;
import com.bitor.tft.form.AppraisalForm;
import com.bitor.tft.form.AttendanceForm;
import com.bitor.tft.form.MemberForm;
import com.bitor.tft.form.PasswordForm;
import com.bitor.tft.service.MyService;
import com.siot.IamportRestClient.exception.IamportResponseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyController {
    private final MyService myService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String getMy(@AuthenticationPrincipal Member you, Model model) {
        log.info("go to my index");
        model.addAttribute("you", you);

        return "my/index";
    }

    @GetMapping("/attendance")
    String get_attendance(@AuthenticationPrincipal Member you, Model model, @RequestParam Optional<Long> lecture_id) {
        log.info("get my attendances");
        List<Lecture> participatedLectures = myService.getLecturesOnParticipationByMemberAndStatus(you, (byte) 2);

        if (!participatedLectures.isEmpty()) {
            log.info("my attendances exists");
            Lecture lec = lecture_id.isPresent() ? myService.lectureOrNot(lecture_id.get()).get()
                    : participatedLectures.get(0);
            List<Attendance> lat = myService.getAttendancesByMemberAndLecture(you, lec);

            model.addAttribute("attendances", lat);
            model.addAttribute("lecture_id", lecture_id.orElse(participatedLectures.get(0).getId()));
            model.addAttribute("lectures", participatedLectures);
        }

        model.addAttribute("you", you);

        return "my/attendance";
    }

    @PostMapping("/attendance")
    String post_attendance(@AuthenticationPrincipal Member you, Model model, @Valid AttendanceForm attendanceForm,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/notfound";
        }
        Optional<Attendance> opatt = myService.getAttendanceById(attendanceForm.getId());

        Attendance att = opatt.get();
        att.updateReason(attendanceForm.reason);

        myService.saveAttendance(att);
        Lecture l = att.getLecture();

        return String.format("redirect:/my/attendance?lecture_id=%d", l.getId());
    }

    // @GetMapping("/curriculum")
    // String get_curriculum(@AuthenticationPrincipal Member you, Model model,
    // @RequestParam Optional<Long> lecture_id) {
    // List<Lecture> participatedLectures =
    // myService.getLecturesOnParticipationByMemberAndStatus(you, (byte) 2);

    // if (!participatedLectures.isEmpty()){
    // Lecture lec = lecture_id.isPresent() ?
    // myService.lectureOrNot(lecture_id.get()).get() : participatedLectures.get(0);
    // List<Curriculum> lcu = myService.getCurriculumsByLecture(lec);

    // model.addAttribute("curriculums", lcu);
    // model.addAttribute("lecture_id",
    // lecture_id.orElse(participatedLectures.get(0).getId()));
    // model.addAttribute("lectures", participatedLectures);
    // }

    // model.addAttribute("you", you);

    // return "my/curriculum";
    // }

    // @PostMapping("/curriculum/create")
    // String post_curriculum_create(@AuthenticationPrincipal Member you,
    // CurriculumForm curriculumForm){
    // Curriculum cur = Curriculum.builder()
    // .lecture(myService.lectureOrNot(curriculumForm.lecture_id).get())
    // .member(you)
    // .startdate(curriculumForm.startdate)
    // .starttime(curriculumForm.starttime)
    // .endtime(curriculumForm.endtime)
    // .title(curriculumForm.title).build();

    // myService.saveCurriculum(cur);
    // Lecture l = cur.getLecture();

    // return String.format("redirect:/my/curriculum?lecture_id=%d", l.getId());
    // }

    // @PostMapping("/curriculum/remove")
    // String post_curriculum_remove(@AuthenticationPrincipal Member you,
    // @RequestParam Optional<Long> id){
    // Optional<Curriculum> opcur = myService.getCurriculumById(id.get());

    // Curriculum cur = opcur.get();
    // Lecture l = cur.getLecture();
    // myService.deleteCurriculum(cur);

    // return String.format("redirect:/my/curriculum?lecture_id=%d", l.getId());
    // }

    // @PostMapping("/curriculum/edit")
    // String post_curriculum_edit(@AuthenticationPrincipal Member you,
    // CurriculumByOwnerForm curriculumForm){
    // Optional<Curriculum> opcur =
    // myService.getCurriculumById(curriculumForm.getId());

    // Curriculum cur = opcur.get();
    // cur.updateByOwner(curriculumForm);
    // myService.saveCurriculum(cur);
    // Lecture l = cur.getLecture();

    // return String.format("redirect:/my/curriculum?lecture_id=%d", l.getId());
    // }

    @GetMapping("/appraisal")
    String get_appraisal(@AuthenticationPrincipal Member you, Model model) {
        List<Appraisal> lap = myService.getAppraisalsByMember(you);

        model.addAttribute("you", you);
        model.addAttribute("appraisals", lap);
        return "my/appraisal";
    }

    @PostMapping("/appraisal/remove")
    String post_appraisal_remove(@AuthenticationPrincipal Member you, @RequestParam Long id) {
        Optional<Appraisal> opapp = myService.getAppraisalById(id);

        Appraisal app = opapp.get();
        myService.deleteAppraisal(app);

        return "redirect:/my/appraisal";
    }

    @PostMapping("/appraisal/edit")
    String post_appraisal_edit(@AuthenticationPrincipal Member you, AppraisalForm appraisalForm,
            @RequestParam Long id) {
        Optional<Appraisal> opapp = myService.getAppraisalById(id);

        Appraisal app = opapp.get();
        app.updateAll(appraisalForm);

        myService.saveAppraisal(app);

        return "redirect:/my/appraisal";
    }

    @GetMapping("/participation")
    String get_participant(@AuthenticationPrincipal Member you, Model model) {
        List<Participation> lpa = myService.getParticipationsByMember(you);

        model.addAttribute("you", you);
        model.addAttribute("participations", lpa);

        return "my/participation";
    }

    @GetMapping("/password")
    String get_password(@AuthenticationPrincipal Member you, Model model) {
        model.addAttribute("you", you);
        return "my/password";
    }

    @PostMapping("/password")
    String post_password(@AuthenticationPrincipal Member you, PasswordForm passwordForm) {
        if (!passwordForm.old_is_correct(you, passwordEncoder)) {
        }

        if (!passwordForm.is_same()) {
        }

        you.updatePassword(passwordForm.password, passwordEncoder);

        return "redirect:/my/password";
    }

    @GetMapping("/info")
    String get_info(@AuthenticationPrincipal Member you, Model model) {
        model.addAttribute("you", you);
        model.addAttribute("form", MemberForm.from(you));
        model.addAttribute("member_id", you.getId());

        return "my/info";
    }

    @PostMapping("/info")
    String post_info(@AuthenticationPrincipal Member you, MemberForm infoForm, BindingResult bindingResult, Model model)
            throws IamportResponseException, IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("form", infoForm);
            return "my/info";
        }

        myService.updateYourInfo(you, infoForm);
        
        String uid = infoForm.getUid();
        if (!uid.equals("already")) {
            myService.updateYourPhoneAndName(you, uid);
            // you.updatePhoneAndName(uid);
        }

        return "redirect:/my/info";
    }
}
