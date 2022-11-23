package com.bitor.tft.search;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.Member;
import com.bitor.tft.repository.LectureRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final LectureRepository lectureRepository;

    public List<Lecture> getLecturesByKeyword(String query) {
        return lectureRepository.findAllByNameContaining(query);
    }

    public List<Lecture> getLecturesByKeywordAndDistance(String query, Member member) {
        return lectureRepository.findAllByNameContainingAndMemberByDistance(query, member.getLatitude(), member.getLongitude());
    }

}
