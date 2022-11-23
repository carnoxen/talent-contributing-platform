package com.bitor.tft.controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bitor.tft.configuration.TimezoneConfiguration;
import com.bitor.tft.entity.Attendance;
import com.bitor.tft.entity.Category;
import com.bitor.tft.entity.Curriculum;
import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.Member;
import com.bitor.tft.entity.Notice;
import com.bitor.tft.entity.Participation;
import com.bitor.tft.entity.Schedule;
import com.bitor.tft.form.AttendanceForm;
import com.bitor.tft.form.CurriculumForm;
import com.bitor.tft.form.LectureForm;
import com.bitor.tft.form.NoticeForm;
import com.bitor.tft.service.ManageService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 관리자 페이지 컨트롤러
 */
@Controller
@RequestMapping({ "/manage", "/manage/{lecture_id}" })
@RequiredArgsConstructor
public class ManageController {
    private final ManageService manageService;
    private final TimezoneConfiguration timezoneConfiguration;

    @GetMapping
    public String get_index(@AuthenticationPrincipal Member you, Model model, @PathVariable Optional<Long> lecture_id) {
        List<Lecture> llec = manageService.getLecturesByMember(you);
        if (llec.isEmpty()) {
            return "manage/default";
        }

        if (lecture_id.isEmpty()) {
            return String.format("redirect:/manage/%d", llec.get(0).getId());
        }

        Optional<Lecture> oplec = manageService.lectureOrNot(lecture_id.get());
        Lecture lec = oplec.get();

        model.addAttribute("you", you);
        model.addAttribute("average", manageService.getAverageByLecture(lec).orElse(0d));
        model.addAttribute("scount", manageService.getStudentCountByLecture(lec));
        model.addAttribute("tcount", manageService.getTeacherCountByLecture(lec));
        model.addAttribute("lecture_id", lecture_id.get());
        model.addAttribute("lecture", lec);
        model.addAttribute("lectures", llec);
        
        return"manage/index";
    }

    @GetMapping("/attendance")
    String get_attendance(@AuthenticationPrincipal Member you, Model model, @PathVariable Long lecture_id, @RequestParam Optional<Date> startdate) {
        Optional<Lecture> oplec = manageService.lectureOrNot(lecture_id);
        Lecture lec = oplec.get();
        Date _now = Date.valueOf(timezoneConfiguration.getNowLocalDate());

        List<Lecture> llec = manageService.getLecturesByMember(you);
        Date paramDate = startdate.orElse(_now);
        List<Attendance> lad = manageService.getAttendancesByLectureAndDate(lec, paramDate);

        model.addAttribute("you", you);
        model.addAttribute("attendances", lad);
        model.addAttribute("lecture", lec);
        model.addAttribute("lecture_id", lecture_id);
        model.addAttribute("lectures", llec);
        model.addAttribute("startdate", paramDate);

        return "manage/attendance";
    }
    
    @PostMapping("/attendance")
    String post_attendance(@AuthenticationPrincipal Member you, AttendanceForm attendanceForm){
        Optional<Attendance> opatt = manageService.getAttendanceById(attendanceForm.getId());

        Attendance att = opatt.get();
        att.updateAll(attendanceForm);

        manageService.saveAttendance(att);
        Lecture l = att.getLecture();

        return String.format("redirect:/manage/%d/attendance", l.getId());
    }

    @GetMapping("/curriculum")
    String get_curriculum(@AuthenticationPrincipal Member you, Model model, @PathVariable Long lecture_id) {
        Optional<Lecture> oplec = manageService.lectureOrNot(lecture_id);
        Lecture lec = oplec.get();
        Date next_day = Date.valueOf(timezoneConfiguration.getNowLocalDate().plusDays(1));

        List<Lecture> llec = manageService.getLecturesByMember(you);
        List<Member> lte = manageService.getTeacherByLecture(lec);
        List<Curriculum> lcu = manageService.getCurriculumsByLecture(lec);

        model.addAttribute("you", you);
        model.addAttribute("curriculums", lcu);
        model.addAttribute("teachers", lte);
        model.addAttribute("lecture_id", lecture_id);
        model.addAttribute("lectures", llec);
        model.addAttribute("lecture", lec);
        model.addAttribute("next_day", next_day);

        return "manage/curriculum";
    }
    
