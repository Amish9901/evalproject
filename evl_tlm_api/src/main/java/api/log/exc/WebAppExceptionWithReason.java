package api.log.exc;

import dal.exception.ApiErrorReasonCode;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class WebAppExceptionWithReason extends WebApplicationException {

    private ApiErrorReasonCode reasonCode;


    public WebAppExceptionWithReason(ApiErrorReasonCode reasonCode, String message,
                                     Response.Status status) {
        super(message, status);
        this.reasonCode = reasonCode;
    }
    public ApiErrorReasonCode getReasonCode() {
        return reasonCode;
    }
}
