package com.bitor.tft.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bitor.tft.entity.Participation;
import com.bitor.tft.form.MemberForm;
import com.bitor.tft.configuration.IamportConfiguration;
import com.bitor.tft.entity.Appraisal;
import com.bitor.tft.entity.Attendance;
import com.bitor.tft.entity.Curriculum;
import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.Member;
import com.bitor.tft.repository.AppraisalRepository;
import com.bitor.tft.repository.AttendanceRepository;
import com.bitor.tft.repository.CurriculumRepository;
import com.bitor.tft.repository.LectureRepository;
import com.bitor.tft.repository.MemberRepository;
import com.bitor.tft.repository.ParticipationRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Certification;
import com.siot.IamportRestClient.response.IamportResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyService {
    private final ParticipationRepository participationRepository;
    private final LectureRepository lectureRepository;
    private final AppraisalRepository appraisalRepository;
    private final AttendanceRepository attendanceRepository;
    private final CurriculumRepository curriculumRepository;
    private final MemberRepository memberRepository;
    private final IamportConfiguration iamportConfiguration;

    // private final String api_key = "2822307326154236";
    // private final String api_secret = "9ksb5gBPWgL8xNp4KtmIsxXTxehpFzs7VHveTCMrYfONy8jKErHchF53L5kCB8OphDatYUOqL6q0L7dP";
    // private IamportClient client = new IamportClient(api_key, api_secret);

    public Optional<Lecture> lectureOrNot(Long id) {
        return lectureRepository.findById(id);
    }

    public List<Lecture> getLecturesOnParticipationByMemberAndStatus(Member member, Byte status) {
        return participationRepository
                .findAllByMemberAndStatus(member, status)
                .stream()
                .map(Participation::getLecture)
                .collect(Collectors.toList());
    }

    public List<Appraisal> getAppraisalsByMember(Member member) {
        return appraisalRepository.findAllByMember(member);
    }

    public List<Participation> getParticipationsByMember(Member member) {
        return participationRepository.findAllByMember(member);
    }

    public List<Attendance> getAttendancesByMemberAndLecture(Member member, Lecture lecture) {
        return attendanceRepository.findAllByMemberAndLecture(member, lecture);
    }

    public List<Curriculum> getCurriculumsByLecture(Lecture lecture) {
        return curriculumRepository.findAllByLecture(lecture);
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

    public void deleteCurriculum(Curriculum cur) {
        curriculumRepository.delete(cur);
    }

    public void saveCurriculum(Curriculum cur) {
        curriculumRepository.save(cur);
    }

    public Optional<Appraisal> getAppraisalById(Long id) {
        return appraisalRepository.findById(id);
    }

    public void deleteAppraisal(Appraisal app) {
        appraisalRepository.delete(app);
    }

    public void saveAppraisal(Appraisal app) {
        appraisalRepository.save(app);
    }

    // public String getNameByUid(String uid) throws IamportResponseException, IOException {
    //     IamportResponse<Certification> iamportResponse = client.certificationByImpUid(uid);
    //     Certification certification = iamportResponse.getResponse();
    //     return certification.getName();
    // }

    public void updateYourInfo(Member member, MemberForm memberForm) {
        member.updateInfo(memberForm);
        memberRepository.save(member);
    }

    public void updateYourPhoneAndName(Member member, String uid) throws IamportResponseException, IOException {
        IamportClient client = iamportConfiguration.getClient();
        IamportResponse<Certification> iamportResponse = client.certificationByImpUid(uid);
        Certification certification = iamportResponse.getResponse();
        
        member.updatePhoneAndName(certification);
        memberRepository.save(member);
    }

}