    @PostMapping("/curriculum/create")
    String post_curriculum_create(@AuthenticationPrincipal Member you, CurriculumForm curriculumForm, @PathVariable Long lecture_id){
        Curriculum cur = Curriculum.builder()
            .lecture(manageService.lectureOrNot(lecture_id).get())
            .member(you)
            .startdate(curriculumForm.startdate)
            .starttime(curriculumForm.starttime)
            .endtime(curriculumForm.endtime)
            .title(curriculumForm.title).build();

        manageService.saveCurriculum(cur);
        Lecture l = cur.getLecture();

        return String.format("redirect:/manage/%d/curriculum", l.getId());
    }
    
    @PostMapping("/curriculum/remove")
    String post_curriculum_remove(@AuthenticationPrincipal Member you, @RequestParam Long id){
        Optional<Curriculum> opcur = manageService.getCurriculumById(id);
        if (opcur.isEmpty()) {
            return "redirect:/error";
        }

        Curriculum cur = opcur.get();
        Lecture l = cur.getLecture();
        manageService.deleteCurriculum(cur);

        return String.format("redirect:/manage/%d/curriculum", l.getId());
    }
    
    @PostMapping("/curriculum/edit")
    String post_curriculum_edit(@AuthenticationPrincipal Member you, @RequestParam Long id, CurriculumForm curriculumForm){
        Optional<Curriculum> opcur = manageService.getCurriculumById(id);
        if (opcur.isEmpty()) {
            return "redirect:/error";
        }

        Curriculum cur = opcur.get();
        cur.updateByManager(you, curriculumForm);

        manageService.saveCurriculum(cur);
        Lecture l = cur.getLecture();

        return "redirect:/manage/" + l.getId() +"/curriculum";
    }

    @GetMapping("/participation")
    String get_participation(@AuthenticationPrincipal Member you, Model model, @PathVariable Long lecture_id) {
        Optional<Lecture> oplec = manageService.lectureOrNot(lecture_id);
        Lecture lec = oplec.get();

        List<Lecture> llec = manageService.getLecturesByMember(you);
        List<Participation> lpa = manageService.getParticipationsByLecture(lec);

        model.addAttribute("you", you);
        model.addAttribute("participations", lpa);
        model.addAttribute("lecture_id", lecture_id);
        model.addAttribute("lecture", lec);
        model.addAttribute("lectures", llec);

        return "manage/participation";
    }
    
    @PostMapping("/participation")
    String post_participation(@AuthenticationPrincipal Member you, @RequestParam Long id, @RequestParam Byte status){
        Optional<Participation> oppar = manageService.getParticipationById(id);
        if (oppar.isEmpty()) {
            return "redirect:/error";
        }

        manageService.saveParticipation(id, status);
        Lecture l = oppar.get().getLecture();

        return "redirect:/manage/" + l.getId() +"/participation";
    }

    @GetMapping("/notice")
    String get_notice(@AuthenticationPrincipal Member you, Model model, @PathVariable Long lecture_id) {
        Optional<Lecture> oplec = manageService.lectureOrNot(lecture_id);
        Lecture lec = oplec.get();

        List<Lecture> llec = manageService.getLecturesByMember(you);
        List<Notice> lno = manageService.getNoticesByLecture(lec);

        model.addAttribute("you", you);
        model.addAttribute("notices", lno);
        model.addAttribute("lecture_id", lecture_id);
        model.addAttribute("lecture", lec);
        model.addAttribute("lectures", llec);

        return "manage/notice";
    }
    
