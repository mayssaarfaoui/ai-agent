package com.aimanager.agent.controller;

import com.aimanager.agent.utils.Response;
import com.aimanager.agent.utils.Response.ResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

public interface IBaseController {
	

	public default String getOneErrorMessage(BindingResult errors){
        if(errors.hasErrors()){
            return errors.getFieldErrors().get(0).getDefaultMessage();
        }
        return null;
	}
}
