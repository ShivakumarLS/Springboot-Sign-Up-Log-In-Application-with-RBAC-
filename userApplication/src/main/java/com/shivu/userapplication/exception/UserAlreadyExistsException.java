package com.shivu.userapplication.exception;

public class UserAlreadyExistsException  extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException(String message)
    {
        super(message);
    }
}
