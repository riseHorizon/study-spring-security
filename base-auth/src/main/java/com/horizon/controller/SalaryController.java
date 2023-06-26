package com.horizon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/baseAuth/api/com/salary")
public class SalaryController {

    @GetMapping("/query")
    public String query() {
        return "salary";
    }

}
