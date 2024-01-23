package hello.productservice.main.controller;

import hello.productservice.main.data.dto.MemberDto;
import hello.productservice.main.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/members")
@SessionAttributes
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
        return "/member/signup3";

    };

    //회원가입
    @ResponseBody
    @PostMapping("")
    public String addNewMember(@ModelAttribute("memberDto") MemberDto memberDto){
        log.info("결과값");
        log.info(String.valueOf(memberDto.getMemberName()));
        log.info(String.valueOf(memberDto.getMemberPassword()));

        MemberDto savedMember = memberService.saveMember(memberDto);
        return savedMember.getMemberName();

    }

//    로그인
    @GetMapping("/login")
    public String showLoginPage(){
        return "/member/loginForm";
    }
    @ResponseBody
    @PostMapping("/login")
    public String login(@RequestParam String memberName,
                        @RequestParam String password){

        //DB에서 id password 조회해서
        //있으면 홈페이지로 보내주고
        //없으면 로그인 에러 메시지 보내줘
        //작동함
        MemberDto foundMemberByName = memberService.findMemberByName(memberName);
        log.info("결과");
        log.info(password);
        log.info(foundMemberByName.getMemberPassword());
        if(foundMemberByName == null){
            log.info("값이없음");
            return "아이디를 확인하세요";

        }else if(Objects.equals(foundMemberByName.getMemberPassword(), password)) {
            return "로그인성공";

        }else {
            return "비밀번호가 틀렸습니다.";
        }
        }

        //작동함
//        MemberDto memberByName = memberService.findMemberById(1L);
//        log.info(id);
//            log.info(password);
//            log.info(String.valueOf(memberByName.getMemberId()));




//    @GetMapping("/test")
//    public String testPage(HttpServletResponse response){
//
//        Cookie cookie = new Cookie("testName", "testValue");
//        cookie.setMaxAge(60 * 1); // 쿠키의 유효 시간 (초 단위)
//        cookie.setPath("/");
//        response.addCookie(cookie);
//            return "redirect:/";
//    }

    //db find member test
    @ResponseBody
    @GetMapping("/test3")
    public String test3(){
//        MemberDto memberDto = new MemberDto();
//        memberDto.setMemberId(1L);
//        memberDto.setMemberName("test1");
//        memberDto.setMemberPassword("1234");

//        MemberDto memberByName = memberService.findMemberById(memberDto);
        MemberDto memberByName = memberService.findMemberByName("dd");
        return memberByName.getMemberPassword();


    };

    @GetMapping("/test")
    public String login(HttpServletRequest request,
                        HttpServletResponse response,
                        HttpSession session){

        session.setAttribute("sessionTest", "sTestValue");
        session.setMaxInactiveInterval(600);

        log.info((String) session.getAttribute("sessionTest"));
        System.out.println("SessionTest Value: " + session.getAttribute("sessionTest"));
        Cookie cookie = new Cookie("testName", "testValue");
        cookie.setMaxAge(60 * 1); // 쿠키의 유효 시간 (초 단위)
        cookie.setPath("/");
        response.addCookie(cookie);
        return "redirect:/";
    }

    @GetMapping("/test2")
    public String login2(HttpServletRequest request,
                        HttpServletResponse response,
                        HttpSession session){
        log.info((String) session.getAttribute("sessionTest"));
        System.out.println("SessionTest Value: " + session.getAttribute("sessionTest"));
        return "redirect:/";
    }


    @ResponseBody
    @GetMapping("/createSession")
    public String createSession(HttpSession session, HttpServletResponse response) {
        // 세션 생성 및 데이터 설정
        session.setAttribute("username", "john_doe");
        // 쿠키 생성 및 설정
        Cookie cookie = new Cookie("sessionId", session.getId());
//        cookie.setMaxAge(60 * 60); // 쿠키의 유효 시간 (초 단위)
        cookie.setPath("/"); // 쿠키의 경로 설정
        response.addCookie(cookie);
        return "ok";

//            return "redirect:/";
    }


    @ResponseBody
    @GetMapping("/getSessionAttribute")
    public String getSessionAttribute(HttpServletRequest request) {
        // 세션에서 속성 가져오기
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        System.out.println("Username from session: " + username);
        return "ok";
    }
}








