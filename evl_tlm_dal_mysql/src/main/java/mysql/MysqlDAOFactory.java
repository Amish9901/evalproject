package mysql;


import dal.DAOAbstractFactory;
import dal.StudentModelDAO;

public class MysqlDAOFactory implements DAOAbstractFactory {
    private MysqlStudentModelDAO mysqlStudentModelDAO;

    public MysqlDAOFactory(String dataSourceName) {
        mysqlStudentModelDAO = new MysqlStudentModelDAO(dataSourceName);
    }
    @Override
    public StudentModelDAO getStudentModelDAO() {
        return mysqlStudentModelDAO;
    }
}
