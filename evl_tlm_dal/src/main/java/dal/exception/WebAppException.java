package dal.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class WebAppException extends WebApplicationException {

    private ApiErrorReasonCode reasonCode;
    public WebAppException(ApiErrorReasonCode reasonCode, String message) {
        super(message);
        this.reasonCode = reasonCode;
    }
    public WebAppException(ApiErrorReasonCode reasonCode, Response.Status status) {
        super(status);
        this.reasonCode = reasonCode;
    }

    public WebAppException(ApiErrorReasonCode reasonCode, String message,
                           Response.Status status) {
        super(message, status);
        this.reasonCode = reasonCode;
    }
    public ApiErrorReasonCode getReasonCode() {
        return reasonCode;
    }
}
