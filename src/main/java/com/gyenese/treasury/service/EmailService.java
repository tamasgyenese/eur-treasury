package com.gyenese.treasury.service;

public interface EmailService {

    void send(String address, String subject, String body);
}
