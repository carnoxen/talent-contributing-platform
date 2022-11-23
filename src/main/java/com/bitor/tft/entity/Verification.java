package com.bitor.tft.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Comment;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("")
    private Long id;

    @Column(length = 320, unique = true, nullable = false)
    @Comment("")
    private String username;

    @Column(length = 6, nullable = false)
    @Comment("")
    private String code;

    @Builder
    private Verification(String username, String code) {
        this.username = username;
        this.code = code;
    }

    public void updateCode(String code) {
        this.code = code;
    }
}
