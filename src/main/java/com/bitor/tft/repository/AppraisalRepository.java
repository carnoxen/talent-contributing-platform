package com.bitor.tft.repository;

import java.util.List;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bitor.tft.entity.Appraisal;
import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.Member;

public interface AppraisalRepository extends CrudRepository<Appraisal, Long> {

    List<Appraisal> findAllByLecture(Lecture lecture);
    @Query("select avg(a.score) from Appraisal as a where a.lecture = ?1")
    public Optional<Double> calculateAverage(Lecture lecture);
    Optional<Appraisal> findByLectureAndMember(Lecture lecture, Member member);
    List<Appraisal> findAllByMember(Member member);
}
