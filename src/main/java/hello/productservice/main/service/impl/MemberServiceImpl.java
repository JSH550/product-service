package hello.productservice.main.service.impl;

import hello.productservice.main.data.dto.MemberDto;
import hello.productservice.main.data.entity.Member;
import hello.productservice.main.repository.MemberRepository;
import hello.productservice.main.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
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
                memberDto.getMemberPassword());
//                memberDto.getMemberEmail(),
//                memberDto.getMemberNickName());
        Member savedMember = memberRepository.save(member);
        
        return convertToDto(savedMember);
    }

    @Override
    public MemberDto findMemberById(Long memberId) {
        Optional<Member> foundMember = memberRepository.findById(memberId);
        if(foundMember.isPresent()){
            Member foundMemberEntity = foundMember.get();
            return convertToDto(foundMemberEntity);
        }else{

            return null;
        }
    }

    @Override
    public MemberDto findMemberByName(String memberName) {

        Optional<Member> foundByName = memberRepository.findByMemberName(memberName);
        return convertToDto(foundByName.get());
    }

    ;

    @Override
    public Optional<MemberDto> login(String memberName, String password) {
        try {
            //memberRepository에 의해서 DB에서 찾아진 값이 Optional<Member>로 꺼내진다
            Member member = memberRepository.findByMemberName(memberName)
                    .filter(foundMember -> foundMember.getMemberPassword().equals(password))
                    .orElseThrow(NoSuchFieldException::new);

            return Optional.of(convertToDto(member));
        } catch (NoSuchFieldException e) {
            log.info("로그인에러");
            ;
        }
        return Optional.empty();
    };



    private MemberDto convertToDto(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setMemberId(member.getMemberId());
        memberDto.setMemberName(member.getMemberName());
        memberDto.setMemberPassword(member.getMemberPassword());
//        memberDto.setMemberEmail(member.getMemberEmail());
//        memberDto.setMemberNickName(member.getMemberNickName());
        return memberDto;
    }
}
