package api.utils;

public class ApiResponseException extends RuntimeException{

    private final int status;
    private final String errorMessage;

    public ApiResponseException(int status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
