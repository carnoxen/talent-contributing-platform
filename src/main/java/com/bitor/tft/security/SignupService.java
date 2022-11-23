package com.bitor.tft.security;

import java.io.IOException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bitor.tft.configuration.IamportConfiguration;
import com.bitor.tft.entity.Member;
import com.bitor.tft.form.MemberForm;
import com.bitor.tft.repository.MemberRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Certification;
import com.siot.IamportRestClient.response.IamportResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignupService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final IamportConfiguration iamportConfiguration;

    public void registerMember(MemberForm signupForm) throws IamportResponseException, IOException {
        IamportClient client = iamportConfiguration.getClient();
        IamportResponse<Certification> iamportResponse = client.certificationByImpUid(signupForm.getUid());
        Certification certification = iamportResponse.getResponse();
        Member newMember = signupForm.toMember(passwordEncoder, certification);
        
        this.memberRepository.save(newMember);
    }
}
