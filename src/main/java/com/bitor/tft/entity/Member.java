package com.bitor.tft.entity;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;

import org.hibernate.annotations.Comment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bitor.tft.form.MemberForm;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.siot.IamportRestClient.response.Certification;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@ToString(exclude = {"password"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
public class Member implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("")
    private Long id;

    @Column(length = 320, unique = true, nullable = false)
    @Comment("")
    private String username;

    @JsonIgnore
    @Column(length = 255, nullable = false)
    @Comment("")
	private String password;
    
    @Column(length = 12, nullable = false)
    @Comment("")
    private String phone;

    @Column(length = 120, nullable = false)
    @Comment("")
    private String name;

    @Column(nullable = false)
    @Comment("")
    private Double latitude;

    @Column(nullable = false)
    @Comment("")
    private Double longitude;

    @Column(length = 255, nullable = false)
    @Comment("")
    private String address;

    @Column(length = 255, nullable = false)
    @Comment("")
    private String addressInternal;

    @Column(nullable = false)
    @Comment("")
    private Byte kind;

    @Column(nullable = false)
    @Lob
    @Comment("")
    private String career;
    
    @Column(length = 120, nullable = false)
    @Comment("")
    private String owner;

    public void updateInfo(MemberForm infoForm) {
        this.username = infoForm.getUsername();
        this.latitude = infoForm.getLatitude();
        this.longitude = infoForm.getLongitude();
        this.address = infoForm.getAddress();
        this.addressInternal = infoForm.getAddressInternal();
        this.career = infoForm.getCareer();
        this.owner = infoForm.getOwner();
    }

    public void updatePhoneAndName(Certification certification) {
        this.phone = certification.getPhone();
        this.name = certification.getName();
    }

    public void updatePassword(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
