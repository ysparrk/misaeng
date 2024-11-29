package dgp.misaeng.domain.member.controller;

import dgp.misaeng.domain.member.dto.MemberDetailResDTO;
import dgp.misaeng.domain.member.service.MemberService;
import dgp.misaeng.global.auth.dto.UserAuthentication;
import dgp.misaeng.global.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("{memberId}")
    public ResponseEntity<ResponseDTO> getDevices(
            @PathVariable Long memberId
    ) {
        //TODO: 토큰으로 사용자 인증
        MemberDetailResDTO memberDetail = memberService.getMemberDetail(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("회원 정보 조회 완료")
                        .data(memberDetail)
                        .build());
    }
}
