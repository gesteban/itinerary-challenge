package com.challenge.itinerary.controller;


import com.challenge.itinerary.exceptions.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.bind.DataBindingException;
import java.util.Date;

/**
 * Centralized exception handler for {@link ItineraryController}. Returns a coherent message type for all exceptions.
 */
@RestControllerAdvice
public class ItineraryControllerAdvice {

    Logger logger = LoggerFactory.getLogger(ItineraryControllerAdvice.class);

    /**
     * Handles runtime exceptions converting them to ItineraryRestException and responding with HTTP 500.
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleRuntimeException(RuntimeException ex) {
        logger.error("RuntimeException handled:", ex);
        return new ErrorMessage(new Date(), "We are sorry something went wrong, try again later please.");
    }

    /**
     * Handles invalid requests responding with HTTP 400.
     */
    @ExceptionHandler({DataBindingException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handleArgumentInvalid(Exception ex) {
        logger.error("DataBindingException handled:", ex);
        return new ErrorMessage(new Date(), "Something went wrong, data received seems invalid.");
    }

    /**
     * Handles non specialized exceptions converting them to ItineraryRestException and responding with HTTP 500.
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleException(Exception ex) {
        logger.error("Exception handled:", ex);
        return new ErrorMessage(new Date(), "We are sorry something went wrong, try again later please.");
    }
}