    @PostMapping("/notice/create")
    String post_notice_create(@AuthenticationPrincipal Member you, NoticeForm noticeForm, @PathVariable Long lecture_id){
        Optional<Lecture> oplet = manageService.lectureOrNot(lecture_id);
        LocalDateTime _now = timezoneConfiguration.getNowLocalDatetime();

        if (oplet.isEmpty()) {
            return "redirect:/error";
        }

        Notice not = Notice.builder()
        .lecture(oplet.get())
        .title(noticeForm.title)
        .description(noticeForm.description)
        .startDatetime(Timestamp.valueOf(_now)).build();

        manageService.saveNotice(not);
        Lecture l = oplet.get();

        return "redirect:/manage/" + l.getId() +"/notice";
    }
    
    @PostMapping("/notice/remove")
    String post_notice_remove(@AuthenticationPrincipal Member you, @RequestParam Long id){
        Optional<Notice> opnot = manageService.getNoticeById(id);
        if (opnot.isEmpty()) {
            return "redirect:/error";
        }

        Notice not = opnot.get();
        Lecture l = not.getLecture();
        manageService.deleteNotice(not);

        return "redirect:/manage/" + l.getId() +"/notice";
    }
    
    @PostMapping("/notice/edit")
    String post_notice(@AuthenticationPrincipal Member you, NoticeForm noticeForm, @RequestParam Long id){
        Optional<Notice> opnot = manageService.getNoticeById(id);
        if (opnot.isEmpty()) {
            return "redirect:/error";
        }

        Notice not = opnot.get();
        not.updateAll(noticeForm);

        manageService.saveNotice(not);
        Lecture l = not.getLecture();

        return "redirect:/manage/" + l.getId() +"/notice";
    }

    // @GetMapping("/create")
    // String get_create_lecture(Model model) {
    //     LectureForm lectureCreateForm = new LectureForm();
    //     model.addAttribute("form", lectureCreateForm);
    //     return "manage/create";
    // }

    // @PostMapping("/create")
    // String post_create_lecture(@AuthenticationPrincipal Member you, LectureForm lectureForm) {
    //     Lecture lecture = lectureForm.toLecture(you);
    //     lecture = manageService.saveLecture(lecture);
    //     manageService.saveSchedules(lecture, lectureForm.getWeekdayForms());
    //     manageService.saveCategories(lecture, lectureForm.getCategories());
    //     return String.format("redirect:/manage/%d", lecture.getId());
    // }

    @GetMapping("/edit")
    String get_edit(@AuthenticationPrincipal Member you, Model model, @PathVariable Long lecture_id) {
        Optional<Lecture> oplec = manageService.lectureOrNot(lecture_id);
        Lecture lec = oplec.get();
        Date next_day = Date.valueOf(timezoneConfiguration.getNowLocalDate().plusDays(1));

        List<Lecture> llec = manageService.getLecturesByMember(you);
        List<Schedule> schedules = manageService.getSchedulesByLecture(lec);
        List<Category> categoryList = manageService.getCategoriesByLecture(lec);
        LectureForm lectureForm = LectureForm.from(lec, schedules, categoryList);

        model.addAttribute("you", you);
        model.addAttribute("form", lectureForm);
        model.addAttribute("lecture_id", lecture_id);
        model.addAttribute("lectures", llec);
        model.addAttribute("lecture", lec);
        model.addAttribute("next_day", next_day);

        return "manage/edit";
    }

    @PostMapping(value="/edit")
    public String post_edit_lecture(LectureForm lectureForm, @RequestParam Long id, @AuthenticationPrincipal Member you) {
        Optional<Lecture> oplet = manageService.lectureOrNot(id);
        if (oplet.isEmpty()) {
            return "redirect:/error";
        }

        Lecture lec = manageService.updateLecture(id, you, lectureForm);
        manageService.updateSchedules(lec, lectureForm.getWeekdayForms());
        manageService.updateCategories(lec, lectureForm.getCategories());

        return String.format("redirect:/lecture/%d", id);
    }
    
}
