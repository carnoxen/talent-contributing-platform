package com.bitor.tft.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.Member;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findAllByMember(Member member);

    List<Lecture> findAllByNameContaining(String query);

    @Query("select distinct l from Lecture as l " + 
    " where l.name like %?1% " + 
    " order by POW(l.latitude - ?2, 2) + POW(l.longitude - ?3, 2)")
    List<Lecture> findAllByNameContainingAndMemberByDistance(String query, Double latitude, Double longitude);
    
}
