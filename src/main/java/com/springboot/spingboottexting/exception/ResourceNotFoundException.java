package com.springboot.spingboottexting.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String resourceName, String name, Object value){
        super(String.format("%s not found with %s : %s", resourceName, name, value));
        this.resourceName = resourceName;
        this.name = name;
        this.value = value;
    }

    private String resourceName;
    private String name;
    private Object value;

    public String getResourceName() {
        return resourceName;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}