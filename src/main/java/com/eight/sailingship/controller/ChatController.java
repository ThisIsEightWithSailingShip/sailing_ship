package com.eight.sailingship.controller;

import com.eight.sailingship.service.chat.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 클라이언트 -> 서버
    // 전체 대화 내역을 저장하기 위한 목적의 컨트롤러
    // 스케줄러 등의 활용 여지가 있을 듯함

    @GetMapping("/sail/chat")
    public String getChat() {
        return null;
    }
}
