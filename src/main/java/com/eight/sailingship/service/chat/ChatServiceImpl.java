package com.eight.sailingship.service.chat;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
//@AllArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final Map<Long, String> chatMap = new HashMap<>();

    @Override
    @Transactional
    public String save() {

        return null;
    }
}
