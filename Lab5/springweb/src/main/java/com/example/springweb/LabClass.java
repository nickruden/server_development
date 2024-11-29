package com.example.springweb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LabClass {

    @GetMapping("/")
    public String sayHello() {
        return "Hello";
    }
}
