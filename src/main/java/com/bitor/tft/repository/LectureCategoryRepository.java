package com.bitor.tft.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bitor.tft.entity.Category;
import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.LectureCategory;

public interface LectureCategoryRepository extends CrudRepository<LectureCategory, Long> {

    List<LectureCategory> findAllByLecture(Lecture lecture);

    Optional<LectureCategory> findByLectureAndCategory(Lecture lecture, Category category);
    
}
