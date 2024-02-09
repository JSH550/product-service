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

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/members")
//@SessionAttributes
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
        return "signup";
    };

    //회원가입
    @ResponseBody
    @PostMapping("")
    public String addNewMember(@ModelAttribute("memberDto") MemberDto memberDto,
                               HttpServletRequest request){
//        log.info("결과값");
//        log.info(String.valueOf(memberDto.getMemberName()));
//        log.info(String.valueOf(memberDto.getMemberPassword()));



        MemberDto savedMember = memberService.saveMember(memberDto);
        return savedMember.getMemberName();

    }

//    로그인
    @GetMapping("/login")
    public String showLoginPage(){
        return "/member/loginForm";
    }


    @PostMapping("/login")
    public String login(@RequestParam String memberEmail,
                        @RequestParam String password,
                        HttpServletResponse response,
                        HttpServletRequest request) {

        Optional<MemberDto> loginMember = memberService.login(memberEmail, password);
        if (loginMember.isEmpty()==true){
            log.info("로그인 에러 검출");
            return "/member/loginForm";
        }
        //세션+쿠키(토큰) 방식으로 로그인 유저 저장
        //getSession(true) 세션이 없으면 만들고 있으면 반환
        //getSession(false) 세션이 없으면 새로만들지 않음
        HttpSession session = request.getSession(true);
        //세션에 로그인 회원 정보 보관
        session.setAttribute("LOGIN_MEMBER",loginMember.get().getMemberName());
        log.info("Session LOGIN_MEMBER: {}", session.getAttribute("LOGIN_MEMBER"));

//        return "redirect:/members/mypage";
        return "redirect:/members";

    };


    //(@SessionAttribute(name ="LOGIN_MEMBER",required = false session에 있는지 확인, 새로생성하진 않음
    @GetMapping("/mypage")
    public String mypage(@SessionAttribute(name ="LOGIN_MEMBER",required = false) String loginMember
                        ,HttpServletRequest request,
                         Model model){
        if(loginMember==null){
            log.info("로그인 하지 않은 사용자 login page로 redirect");
            return "redirect:/members/login";
        }else {

            log.info("login member, mypage로 redirect");
            log.info(String.valueOf(loginMember));

            model.addAttribute("member",loginMember);
            return "/member/memberMyPage";
        }







//        memberId.equals(null) 값이 null일경우 equals호출할수 없음 Objects.equlas(memberId,null)이렇게 하자
        /*
        mypage 접속시 cookie 확인, 없거나 DB에서 조회가 안되면 로그인 페이지로 리다이렉트
         */
//        if ( Objects.equals(memberId, null)){
//            log.info("로그인 하지 않은 사용자");
//        return "redirect:/members/login";
//       }else if(memberEmptyCheck(memberId)) {
//            log.info("등록되지 않은 사용자 접속");
//            return "redirect:/members/login";
//        }else {
//            model.addAttribute("memberId",memberId);
//            return "/member/memberMyPage";
//        }


    };

    public boolean memberEmptyCheck(String memberId){
        Optional<MemberDto> memberByName = Optional.ofNullable(memberService.findMemberByName(memberId));
        if (memberByName.isEmpty()==true){
            return true;
        }else {
            return false;
        }
    }

//    @GetMapping("/logout")
//    public String lougOut(HttpServletResponse response){
//        Cookie cookie = deleteCookie("memberId");
//        response.addCookie(cookie);
//        return "redirect:/members";
//    }

    @GetMapping("/logout")
    public String lougOut(HttpServletRequest request,
            HttpServletResponse response){
        HttpSession session = request.getSession(false);
        if(session!=null){
            //session 정보 삭제
            session.invalidate();
        }

        return "redirect:/members";
    }
    public Cookie deleteCookie(String cookieName){
        //cookie age를 0으로 바꿔 삭제
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        return cookie;
    }
}








