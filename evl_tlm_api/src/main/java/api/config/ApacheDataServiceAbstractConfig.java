package api.config;

public class ApacheDataServiceAbstractConfig extends ApacheConfigBase  {

private static final String CONFIGURATION_FILE_NAME = "datasource.properties";
    private static final String PROPERTY_IS_ENABLED = "is_enabled";
    private static final String PROPERTY_DATA_SOURCE = "datasource";
    private static final String PROPERTY_DATABASE_NAME = "databasename";
    public ApacheDataServiceAbstractConfig(String prefix) {
        super(CONFIGURATION_FILE_NAME , prefix);
    }

    public Boolean isEnabled() {
        return getConfig().getBoolean(PROPERTY_IS_ENABLED, null);
    }
    public String getDataSource() {
        return getConfig().getString(PROPERTY_DATA_SOURCE,null);
    }
    public String getDataBaseName() {
        return getConfig().getString(PROPERTY_DATABASE_NAME,null);
    }
}
