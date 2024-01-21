package hello.productservice.main.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    @ResponseBody
    @GetMapping(value = "")
    String a(){
        return"1";

    }
}
