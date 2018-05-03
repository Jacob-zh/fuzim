package com.acostek.fuzim.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class FuzimController {

    @RequestMapping(value = "/view",method = RequestMethod.GET)
    public String say(){
        return "index";
    }
}
