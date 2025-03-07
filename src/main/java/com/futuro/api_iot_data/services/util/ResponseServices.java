package com.futuro.api_iot_data.services.util;

import com.futuro.api_iot_data.services.util.ResponseServices.ResponseServicesBuilder;

public class ResponseServices {
    private String message;
    private int code;
    private Object modelDAO;

    public ResponseServices(String message, int code, Object modelDAO) {
        this.message = message;
        this.code = code;
        this.modelDAO = modelDAO;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getModelDAO() {
        return modelDAO;
    }

    public void setModelDAO(Object modelDAO) {
        this.modelDAO = modelDAO;
    }

    public static ResponseServicesBuilder builder() {
        return new ResponseServicesBuilder();
    }

    public static class ResponseServicesBuilder {
        private String message;
        private int code;
        private Object modelDAO;

        public ResponseServicesBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ResponseServicesBuilder code(int code) {
            this.code = code;
            return this;
        }

        public ResponseServicesBuilder modelDAO(Object modelDAO) {
            this.modelDAO = modelDAO;
            return this;
        }

        public ResponseServices build() {
            return new ResponseServices(message, code, modelDAO);
        }
    }
}