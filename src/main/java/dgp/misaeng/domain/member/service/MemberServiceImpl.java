package dgp.misaeng.domain.member.service;

import dgp.misaeng.domain.member.dto.MemberDetailResDTO;
import dgp.misaeng.domain.member.entity.Member;
import dgp.misaeng.domain.member.repository.MemberRepository;
import dgp.misaeng.global.auth.dto.AuthUserInfo;
import dgp.misaeng.global.auth.dto.OAuthUserInfo;
import dgp.misaeng.global.auth.service.JwtService;
import dgp.misaeng.global.exception.CustomException;
import dgp.misaeng.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
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

    @Override
    public MemberDetailResDTO getMemberDetail(Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MEMBER) {
            @Override
            public ErrorCode getErrorCode() {
                return super.getErrorCode();
            }
        });

        MemberDetailResDTO memberDetailResDTO = MemberDetailResDTO.builder()
                .name(member.getName())
                .email(member.getEmail())
                .build();

        return memberDetailResDTO;
    }
}
