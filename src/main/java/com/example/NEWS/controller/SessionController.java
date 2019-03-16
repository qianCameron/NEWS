package com.example.NEWS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SessionController {
@RequestMapping("/setting")
    @ResponseBody
    public String setting(){
    return "setting is ok";
}
}
