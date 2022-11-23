package com.bitor.tft.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bitor.tft.entity.Attendance;
import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.Member;

public interface AttendanceRepository extends CrudRepository<Attendance, Long> {
    List<Attendance> findAllByMemberAndStartDate(Member member, Date startDate);

    List<Attendance> findAllByLectureAndStartDate(Lecture lecture, Date startDate);

    List<Attendance> findAllByMemberAndLecture(Member member, Lecture lecture);
}
