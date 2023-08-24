package api.service;

import api.config.ApacheDataServiceAbstractConfig;
import api.config.ApcheDataServiceConfig;
import dal.DAOAbstractFactory;
import dynamodb.DynamoDbConfig;
import dynamodb.DynamodbDAOFactory;
import inmemory.InMemoryDAOFactory;
import mysql.MysqlDAOFactory;

public class DataService {
    private static DataService instance;
    private static DAOAbstractFactory daoFactory;
    public static DataService getInstance() {
        return instance;
    }

    public static void initialize(){
        if(instance == null) {
            instance = new DataService(new ApcheDataServiceConfig("trip"));
        }
    }

    private DataService(ApacheDataServiceAbstractConfig serviceConfig ) {
        if(serviceConfig.isEnabled()) {
            if("dynamo".equalsIgnoreCase(serviceConfig.getDataSource())){
               daoFactory = new DynamodbDAOFactory((DynamoDbConfig)serviceConfig);
            }
            else if("mysql".equalsIgnoreCase(serviceConfig.getDataSource())) {
                daoFactory = new MysqlDAOFactory(serviceConfig.getDataBaseName());
            }
            else {
                daoFactory = new InMemoryDAOFactory();
            }
        }
    }

    public  DAOAbstractFactory getDAOFactory() {
        return daoFactory;
    }
}
