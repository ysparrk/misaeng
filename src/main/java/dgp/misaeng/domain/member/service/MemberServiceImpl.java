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

        if(member == null){
            member = Member.builder()
                    .name(oauthUserInfo.getNickname())
                    .email(oauthUserInfo.getEmail())
                    .isDeleted(false)
                    .build();
            memberRepository.save(member);
        }
        return new AuthUserInfo(member.getId(), member.getEmail(), Arrays.asList("USER"));
    }
}
