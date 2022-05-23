package com.adda.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/advert/generator")
public class QRcodeController {

    @GetMapping("/heroku")
    public String heroku() {
        return "Heroku app is running";
    }
}
