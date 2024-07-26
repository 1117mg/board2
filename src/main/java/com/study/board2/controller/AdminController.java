package com.study.board2.controller;

import com.study.board2.dto.Board;
import com.study.board2.dto.JoinForm;
import com.study.board2.dto.LoginForm;
import com.study.board2.dto.User;
import com.study.board2.service.PostService;
import com.study.board2.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/master")
@Controller
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/auth/login")
    public String loginForm(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info(String.valueOf(authentication));
        if (authentication instanceof AnonymousAuthenticationToken){
            model.addAttribute("loginForm", new LoginForm());
            return "master/login";}
        return "master/main";
    }

    @GetMapping("/auth/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }

        return "redirect:/master/main";
    }

    @GetMapping("/auth/join")
    public String joinForm(Model model){
        model.addAttribute("joinForm", new JoinForm());
        return "master/join";
    }

    @PostMapping("/auth/join")
    public String join(JoinForm form,Model model){
        if(userService.findByUserId(form.getLoginId())!=null){
            model.addAttribute("IdDuplicate",true);
        }
        User user = User.builder()
                .userId(form.getLoginId())
                .userPw(passwordEncoder.encode(form.getLoginPw()))
                .userName(form.getUserName())
                .userEmail(form.getUserEmail())
                .userRole("ROLE_ADMIN")
                .build();
        userService.register(user);
        return "redirect:/master/auth/login";
    }

    @GetMapping("/main")
    public String main(){
        return "master/main";
    }

    @GetMapping("/users")
    public String adminList(Model model) {
        List<User> users = userService.getAllAdmins();
        model.addAttribute("users", users);
        return "master/adminList";
    }

    @GetMapping("/user/{idx}")
    public String adminDetail(@PathVariable("idx") int idx, Model model){
        User user=userService.findAdminByIdx(idx);
        model.addAttribute("user",user);
        return "master/adminDetail";
    }

    @PostMapping("/user/{idx}")
    public String adminUpdate(@PathVariable("idx") int idx, @ModelAttribute("user") User user){
        userService.updateAdmin(user);
        return "redirect:/master/user/"+user.getIdx();
    }

    @GetMapping("/user/new")
    public String adminNewForm(Model model) {
        model.addAttribute("joinForm", new JoinForm());
        return "master/adminNew";
    }

    @PostMapping("/user/new")
    public String adminNew(JoinForm form){
        User user = User.builder()
                .userId(form.getLoginId())
                .userPw(passwordEncoder.encode(form.getLoginPw()))
                .userName(form.getUserName())
                .userEmail(form.getUserEmail())
                .userRole("ROLE_ADMIN")
                .build();
        userService.register(user);
        return "redirect:/master/users";
    }
}
