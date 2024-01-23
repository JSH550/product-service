package hello.productservice.main.service;

import hello.productservice.main.data.dto.MemberDto;
import hello.productservice.main.data.entity.Member;

public interface MemberService {

    MemberDto saveMember(MemberDto memberDto);

    MemberDto findMemberById(Long memberId);

    MemberDto findMemberByName(String memberName);
}
