package com.groupeisi.services.controller;

import com.groupeisi.services.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService UserService;

    @GetMapping("/test")
    public String hello(){
        return "Hello world!";
    }

}
