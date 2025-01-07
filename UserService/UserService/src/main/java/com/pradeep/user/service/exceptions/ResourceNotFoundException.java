package com.pradeep.user.service.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    //extra propertires that you want to manage

    public ResourceNotFoundException() {
        super("resource not found on server !!");
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
