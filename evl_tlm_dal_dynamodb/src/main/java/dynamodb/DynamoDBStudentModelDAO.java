package dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.xspec.N;
import dal.StudentModelDAO;
import dal.model.AverageApiResponseDO;
import dal.model.StudentDetailsDO;
import dal.model.TopStudentResponseDO;
import dal.model.UploadApiResponseDO;
import dynamodb.model.StudentDetailsDynamo;

import java.util.*;


public class DynamoDBStudentModelDAO implements StudentModelDAO {
    private DynamoDBMapper mapper;
    private static AmazonDynamoDB client;

    DynamoDBStudentModelDAO(DynamoDBMapper mapper , AmazonDynamoDB client) {
        this.mapper = mapper;
        this.client = client;
    }
    private static int checkExistence (String id) {
        String expression = "student_id = :v1";
        Map<String, AttributeValue> valueMap = new HashMap<>();
        valueMap.put(":v1", new AttributeValue().withS(id));
        ScanRequest request = new ScanRequest()
                .withTableName("student_details")
                .withFilterExpression(expression)
                .withExpressionAttributeValues(valueMap);

        ScanResult result = client.scan(request);
        return result.getCount();
    }
    private static int getTotalScore() {
        int totalScore = 0;
        ScanRequest request = new ScanRequest()
                .withTableName("student_details")
                .withProjectionExpression("student_score");
        ScanResult response = client.scan(request);
        for(Map<String,AttributeValue> item :response.getItems()) {
            totalScore += Integer.parseInt(item.get("student_score").getN());
        }
        System.out.println(totalScore);
        return totalScore;
    }
    private static int getTotalStudentCount() {
        ScanRequest request = new ScanRequest()
                .withTableName("student_details");
        ScanResult response = client.scan(request);
        System.out.println(response.getCount());
        return response.getCount();

    }
    @Override
    public List<UploadApiResponseDO> postData(List<StudentDetailsDO> studentDetails) {
        int successCount = 0;
        int dublicateCount = 0;
        for (StudentDetailsDO student : studentDetails) {
            if(checkExistence(student.getId()) != 0){
                dublicateCount++;
            }
            else{
                StudentDetailsDynamo studentModel  = new StudentDetailsDynamo();
                studentModel.setId(student.getId());
                studentModel.setName(student.getName());
                studentModel.setScore(student.getScore());
                mapper.save(studentModel);
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
        average = (double) getTotalScore() / getTotalStudentCount();
        AverageApiResponseDO avgResponse = new AverageApiResponseDO();
        avgResponse.setAverage(average);
        avgResponse.setTotalStudent(getTotalStudentCount());

        return avgResponse;
    }

    @Override
    public List<TopStudentResponseDO> getToStudent() {
        int counter = 1;
        ScanRequest request = new ScanRequest()
                .withTableName("student_details");
        ScanResult response = client.scan(request);
        List<Map<String,AttributeValue>> item = response.getItems();
        item.sort(Comparator.comparing(items -> Integer.parseInt(items.get("student_score").getN()),Comparator.reverseOrder()));
        List<TopStudentResponseDO> topStudentsList = new ArrayList<TopStudentResponseDO>();
            for (Map<String, AttributeValue> items : item) {
                if(counter <= 5) {
                    TopStudentResponseDO studentDetails = new TopStudentResponseDO();
                    studentDetails.setId(items.get("student_id").getS());
                    studentDetails.setName(items.get("student_name").getS());
                    studentDetails.setId(items.get("student_score").getN());
                    topStudentsList.add(studentDetails);
                    counter++;
                }
            }
        return topStudentsList;
    }
}




