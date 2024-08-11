package com.sunyy.usercentor.controller;


import com.sunyy.usercentor.common.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/api/health")
public class HealthCheckController {
    private static final DateTimeFormatter ymdHms = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String startTime = ymdHms.format(LocalDateTime.now());

    @GetMapping("/check")
    public Message check() {
        return Message.ok(String.valueOf(System.currentTimeMillis()), startTime);
    }

}
