package com.eight.sailingship.controller;

import com.eight.sailingship.repository.TempRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TempController {

    private final TempRepository tempRepository;
}
