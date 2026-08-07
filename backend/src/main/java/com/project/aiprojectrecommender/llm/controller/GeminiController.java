package com.project.aiprojectrecommender.llm.controller;

import com.project.aiprojectrecommender.llm.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/llm")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @GetMapping("/test")
    public String test() {

        return geminiService.generate("Reply with exactly: Hello from Gemini?");

    }

}
