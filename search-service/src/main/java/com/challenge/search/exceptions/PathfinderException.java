package com.challenge.search.exceptions;

public class PathfinderException extends RuntimeException {

    public PathfinderException(String errorMessage) {
        super(errorMessage);
    }

    public PathfinderException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

}
