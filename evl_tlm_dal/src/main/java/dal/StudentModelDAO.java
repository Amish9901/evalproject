package dal;



import dal.model.AverageApiResponseDO;
import dal.model.StudentDetailsDO;
import dal.model.TopStudentResponseDO;
import dal.model.UploadApiResponseDO;

import java.sql.SQLException;
import java.util.List;

public interface StudentModelDAO {
    List<UploadApiResponseDO> postData(List<StudentDetailsDO> studentDetails);
    AverageApiResponseDO getStudentAvg();
    List<TopStudentResponseDO> getToStudent();

}
