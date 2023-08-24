package api.log.exc;

import dal.exception.ApiErrorReasonCode;

public class ApiErrorMessage {
    Integer httpStatus;
    String message;
    String requestId;
    String referenceId;
    private ApiErrorReasonCode reasonCode;

    public ApiErrorReasonCode getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(ApiErrorReasonCode reasonCode) {
        this.reasonCode = reasonCode;
    }

    String apiVersion = System.getProperty("SERVER_VERSION");

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
