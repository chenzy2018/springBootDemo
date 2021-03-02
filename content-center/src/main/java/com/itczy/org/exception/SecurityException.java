package com.itczy.org.exception;

public class SecurityException extends RuntimeException{

    public SecurityException(){
        super();
    }

    public SecurityException(String msg){
        super(msg);
    }

    public SecurityException(String msg, Throwable e){
        super(msg, e);
    }

}
