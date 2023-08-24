package api.log;

import com.google.gson.Gson;
import org.glassfish.jersey.server.ExtendedUriInfo;
import org.glassfish.jersey.uri.UriTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;
@Provider
public class ApiLoggingFilterResponse implements ContainerResponseFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger("api-logger");
    private static final Gson GSON = new Gson();

    @Context
    private HttpServletRequest request;
    @Context
    private HttpServletResponse response;

    @Context
    private ExtendedUriInfo uriInfo;

    @Override
    public void filter(ContainerRequestContext containerRequestContext,
                       ContainerResponseContext containerResponseContext) throws IOException {
        Integer statusCode = ResponseStatusHelper.getStatusCode();
        if (statusCode != null) {
            containerResponseContext.setStatus(statusCode);
        }

        ApiLogMessage msg = (ApiLogMessage) request.getAttribute(Constants.ATTRIBUTE_ID_MESSAGE);
        if (msg != null && !"/heartbeat".equals(msg.getPath())) {
            msg.setResponseStatus(containerResponseContext.getStatus());
            msg.setElapsedTime(System.currentTimeMillis() - msg.getRequestTime());
            response.addHeader("ed-request-id", msg.getRequestId());
            msg.setPathTemplate(getPathTemplate());
            msg.setPathParam(getPathParam(msg));
            LOGGER.info("API response sent - RequestId: {}, Path: {}, Status: {}", msg.getRequestId(), msg.getPath(), msg.getResponseStatus());
            LOGGER.info(GSON.toJson(msg));
        }
        if (containerResponseContext.getStatus() >= 400) {
            assert msg != null;
            LOGGER.error("API response error - RequestId: {}, Path: {}, Status: {}", msg.getRequestId(), msg.getPath(), msg.getResponseStatus());
        }



        MDC.clear();
    }

    private String getPathParam(ApiLogMessage msg){
        String path = msg.getPath();
        if (path.contains(","))
            return "multi";
        else
            return "single";
    }

    private String getPathTemplate() {

        StringBuilder pathTemplate = new StringBuilder();
        if (uriInfo != null) {
            List<UriTemplate> matchedTemplates = uriInfo.getMatchedTemplates();
            if (matchedTemplates != null) {
                for (int i = matchedTemplates.size() - 1; i >= 0; i--) {
                    pathTemplate.append(matchedTemplates.get(i).getTemplate());
                }
            }
        }
        return pathTemplate.toString();
    }
}
