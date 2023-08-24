package api.init;


import api.service.DataService;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import javax.ws.rs.ext.Provider;

@Provider
public class ServiceInitializer implements ApplicationEventListener {

    @Override
    public void onEvent(ApplicationEvent applicationEvent) {
        switch (applicationEvent.getType()) {
            case INITIALIZATION_START:{
                try {
                    System.out.println(" --- initializing data service");
                    DataService.initialize();
                    System.out.println(" Done");
                }catch (Throwable t) {
                    t.printStackTrace();
                    System.err.println("======== APP FAILED TO INITIALIZE");
                    System.exit(100);
                }
            }
        }
    }

    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return null;
    }


}
