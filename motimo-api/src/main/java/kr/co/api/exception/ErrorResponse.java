package kr.co.api.exception;

public record ErrorResponse(
        int statusCode,
        String message
) {

}
