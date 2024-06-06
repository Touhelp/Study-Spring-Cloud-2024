package com.atguigu.cloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class EmpowerController {

    @GetMapping("/empower")
    public String testAuthorization(){
        return "Entering testAuthorization method";
    }


}
