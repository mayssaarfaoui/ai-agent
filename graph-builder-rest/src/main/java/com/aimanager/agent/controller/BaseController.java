package com.aimanager.agent.controller;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.aimanager.agent.utils.Response;
import com.aimanager.agent.utils.Response.ResponseException;

public class BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(BaseController.class);
	

	public String getOneErrorMessage(BindingResult errors){
        if(errors.hasErrors()){
            return errors.getFieldErrors().get(0).getDefaultMessage();
        }
        return null;
	}


    @ExceptionHandler( Exception.class )
    public Response<?> handleException(Exception exception) {
    	LOG.error("", exception);
    	return Response.error(exception.getMessage());
    }
    
    @ExceptionHandler( EntityNotFoundException.class )
    public Response<?> handleEntityNotFoundException(EntityNotFoundException exception) {
    	return Response.error(exception.getMessage());
    }

    @ExceptionHandler( ResponseException.class )
    public Response<?> responseException(ResponseException exception, HttpServletResponse response) {
    	response.setStatus(exception.status());
    	return exception.response();
    }

}
