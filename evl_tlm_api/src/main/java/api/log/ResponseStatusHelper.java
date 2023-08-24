package api.log;

import org.slf4j.MDC;

import javax.ws.rs.core.Response;

public class ResponseStatusHelper {
    public static Integer getStatusCode() {
        String statusStr = MDC.get(Constants.RESPONSE_STATUS);
        return statusStr == null ? null : Integer.valueOf(statusStr);
    }
    public static void setStatus(Response.Status status) {
        MDC.put(Constants.RESPONSE_STATUS, String.valueOf(status.getStatusCode()));
    }
}
