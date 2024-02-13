package hello.productservice.main.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class MemberDto {
    private Long memberId;

    @Email(message = "올바른 이메일 형식이어야 합니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String memberEmail;
    @NotBlank(message = "이름을 입력해주세요.")
    private String memberName;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String memberPassword;

//    private String memberEmail;
//
//    private String memberNickName;
}
