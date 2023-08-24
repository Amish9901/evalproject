package api.config;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ApacheConfigBase {
    private org.apache.commons.configuration2.Configuration config;
    protected ApacheConfigBase(String fileName) {

        Configurations configurations = new Configurations();
        try {
            config = configurations.properties(fileName);
            ((PropertiesConfiguration) config).setThrowExceptionOnMissing(true);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
    protected ApacheConfigBase(String fileName, String prefix) {

        this(fileName);
        config = config.subset(prefix);
    }
    protected org.apache.commons.configuration2.Configuration getConfig() {
        return config;
    }
}
