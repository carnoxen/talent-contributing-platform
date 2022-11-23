package com.bitor.tft.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.bitor.tft.entity.Member;

public interface MemberRepository extends CrudRepository<Member, Long> {
    public Optional<Member> findByUsername(String username);

    public Optional<Member> findByPhone(String phone);
}
