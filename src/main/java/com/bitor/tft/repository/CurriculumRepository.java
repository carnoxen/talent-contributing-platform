package com.bitor.tft.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bitor.tft.entity.Curriculum;
import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.Member;

public interface CurriculumRepository extends CrudRepository<Curriculum, Long> {

    List<Curriculum> findAllByLecture(Lecture lecture);
    List<Curriculum> findByLectureAndStartDateAndMemberOrderByStartTime(Lecture lecture, Date startDate, Member member);
    
}
