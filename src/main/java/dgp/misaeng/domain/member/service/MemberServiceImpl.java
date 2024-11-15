package dgp.misaeng.domain.member.service;

import dgp.misaeng.domain.member.entity.Member;
import dgp.misaeng.domain.member.repository.MemberRepository;
import dgp.misaeng.global.auth.dto.AuthUserInfo;
import dgp.misaeng.global.auth.dto.OAuthUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public AuthUserInfo getOrRegisterUser(OAuthUserInfo oauthUserInfo) {

        // 유저가 존재하는지 확인
        Member member = memberRepository.findByEmail(oauthUserInfo.getEmail());

        // 일단 등록은 해놓고 추가 정보 없을 시 입력하는 페이지로 이동하도록 처리
        if(member == null){
            member = Member.builder()
                    .nickname(oauthUserInfo.getNickname())
                    .email(oauthUserInfo.getEmail())
                    .isDeleted(false)
                    .build();
            memberRepository.save(member);
        }
        return new AuthUserInfo(member.getId(), member.getEmail(), Arrays.asList("USER"));
    }
}
