package mysql;

import com.edriving.commons.dal.jdbc.BaseDao;
import com.edriving.commons.dal.jdbc.JdbcConnectionPool;
import dal.StudentModelDAO;

import dal.model.AverageApiResponseDO;
import dal.model.StudentDetailsDO;
import dal.model.TopStudentResponseDO;
import dal.model.UploadApiResponseDO;

import java.math.BigDecimal;
import java.sql.SQLException;

import java.util.*;

public class MysqlStudentModelDAO extends BaseDao implements StudentModelDAO {
    private static final String SQL_SAVE_STUDENT_DETAILS = "" + "insert into student_details (id,name,score) values (?,?,?)" ;
    private static final String SQL_SCAN_STUDENT_DETAILS = "" + "select * from student_details where id = ? " ;
    private static final String SQL_GET_STUDENT_AVG ="" +"SELECT " +
            "(SELECT CAST(SUM(score) AS SIGNED) FROM evl.student_details) as total_score, " +
            "(SELECT CAST(COUNT(*) AS SIGNED) FROM evl.student_details) as total_count;";
    private static final String SQL_TOP = ""+ "SELECT * FROM student_details" +
            " ORDER BY score DESC;";



    public MysqlStudentModelDAO(String dataSourceName) {
        super(JdbcConnectionPool.getInstance().initializeDataSource(dataSourceName));
    }

    @Override
    public List<UploadApiResponseDO> postData(List<StudentDetailsDO> studentDetails) {
        int successCount = 0;
        int dublicateCount = 0;
        List<Object[]> params = new ArrayList<>();
        for (StudentDetailsDO student : studentDetails) {
            List<Map<String, Object>> rows = null;
            try {
                rows = query(SQL_SCAN_STUDENT_DETAILS, student.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (rows.size() != 0) {
                dublicateCount++;
            } else {
                try {
                    update(SQL_SAVE_STUDENT_DETAILS,
                            student.getId(),
                            student.getName(),
                            student.getScore()
                    );
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                successCount++;
            }
        }
        List<UploadApiResponseDO> uploadApiResponse = new ArrayList<>();

        UploadApiResponseDO dublicateResponse = new UploadApiResponseDO();
        UploadApiResponseDO successResponse = new UploadApiResponseDO();
        dublicateResponse.setStatus("DUPLICATES");
        dublicateResponse.setCount(dublicateCount);
        successResponse.setStatus("SUCCESS");
        successResponse.setCount(successCount);

        uploadApiResponse.add(dublicateResponse);
        uploadApiResponse.add(successResponse);

        return uploadApiResponse;
    }

    @Override
    public AverageApiResponseDO getStudentAvg() {
        double average = 0;
        int totalStudent = 0;
        int totalScore = 0;

            List<Map<String, Object>> rows;
            try {
                rows = query(SQL_GET_STUDENT_AVG);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            for (Map<String, Object> row : rows) {
                try {
                    totalScore += (BaseDao.cast(row.get("total_score".toUpperCase()), Long.class));
                    totalStudent += (BaseDao.cast(row.get("total_count".toUpperCase()), Long.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        average = (double) totalScore / totalStudent;
        AverageApiResponseDO avgResponse = new AverageApiResponseDO();
        avgResponse.setAverage(average);
        avgResponse.setTotalStudent(totalStudent);

        return avgResponse;

    }

    @Override
    public List<TopStudentResponseDO> getToStudent() {
        int counter = 1;
        List<Map<String, Object>> rows;
        try {
            rows = query(SQL_TOP);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<TopStudentResponseDO> topStudentResponseList = new ArrayList<TopStudentResponseDO>();
            for(Map<String, Object> row : rows){
                if( counter <= 5) {
                    TopStudentResponseDO topStudentResponse = new TopStudentResponseDO();
                    topStudentResponse.setId(BaseDao.cast(row.get("id".toUpperCase()), String.class));
                    topStudentResponse.setName(BaseDao.cast(row.get("name".toUpperCase()), String.class));
                    topStudentResponse.setScore(BaseDao.cast(row.get("score".toUpperCase()), Integer.class));
                    topStudentResponseList.add(topStudentResponse);
                    counter++;
                }
            }

        return topStudentResponseList;
    }
}