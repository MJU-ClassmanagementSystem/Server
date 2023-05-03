package mju.capstone.cms.global;

import lombok.Getter;

@Getter
public class BaseResponse<T> {

    private final int status;
    private final String message;

    private final T data;

    public BaseResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}