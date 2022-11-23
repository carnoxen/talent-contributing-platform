package com.bitor.tft.service;

import java.sql.Date;
// import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bitor.tft.entity.Member;
import com.bitor.tft.entity.Schedule;
import com.bitor.tft.configuration.TimezoneConfiguration;
import com.bitor.tft.entity.Attendance;
import com.bitor.tft.entity.Curriculum;
import com.bitor.tft.entity.Lecture;
import com.bitor.tft.repository.AttendanceRepository;
import com.bitor.tft.repository.CurriculumRepository;
import com.bitor.tft.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendService {
    private final AttendanceRepository attendanceRepository;
    private final CurriculumRepository curriculumRepository;
    private final TimezoneConfiguration timezoneConfiguration;
    private final ScheduleRepository scheduleRepository;

    public List<Attendance> get_all_your_lecture(Member member) {
        Date _now = Date.valueOf(timezoneConfiguration.getNowLocalDate());
        return attendanceRepository.findAllByMemberAndStartDate(member, _now);
    }

    public Optional<Attendance> getAttendance(Long id) {
        return attendanceRepository.findById(id);
    }

    public void updateTimes(Long id) {
        Optional<Attendance> a = attendanceRepository.findById(id);
        if (a.isPresent()) {
            Attendance ap = a.get();
            ap.updateTime(timezoneConfiguration.getNowLocalTime());

            attendanceRepository.save(ap);
        } 
    }

    public void updateReason(Long id, String reason) {
        Optional<Attendance> a = attendanceRepository.findById(id);
        if (a.isPresent()) {
            Attendance ap = a.get();
            ap.updateReason(reason);

            attendanceRepository.save(ap);
        } 
    }

    public Optional<Schedule> getSchedule(Lecture lecture, Date date) {
        // DayOfWeek _now_weekday = date.toLocalDate().getDayOfWeek();
        Calendar calendar = Calendar.getInstance(timezoneConfiguration.getTimeZone());
        calendar.setTime(date);
        return scheduleRepository.findByLectureAndWeekday(lecture, (byte) calendar.get(Calendar.DAY_OF_WEEK));
    }

    public List<Curriculum> getCurriculum(Lecture lecture, Date date, Member member) {
        return curriculumRepository.findByLectureAndStartDateAndMemberOrderByStartTime(lecture, date, member);
    }
}
