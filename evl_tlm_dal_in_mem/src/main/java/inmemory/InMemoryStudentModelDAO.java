package inmemory;

import dal.StudentModelDAO;
import dal.model.AverageApiResponseDO;
import dal.model.StudentDetailsDO;
import dal.model.TopStudentResponseDO;
import dal.model.UploadApiResponseDO;

import java.util.*;

public class InMemoryStudentModelDAO implements StudentModelDAO {
    Map<String, AttributeClass> inmemData = new HashMap<String, AttributeClass>();
    @Override
    public List<UploadApiResponseDO> postData(List<StudentDetailsDO> studentDetails) {
        int successCount = 0;
        int dublicateCount = 0;
        for (StudentDetailsDO student : studentDetails) {
            if(inmemData.get(student.getId()) != null) {
                dublicateCount++;
            }
            else {
                AttributeClass attribute = new AttributeClass();
                attribute.setName(student.getName());
                attribute.setScore(student.getScore());
                inmemData.put(student.getId(), attribute);
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
        int totalStudent = inmemData.size() ;
        int totalScore = 0;
        for(Map.Entry<String,AttributeClass> item:inmemData.entrySet()) {
            totalScore += item.getValue().getScore();
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
        List<Map.Entry<String, AttributeClass>> sortedList =new ArrayList<>(inmemData.entrySet());
        sortedList.sort(Comparator.comparing(entry -> entry.getValue().getScore(),Comparator.reverseOrder()));
        List<TopStudentResponseDO> topStudentResponseList = new ArrayList<>();
        for(Map.Entry<String, AttributeClass> entry : sortedList) {
            if(counter <= 5) {
                TopStudentResponseDO topStudentResponse = new TopStudentResponseDO();
                topStudentResponse.setId(entry.getKey());
                topStudentResponse.setName(entry.getValue().getName());
                topStudentResponse.setScore(entry.getValue().getScore());
                topStudentResponseList.add(topStudentResponse);
                counter++;
            }
        }
        return topStudentResponseList;
    }
}
