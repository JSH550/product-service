package hello.productservice.main.service;

import hello.productservice.main.data.dto.MemberDto;
import hello.productservice.main.data.entity.Member;

import java.util.Optional;

public interface MemberService {

    MemberDto saveMember(MemberDto memberDto);

    MemberDto findMemberById(Long memberId);

    MemberDto findMemberByName(String memberName);

    Optional<MemberDto> login(String memberName, String password);
}
