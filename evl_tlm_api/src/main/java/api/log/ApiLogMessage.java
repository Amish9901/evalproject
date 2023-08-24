package api.log;

public class ApiLogMessage {
    public String requestId;
    public String path;
    public String pathTemplate;
    public String method;
    public String query;
    public Long elapsedTime;
    private String pathParam;
    private String client;
    private Long requestTime;
    String referenceId;

    public int responseStatus;
    public String host;
    public String apiVersion;
    public String errorMessage;
    private String errorReasonCode;
    public String errorStackTrace;

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int status) {
        this.responseStatus = responseStatus;
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    public String getPath() { return path; }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPathTemplate() { return pathTemplate; }

    public void setPathTemplate(String pathTemplate) {
        this.pathTemplate = pathTemplate;
    }

    public String getPathParam() { return pathParam; }

    public void setPathParam(String pathParam) {
        this.pathParam = pathParam;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
    public String getReferenceId() {
        return errorMessage;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public String getErrorStackTrace() {
        return errorStackTrace;
    }

    public void setErrorStackTrace(String errorStackTrace) {
        this.errorStackTrace = errorStackTrace;
    }
    public String getErrorReasonCode() {
        return errorReasonCode;
    }

    public void setErrorReasonCode(String errorReasonCode) {
        this.errorReasonCode = errorReasonCode;
    }

    public Long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
    }

}
