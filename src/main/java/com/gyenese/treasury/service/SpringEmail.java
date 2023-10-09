package com.gyenese.treasury.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SpringEmail implements EmailService {


    @Override
    public void send(String address, String subject, String body) {
        log.debug("sending email to: {}", address);
    }
}
