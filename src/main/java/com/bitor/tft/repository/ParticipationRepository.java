package com.bitor.tft.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.Member;
import com.bitor.tft.entity.Participation;

public interface ParticipationRepository extends CrudRepository<Participation, Long> {

    Optional<Participation> findByLectureAndMember(Lecture lecture, Member member);

    List<Participation> findAllByLecture(Lecture lecture);

    List<Participation> findAllByLectureAndStatus(Lecture lecture, Byte status);

    @Query("select count(p) from Participation as p where p.lecture = ?1 and p.member.kind = 1 and p.status = 2")
    Long countStudentByLecture(Lecture lecture);

    @Query("select count(p) from Participation as p where p.lecture = ?1 and p.member.kind = 2 and p.status = 2")
    Long countTeacherByLecture(Lecture lecture);

    List<Participation> findAllByMember(Member member);

    List<Participation> findAllByMemberAndStatus(Member member, Byte status);
    
}
