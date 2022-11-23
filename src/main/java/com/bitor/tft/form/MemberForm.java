package com.bitor.tft.form;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
// import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.bitor.tft.entity.Member;
import com.siot.IamportRestClient.response.Certification;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class MemberForm {
    @NotNull(message = "회원가입을 위해 동의하셔야 합니다.")
    @AssertTrue(message = "회원가입을 위해 동의하셔야 합니다.")
    private Boolean approved;

    @NotBlank(message = "이메일 작성 필수")
    @Email(message = "이메일 양식이 아닙니다")
    private String username;

    @AssertTrue(message = "이메일 인증 필수")
    private Boolean verified;

    @NotBlank(message = "비밀번호 작성 필수")
    @Size(min = 5, message = "비밀번호 최소 길이 5자리")
	private String password; // 인코딩된 채로 저장

    @NotBlank(message = "비밀번호 확인 작성 필수")
    private String password_confirm;

    // @NotBlank(message = "전화번호 작성 필수")
    // @Pattern(regexp = "\\d{9,12}", message = "숫자만 작성해주세요")
    // private String phone;

    @NotBlank(message = "")
    private String address;

    @NotBlank(message = "")
    private String addressInternal;

    @NotNull(message = "")
    private Byte kind;

    private String career;

    private String owner;

    @NotBlank(message = "")
    private String uid;

    @NotNull(message = "")
    private Double latitude;
    
    @NotNull(message = "")
    private Double longitude;

    @AssertTrue(message = "비밀번호와 일치해야 합니다.")
    public Boolean passwordConfirmed() {
        return password.equals(password_confirm);
    }

    public Member toMember(PasswordEncoder passwordEncoder, Certification certification) {
        return Member.builder()
            .username(username)
            .password(passwordEncoder.encode(password))
            .name(certification.getName())
            .phone(certification.getPhone())
            .latitude(latitude)
            .longitude(longitude)
            .address(address)
            .addressInternal(addressInternal)
            .kind(kind)
            .career(career)
            .owner(owner).build();
    }

    public static MemberForm from(Member you) {

        MemberForm memberForm = new MemberForm();

        memberForm.username = you.getUsername();
        memberForm.verified = true;
        // memberForm.phone = you.getPhone();
        memberForm.address = you.getAddress();
        memberForm.addressInternal = you.getAddressInternal();
        memberForm.kind = you.getKind();
        memberForm.career = you.getCareer();
        memberForm.owner = you.getOwner();
        memberForm.latitude = you.getLatitude();
        memberForm.longitude = you.getLongitude();
        memberForm.uid = "already";
        
        return memberForm;
    }
}
