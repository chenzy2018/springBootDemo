package com.itczy.org.controller;

import com.itczy.org.domain.dto.TestDTO;
import com.itczy.org.service.ContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content")
@Slf4j
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping("/getContent")
    public TestDTO getContent(){
        log.info("这是个log");
        return contentService.getContent();
    }
}
