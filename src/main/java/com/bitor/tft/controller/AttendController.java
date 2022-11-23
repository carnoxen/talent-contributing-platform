package com.bitor.tft.controller;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bitor.tft.configuration.TimezoneConfiguration;
import com.bitor.tft.entity.Attendance;
import com.bitor.tft.entity.Curriculum;
import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.Member;
import com.bitor.tft.entity.Schedule;
import com.bitor.tft.service.AttendService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/attend")
@RequiredArgsConstructor
public class AttendController {
    private final AttendService attendService;
    private final TimezoneConfiguration timezoneConfiguration;

    @GetMapping
    String attend(@AuthenticationPrincipal Member you, Model model, @RequestParam Optional<Long> attendance_id) {
        List<Attendance> attendances = attendService.get_all_your_lecture(you);

        if (!attendances.isEmpty()) {
            Attendance att = attendance_id.isPresent() ? attendService.getAttendance(attendance_id.get()).get() : attendances.get(0);
            Lecture attlet = att.getLecture();
            LocalTime _now_time = timezoneConfiguration.getNowLocalTime().withSecond(0).withNano(0);

            Schedule _now_schedule = attendService.getSchedule(attlet, att.getStartDate()).get();

            LocalTime limit = _now_schedule.getStartTime().toLocalTime();
            LocalTime limit_start = limit.minusMinutes(attlet.getAttendLimitStart());
            LocalTime limit_end = limit.plusMinutes(attlet.getAttendLimitEnd());

            if (you.getKind() == 2) {
                Curriculum _now_curriculum = attendService.getCurriculum(attlet, att.getStartDate(), you).get(0);
                
                limit = _now_curriculum.getStartTime().toLocalTime();
                limit_start = limit.minusMinutes(attlet.getAttendLimitStart());
                limit_end = limit.plusMinutes(attlet.getAttendLimitEnd());
            }

            model.addAttribute("attendances", attendances);
            model.addAttribute("attendance", att);

            model.addAttribute("before_time", _now_time.isBefore(limit_start));
            model.addAttribute("on_time", _now_time.equals(limit_start) || _now_time.equals(limit_end) || (_now_time.isAfter(limit_start) && _now_time.isBefore(limit_end)));
            model.addAttribute("after_time", _now_time.isAfter(limit_end));
        }

        model.addAttribute("you", you);
        return "attend";
    }

    @PostMapping
    String post_attend(@RequestParam Long id) {
        attendService.updateTimes(id);
        return "redirect:/attend";
    }

    @PostMapping("/reason")
    String post_reason(@RequestParam Long id, @RequestParam String reason){
        attendService.updateReason(id, reason);
        return "redirect:/attend";
    }
}
