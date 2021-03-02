package com.itczy.org.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GrobalExceptionErrorHandler {

    /**
     * 全局异常处理，SecurityException异常就会进入这里
     *
     * 返回401
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorBody> error(SecurityException e){
        log.warn("发生SecurityException异常", e);

        return
                new ResponseEntity<ErrorBody>(
                        ErrorBody.builder()
                                .ErrorMsg("Token非法,用户不允许访问!")
                                .status(HttpStatus.UNAUTHORIZED)
                                .build(),
                    HttpStatus.UNAUTHORIZED
                );
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ErrorBody{
    private String ErrorMsg;
    private HttpStatus status;
}
