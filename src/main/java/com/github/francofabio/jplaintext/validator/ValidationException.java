package com.github.francofabio.jplaintext.validator;

@SuppressWarnings("serial")
public class ValidationException extends RuntimeException {
    
    private String property;
    private String value;

    public ValidationException(String message){
        super(message);
    }
    
    public ValidationException(String message, Throwable thow){
        super(message, thow);
    }
    
    public ValidationException(String property, String message){
        super(message);
        this.setProperty(property);
    }
    
    public ValidationException(String property, String message, String value) {
        super(message);
        this.setProperty(property);
        this.setValue(value);
    }
    
    public ValidationException(Throwable cause, String property, String message, String value) {
        super(message, cause);
        this.setProperty(property);
        this.setValue(value);
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
