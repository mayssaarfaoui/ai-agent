package com.aimanager.agent.utils;

import org.springframework.http.HttpStatus;

public class Response<D> {

    public static class ResponseException extends RuntimeException{

        private static final long serialVersionUID = 1983725415259954780L;
        private Response<?> response;
        private int status;

        public <D> ResponseException(Response<D> response){
            this(response, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        public <D> ResponseException(Response<D> response, int status){
            this.response = response;
            this.status = status;
        }

        public Response<?> response() {
            return response;
        }

        public int status() {
            return status;
        }
    }

    public enum Status{SUCCESS, ERROR};

    private Status status;
    private String message;
    private D data;

    public static void throwError(int status){
        throw new ResponseException(error(), status);
    }

    public static void throwError(String message, int status){
        throw new ResponseException(error(message, null), status);
    }

    public static <T> void throwError(String message, T data, int status){
        throw new ResponseException(error(message, data), status);
    }

    public static void throwError(){
        throw new ResponseException(error());
    }

    public static void throwError(String message){
        throw new ResponseException(error(message, null));
    }

    public static <T> void throwError(String message, T data){
        throw new ResponseException(error(message, data));
    }

    public static <T> Response<T> error(){
        return error(null, (T)null);
    }

    public static <T> Response<T> error(String message){
        return error(message, null);
    }

    public static <T> Response<T> error(String message, T data){
        Response<T> response = new Response<T>(Status.ERROR, message, data);
        return response;
    }

    public static <T> Response<T> success(){
        return success(null, null);
    }

    public static <T> Response<T> success(String message){
        return success(message, null);
    }

    public static <T> Response<T> success(String message, T data){
        return new Response<T>(Status.SUCCESS, message, data);
    }

    @Deprecated
    public Response(){}			// should be used only for deserialization

    public Response(Status status, String message, D data) {
        super();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
    public D getData() {
        return data;
    }
}
