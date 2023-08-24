package api.convertor;

import api.model.AverageApiResponse;
import api.model.StudentDetails;
import api.model.TopStudentResponse;
import api.model.UploadApiResponse;
import dal.model.AverageApiResponseDO;
import dal.model.StudentDetailsDO;
import dal.model.TopStudentResponseDO;
import dal.model.UploadApiResponseDO;

import java.util.ArrayList;
import java.util.List;

public class ApiModelConvertor {
    public static List<StudentDetailsDO> convertApiModelToDalModel(List<StudentDetails> studentDetails) {
        List<StudentDetailsDO> studentDetailsdal = new ArrayList<  >();
        for(StudentDetails student : studentDetails) {
            StudentDetailsDO result = new StudentDetailsDO();
            result.setId(student.getId());
            result.setName(student.getName());
            result.setScore(student.getScore());
            studentDetailsdal.add(result);
        }
        return studentDetailsdal;
    }
    public static List<UploadApiResponse> convertToApiUploadResponse(List<UploadApiResponseDO> uploadDalResponse) {
        List<UploadApiResponse> uploadApiResponses = new ArrayList<  >();
        for(UploadApiResponseDO response : uploadDalResponse) {
            UploadApiResponse result = new UploadApiResponse();
            result.setStatus(response.getStatus());
            result.setCount(response.getCount());
            uploadApiResponses.add(result);
        }
        return uploadApiResponses;
    }

    public static AverageApiResponse convertToApiAvgResponse(AverageApiResponseDO avgDalResponse) {
        AverageApiResponse response = new AverageApiResponse();
        response.setAverage(avgDalResponse.getAverage());
        response.setTotalStudent(avgDalResponse.getTotalStudent());
        return response;
    }
    public static List<TopStudentResponse> convertTopStudentResponse(List<TopStudentResponseDO> topStudentList) {
        List<TopStudentResponse> responseList = new ArrayList<TopStudentResponse>();
        for (TopStudentResponseDO topStudentResponse : topStudentList) {
            TopStudentResponse topStudent = new TopStudentResponse();
            topStudent.setId(topStudentResponse.getId());
            topStudent.setName(topStudentResponse.getName());
            topStudent.setScore(topStudentResponse.getScore());
            responseList.add(topStudent);
        }
        return responseList;
    }

}
