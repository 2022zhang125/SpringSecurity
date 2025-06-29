package cn.believesun.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/hello")
    public String hello(HttpServletRequest request) {
/*        HttpSession session = request.getSession();
        // 35C3777D70DA9E9A89DFADAF138C0AEE
        System.out.println(session.getId());*/
        return "Hello Spring Security!";
    }
}
