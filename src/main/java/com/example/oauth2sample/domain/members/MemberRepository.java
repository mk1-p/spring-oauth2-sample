package com.example.oauth2sample.domain.members;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAttributeIdAndRegistrationId(String attributeId, String registrationId);

    Optional<Member> findByNickname(String nickname);

}
