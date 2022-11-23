package com.bitor.tft.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.Notice;

public interface NoticeRepository extends CrudRepository<Notice, Long> {

    List<Notice> findAllByLecture(Lecture lecture);
    
}
