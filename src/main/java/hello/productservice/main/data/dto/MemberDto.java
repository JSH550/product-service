package hello.productservice.main.data.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class MemberDto {
    private Long memberId;

    private String memberName;

    private String memberPassword;

//    private String memberEmail;
//
//    private String memberNickName;
}
