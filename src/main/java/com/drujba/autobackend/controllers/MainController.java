package com.drujba.autobackend.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/")
@RequiredArgsConstructor
public class MainController {

    @GetMapping("/version")
    public  String getVersion() {
        return "v0.1";
    }

}
