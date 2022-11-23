package com.bitor.tft.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bitor.tft.configuration.TimezoneConfiguration;
import com.bitor.tft.entity.Appraisal;
import com.bitor.tft.entity.Category;
import com.bitor.tft.entity.Curriculum;
import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.LectureCategory;
import com.bitor.tft.entity.Member;
import com.bitor.tft.entity.Notice;
import com.bitor.tft.entity.Participation;
import com.bitor.tft.entity.Schedule;
import com.bitor.tft.form.AppraisalForm;
import com.bitor.tft.repository.AppraisalRepository;
import com.bitor.tft.repository.CurriculumRepository;
import com.bitor.tft.repository.LectureCategoryRepository;
import com.bitor.tft.repository.LectureRepository;
import com.bitor.tft.repository.NoticeRepository;
import com.bitor.tft.repository.ParticipationRepository;
import com.bitor.tft.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final LectureCategoryRepository lectureCategoryRepository;
    private final ScheduleRepository scheduleRepository;
    private final CurriculumRepository curriculumRepository;
    private final AppraisalRepository appraisalRepository;
    private final NoticeRepository noticeRepository;
    private final ParticipationRepository participationRepository;
    private final TimezoneConfiguration timezoneConfiguration;
    
    public Optional<Lecture> lectureOrNot(Long lecture_id) {
        return lectureRepository.findById(lecture_id);
    }

    public List<Schedule> getSchedulesByLecture(Lecture lecture) {
        return scheduleRepository.findAllByLecture(lecture);
    }

    public List<Category> getCategoriesByLecture(Lecture lecture) {
        return lectureCategoryRepository.findAllByLecture(lecture).stream().map(LectureCategory::getCategory).toList();
    }

    public List<Curriculum> getCurriculumsByLecture(Lecture lecture) {
        return curriculumRepository.findAllByLecture(lecture);
    }

    public List<Notice> getNoticesByLecture(Lecture lecture) {
        return noticeRepository.findAllByLecture(lecture);
    }

    public List<Appraisal> getAppraisalsByLecture(Lecture lecture) {
        return appraisalRepository.findAllByLecture(lecture);
    }

    public void saveAppraisal(Member you, Lecture lecture, AppraisalForm appraisalForm) {
        Appraisal newAppraisal = Appraisal.builder()
        .member(you)
        .lecture(lecture)
        .score(appraisalForm.score)
        .reason(appraisalForm.reason).build();
        
        appraisalRepository.save(newAppraisal);
    }

    public void saveParticipation(Lecture lecture, Member member) {
        LocalDateTime _now = timezoneConfiguration.getNowLocalDatetime();
        
        Participation newPar = Participation.builder()
        .member(member)
        .lecture(lecture)
        .startDatetime(Timestamp.valueOf(_now)).build();

        participationRepository.save(newPar);
    }

    //
}
