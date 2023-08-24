package api.log.exc;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ApiLoggingThrowableExceptionMapper extends ApiLoggingExceptionMapperBase implements ExceptionMapper<Throwable> {

    @Override
   public Response toResponse(Throwable throwable) {
        return super.handleToResponse(throwable);
    }
}
