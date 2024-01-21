package hello.productservice.main.controller;

import hello.productservice.main.data.dto.MemberDto;
import hello.productservice.main.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/members")
public class MemberController {

  private final MemberService memberService;
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
//    @ResponseBody


    @GetMapping(value = "")
   public String showMemberHome(){

        return"/member/memberHome";
    }
    @GetMapping(value = "/signup")
    public String showSignup(Model model){
        model.addAttribute("memberDto",new MemberDto());
        return "/member/signup2";

    };

    //회원가입
    @ResponseBody
    @PostMapping("")
    public String addNewMember(@ModelAttribute("memberDto") MemberDto memberDto){
        log.info("결과값");
        log.info(String.valueOf(memberDto.getMemberEmail()));
        log.info(String.valueOf(memberDto.getMemberName()));
        log.info(String.valueOf(memberDto.getMemberPassword()));
        log.info(String.valueOf(memberDto.getMemberNickName()));
        MemberDto savedMember = memberService.saveMember(memberDto);
        return savedMember.getMemberName();

    }




}
