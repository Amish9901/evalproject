package api.log.exc;

import api.log.ApiLogMessage;
import api.log.Constants;
import api.utils.ApiResponseException;
import api.utils.InvalidTokenException;
import api.utils.RetrofitClientException;
import dal.exception.WebAppException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ApiLoggingExceptionMapperBase implements ExceptionMapper<Throwable> {
//    private static final Logger ERROR_LOGGER = LoggerFactory.getLogger(ApiLoggingExceptionMapperBase.class);
    private static final Logger ERROR_LOGGER = LoggerFactory.getLogger("error-logger");
    private static final int MAX_STACK_TRACE_LINES = 5;
    @Context
    HttpServletRequest request;

    private static String getStackTrace(StackTraceElement[] stackTrace) {
        if (stackTrace == null) {
            return null;
        }
        int count = 0;
        StringBuilder str = new StringBuilder();
        for (StackTraceElement row : stackTrace) {
            count++;
            if (count > MAX_STACK_TRACE_LINES) {
                break;
            }
            str.append(row.toString());
        }
        return str.toString();
    }

    @Override
    public Response toResponse(Throwable t0) {
        ApiLogMessage logMessage = (ApiLogMessage) request.getAttribute("api-log-message");
        ApiErrorMessage errorMessage = new ApiErrorMessage();
        if (logMessage != null) {
            errorMessage.setReferenceId(logMessage.getReferenceId());
            errorMessage.setRequestId(logMessage.requestId);
        }
        Throwable t = handleException(t0, errorMessage);
        if (logMessage != null) {
            logMessage.errorMessage = errorMessage.getMessage();
            logMessage.errorStackTrace = getStackTrace(t.getStackTrace());
        }
        return Response.status(errorMessage.getHttpStatus())
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

        private static boolean isTimeoutException(Throwable t) {
        if (t == null) {
            return false;
        }
        if (t instanceof TimeoutException) {
            return true;
        }
        if (t instanceof IOException) {
            return t.getCause() instanceof TimeoutException;
        }
        return false;
    }
    Response handleToResponse(Throwable throwable) {
        ApiLogMessage logMessage = (ApiLogMessage) request.getAttribute(Constants.ATTRIBUTE_ID_MESSAGE);
        ApiErrorMessage errorMessage = new ApiErrorMessage();
        if (logMessage != null) {
            errorMessage.requestId = logMessage.requestId;
        }
        Throwable t = handleException(throwable, errorMessage);
        if (logMessage != null) {
            logMessage.errorMessage = errorMessage.message;
            logMessage.errorStackTrace = getStackTrace(t.getStackTrace());
        }
        return Response.status(errorMessage.httpStatus)
                .entity(errorMessage)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

    private static Throwable handleException(Throwable t0, ApiErrorMessage errorMessage) {

        Throwable t = handleExceptionRecursive(t0, false, errorMessage);
        if (errorMessage.httpStatus == null) {
            errorMessage.httpStatus = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
            errorMessage.message = Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        ERROR_LOGGER.error("Error occurred: (" + printErrorMessage(errorMessage) + ")");
        return t;
    }
    private static Throwable handleExceptionRecursive(Throwable t, boolean isNested, ApiErrorMessage errorMessage) {
        if (!isNested) {
            ERROR_LOGGER.error("Error occurred (" + errorMessage.requestId + ")", t);
        }
        if (t instanceof java.util.concurrent.CompletionException) {
            return handleExceptionRecursive(t.getCause(), true, errorMessage);
        } else if (isTimeoutException(t)) {
            errorMessage.httpStatus = Response.Status.GATEWAY_TIMEOUT.getStatusCode();
            errorMessage.message = Response.Status.GATEWAY_TIMEOUT.getReasonPhrase();
        } else if (t instanceof ApiResponseException) {
            ApiResponseException exc = (ApiResponseException) t;
            errorMessage.httpStatus = exc.getStatus();
            errorMessage.message = exc.getErrorMessage();
        } else if (t instanceof WebApplicationException) {
            handleWebApplicationException((WebApplicationException) t, errorMessage);
        } else if (t instanceof InvalidTokenException) {
            handleInvalidTokenException(errorMessage);
        } else if (t instanceof ValidationException) {
            handleValidationException((ValidationException) t, errorMessage);
        } else if (t instanceof RetrofitClientException) {
        }
        return t;
    }

    private static void handleInvalidTokenException(ApiErrorMessage errorMessage) {
        errorMessage.httpStatus = Response.Status.UNAUTHORIZED.getStatusCode();
        errorMessage.message = "handle InvalidTokenException";
    }

    private static void handleValidationException(ValidationException t, ApiErrorMessage errorMessage) {
        String excMessage = t.getMessage();
        if (excMessage != null) {
            errorMessage.httpStatus = Response.Status.BAD_REQUEST.getStatusCode();
            errorMessage.message = "handle ValidationException: " + excMessage;
        }
    }

    private static void handleWebApplicationException(WebApplicationException t, ApiErrorMessage errorMessage) {
        Response excResponse = t.getResponse();
        if (excResponse != null) {
            errorMessage.httpStatus = excResponse.getStatus();
            if (excResponse.getStatusInfo() != null) {
                errorMessage.message = excResponse.getStatusInfo().getReasonPhrase();
            }
        }
        if(t instanceof WebAppExceptionWithReason) {
            errorMessage.setReasonCode(((WebAppExceptionWithReason) t).getReasonCode());
        }
        else if( t instanceof WebAppException ) {
            errorMessage.setReasonCode(((WebAppException) t).getReasonCode());
        }
    }

    private static String printErrorMessage(ApiErrorMessage errorMessage) {
        StringBuilder str = new StringBuilder();
        if (errorMessage != null) {
            str.append("reqId=").append(errorMessage.requestId).append("; ");
            str.append("refId=").append(errorMessage.referenceId).append("; ");
            str.append("status=").append(errorMessage.httpStatus).append("; ");
            str.append("msg=").append(errorMessage.message).append("; ");
            str.append("reason=").append(errorMessage.getReasonCode()).append("; ");
        }
        return str.toString();
    }
}
