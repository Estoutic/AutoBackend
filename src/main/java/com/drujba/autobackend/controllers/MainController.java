package com.drujba.autobackend.controllers;


import com.drujba.autobackend.models.enums.Locale;
import com.drujba.autobackend.models.enums.car.TransmissionType;
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

    @GetMapping(value = "/test", produces = "application/json; charset=UTF-8")
    public String test() {
        return TransmissionType.AUTOMATIC.getLocalizedValue(Locale.RU);
    }

}
