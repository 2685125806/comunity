package com.twinkle.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode{
    QUESTION_NOT_FOUND("你找的问题不存在"),
    PAGE_NOT_EXIST("这页好像离家出走了!");

    private String message;

    CustomizeErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
