package hello.productservice.main.service.impl;

import hello.productservice.main.data.dto.MemberDto;
import hello.productservice.main.data.entity.Member;
import hello.productservice.main.repository.MemberRepository;
import hello.productservice.main.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public MemberDto saveMember(MemberDto memberDto) {
        Member member = new Member();
        member.saveMember(memberDto.getMemberName(),
                memberDto.getMemberPassword(),
                memberDto.getMemberEmail(),
                memberDto.getMemberNickName());
        Member savedMember = memberRepository.save(member);
        return convertToDto(savedMember);
    }

    private MemberDto convertToDto(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId(member.getMemberId());
        memberDto.setMemberName(member.getMemberName());
        memberDto.setMemberPassword(member.getMemberPassword());
        memberDto.setMemberEmail(member.getMemberEmail());
        memberDto.setMemberNickName(member.getMemberNickName());
        return memberDto;
    }
}
