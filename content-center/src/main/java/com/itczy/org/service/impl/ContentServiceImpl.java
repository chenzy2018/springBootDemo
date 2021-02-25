package com.itczy.org.service.impl;

import com.itczy.org.domain.dto.TestDTO;
import com.itczy.org.service.ContentService;
import org.springframework.stereotype.Service;

@Service
public class ContentServiceImpl implements ContentService {

    @Override
    public TestDTO getContent() {
        return TestDTO.builder().ID("1").name("success").build();
    }

}
