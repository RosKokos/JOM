package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class RegistrationController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/create-user")
    public String registration( Model model) {
        model.addAttribute("user", new User());
        return "create-user";
    }

    @PostMapping("/create-user")
    public String addUser(@Validated @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "create-user";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleService.readById(2));
        User newUser = userService.create(user);
        return "redirect:/todos/all/users/" + newUser.getId();

    }
    @GetMapping("/todo")
    public String myToDos(Principal principal) {
        long id = 0;
        for(User user : userService.getAll()){
            if (user.getEmail().equals(principal.getName())){
                id = user.getId();
            }
        }
        return "redirect:/todos/all/users/"+id;
    }
    @GetMapping("/Forbidden")
    public ModelAndView accessDenied(Principal user){
        ModelAndView model = new ModelAndView();
        if (user != null){
            model.addObject("error","Forbidden");
        }
        model.setViewName("forbidden");
        return model;
    }

}