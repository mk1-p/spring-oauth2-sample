package com.example.oauth2sample.domain.members;

import com.example.oauth2sample.global.security.dto.CustomUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * Member 조회 후 저장 및 세팅
     * @param userInfo
     * @return
     */
    public Member saveOrUpdate(CustomUserInfo userInfo) {

        // Member 객체 조회 및 세팅
        Member member = memberRepository.findByAttributeIdAndRegistrationId(userInfo.getAttributeId(), userInfo.getRegistrationId())
                .map(entity -> entity.updateNameAndEmail(entity.getName(),entity.getEmail()))
                .orElseGet(() -> {
                    // Member 객체 첫 생성
                    Member newMember = Member.toEntity(userInfo);
                    // Nickname이 정의 되지 않은 경우 새로운 닉네임 입력
                    if (newMember.getNickname() == null) {
                        newMember.setNickname(initNickname());
                    }
                    return newMember;
                });

        return memberRepository.save(member);
    }

    /**
     * 유저 닉네임 세팅
     * 중복되는 경우를 방지하기 위한 재귀함수
     * @return
     */
    private String initNickname() {
        String randomNickname = getRandomNickname();
        Optional<Member> nicknameOpt = memberRepository.findByNickname(randomNickname);
        if (nicknameOpt.isEmpty()) {
            return randomNickname;
        } else {
            return initNickname();
        }
    }

    /**
     * 랜덤한 스트링 닉네임을 만들어줌
     * @return
     */
    private String getRandomNickname() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit,rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        String time = String.valueOf(System.currentTimeMillis());
        String nickName = String.format("user-%s%s",generatedString,time);

        return nickName;
    }


}
