package com.dekapx.apps.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApplicationController {
    @GetMapping(value = "/info", produces = "application/json")
    public String getInfo() {
        log.info("ApplicationController.getInfo() invoked...");
        return "Spring Cloud Stream Kafka Application...";
    }
}
