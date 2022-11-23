package com.bitor.tft.service;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bitor.tft.entity.Attendance;
import com.bitor.tft.entity.Category;
import com.bitor.tft.entity.Curriculum;
import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.LectureCategory;
import com.bitor.tft.entity.Member;
import com.bitor.tft.entity.Notice;
import com.bitor.tft.entity.Participation;
import com.bitor.tft.entity.Schedule;
import com.bitor.tft.form.LectureForm;
import com.bitor.tft.form.WeekdayForm;
import com.bitor.tft.repository.AppraisalRepository;
import com.bitor.tft.repository.AttendanceRepository;
import com.bitor.tft.repository.CategoryRepository;
import com.bitor.tft.repository.CurriculumRepository;
import com.bitor.tft.repository.LectureCategoryRepository;
import com.bitor.tft.repository.LectureRepository;
import com.bitor.tft.repository.MemberRepository;
import com.bitor.tft.repository.NoticeRepository;
import com.bitor.tft.repository.ParticipationRepository;
import com.bitor.tft.repository.ScheduleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManageService {
    private final LectureRepository lectureRepository;
    private final AttendanceRepository attendanceRepository;
    private final CurriculumRepository curriculumRepository;
    private final NoticeRepository noticeRepository;
    private final ParticipationRepository participationRepository;
    private final MemberRepository memberRepository;

    private final AppraisalRepository appraisalRepository;
    private final ScheduleRepository scheduleRepository;
    private final CategoryRepository categoryRepository;
    private final LectureCategoryRepository lectureCategoryRepository;

    public Optional<Lecture> lectureOrNot(Long lecture_id) {
        return lectureRepository.findById(lecture_id);
    }

    public List<Curriculum> getCurriculumsByLecture(Lecture lec) {
        return curriculumRepository.findAllByLecture(lec);
    }

    public List<Notice> getNoticesByLecture(Lecture lec) {
        return noticeRepository.findAllByLecture(lec);
    }

    public List<Participation> getParticipationsByLecture(Lecture lec) {
        return participationRepository.findAllByLecture(lec);
    }

    public List<Lecture> getLecturesByMember(Member you) {
        if (you.getKind() == 0)
            return lectureRepository.findAllByMember(you);
        else if (you.getKind() == 2)
            return participationRepository.findAllByMemberAndStatus(you, (byte) 2).stream().map(Participation::getLecture).toList();

        return null;
    }

    public List<Attendance> getAttendancesByLectureAndDate(Lecture lec, Date date) {
        return attendanceRepository.findAllByLectureAndStartDate(lec, date);
    }

    public Optional<Double> getAverageByLecture(Lecture lec) {
        return appraisalRepository.calculateAverage(lec);
    }

    public Object getStudentCountByLecture(Lecture lec) {
        return participationRepository.countStudentByLecture(lec);
    }

    public Object getTeacherCountByLecture(Lecture lec) {
        return participationRepository.countTeacherByLecture(lec);
    }

    public Optional<Attendance> getAttendanceById(Long id) {
        return attendanceRepository.findById(id);
    }

    public void saveAttendance(Attendance att) {
        attendanceRepository.save(att);
    }

    public Optional<Curriculum> getCurriculumById(Long id) {
        return curriculumRepository.findById(id);
    }

    public void saveCurriculum(Curriculum cur) {
        curriculumRepository.save(cur);
    }

    public Optional<Member> getMemberById(Long teacher_id) {
        return memberRepository.findById(teacher_id);
    }

    public List<Member> getTeacherByLecture(Lecture lec) {
        return participationRepository
        .findAllByLectureAndStatus(lec, (byte) 2)
        .stream().map(Participation::getMember)
        .filter(x -> x.getKind() == (byte) 2).toList();
    }

    public Optional<Participation> getParticipationById(Long id) {
        return participationRepository.findById(id);
    }

    public void saveParticipation(Long id, Byte status) {
        Participation participation = participationRepository.findById(id).get();
        participation.updateStatus(status);
        participationRepository.save(participation);
    }

    public void deleteCurriculum(Curriculum curriculum) {
        curriculumRepository.delete(curriculum);
    }

    public Optional<Notice> getNoticeById(Long id) {
        return noticeRepository.findById(id);
    }

    public void deleteNotice(Notice notice) {
        noticeRepository.delete(notice);
    }

    public void saveNotice(Notice notice) {
        noticeRepository.save(notice);
    }

    public Lecture saveLecture(Lecture lecture) {
        return lectureRepository.save(lecture);
    }

    public List<Schedule> getSchedulesByLecture(Lecture lec) {
        return scheduleRepository.findAllByLecture(lec);
    }

    public void saveSchedules(Lecture lecture, List<WeekdayForm> weekdayForms) {
        List<WeekdayForm> filtered = weekdayForms.stream().filter(WeekdayForm::isValid).toList();

        for (WeekdayForm weekdayForm : filtered) {
            Schedule schedule = Schedule.builder()
            .lecture(lecture)
            .weekday(weekdayForm.getWeekday())
            .starttime(weekdayForm.getStarttime())
            .endtime(weekdayForm.getEndtime())
            .build();

            scheduleRepository.save(schedule);
        }
    }

    public void saveCategories(Lecture lecture, String categories) {
        String[] categoryList = categories.split(",");
        for (String catString : categoryList) {
            if (categoryRepository.findByName(catString).isEmpty()) {
                categoryRepository.save(Category.builder().name(catString).build());
            }

            Category category = categoryRepository.findByName(catString).get();
            
            if (lectureCategoryRepository.findByLectureAndCategory(lecture, category).isEmpty()) {
                lectureCategoryRepository.save(LectureCategory.builder().lecture(lecture).category(category).build());
            }
        }
    }

    public Lecture updateLecture(Long id, Member member, LectureForm lectureForm) {
        Lecture lecture = lectureForm.toLecture(member);
        lecture.updateId(id);
        return lectureRepository.save(lecture);
    }

    public void updateSchedules(Lecture lecture, List<WeekdayForm> weekdayForms) {
        for (WeekdayForm weekdayForm : weekdayForms) {
            Optional<Schedule> opsce = scheduleRepository.findByLectureAndWeekday(lecture, weekdayForm.getWeekday());

            if (weekdayForm.isValid() && opsce.isEmpty()){
                Schedule schedule = Schedule.builder()
                .lecture(lecture)
                .weekday(weekdayForm.getWeekday())
                .starttime(weekdayForm.getStarttime())
                .endtime(weekdayForm.getEndtime())
                .build();
                
                scheduleRepository.save(schedule);
            }
            else if (weekdayForm.isValid() && opsce.isPresent()) {
                Schedule schedule = opsce.get();
                schedule.updateAll(weekdayForm);
                scheduleRepository.save(schedule);
            }
            else if (!weekdayForm.isValid() && opsce.isPresent()) {
                scheduleRepository.delete(opsce.get());
            }
        }
    }

    public void updateCategories(Lecture lecture, String categories) {
        String[] categoryList = categories.split(",");
        List<LectureCategory> lectureCategories = lectureCategoryRepository.findAllByLecture(lecture);
        
        for (LectureCategory lectureCategory : lectureCategories) {
            if (!Arrays.asList(categoryList).contains(lectureCategory.getCategory().getName())) {
                lectureCategoryRepository.delete(lectureCategory);
            }
        }
        for (String catString : categoryList) {
            Optional<Category> opcat = categoryRepository.findByName(catString);
            if (opcat.isEmpty()) {
                categoryRepository.save(Category.builder().name(catString).build());
            }

            Category category = categoryRepository.findByName(catString).get();
            
            if (lectureCategoryRepository.findByLectureAndCategory(lecture, category).isEmpty()) {
                lectureCategoryRepository.save(LectureCategory.builder().lecture(lecture).category(category).build());
            }
        }
    }

    public List<Category> getCategoriesByLecture(Lecture lecture) {
        return lectureCategoryRepository.findAllByLecture(lecture).stream().map(LectureCategory::getCategory).toList();
    }
}
