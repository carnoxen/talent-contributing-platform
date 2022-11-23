package com.bitor.tft.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.Schedule;

public interface ScheduleRepository extends CrudRepository<Schedule, Long> {

    List<Schedule> findAllByLecture(Lecture lecture);
    Optional<Schedule> findByLectureAndWeekday(Lecture lecture, Byte weekday);
    
}
