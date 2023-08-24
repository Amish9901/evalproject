package inmemory;

import dal.DAOAbstractFactory;
import dal.StudentModelDAO;

public class InMemoryDAOFactory implements DAOAbstractFactory {
    private InMemoryStudentModelDAO inmemoryStudentModelDAO;
    public InMemoryDAOFactory() {
        inmemoryStudentModelDAO = new InMemoryStudentModelDAO();    }

    @Override
    public StudentModelDAO getStudentModelDAO() {
        return inmemoryStudentModelDAO ;
    }
}
