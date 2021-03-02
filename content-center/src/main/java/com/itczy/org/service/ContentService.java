package com.itczy.org.service;

import com.itczy.org.domain.dto.TestDTO;
import com.itczy.org.domain.dto.UserDTO;

public interface ContentService {

    UserDTO getContent(String token);
}
