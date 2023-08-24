package api;

import api.convertor.ApiModelConvertor;
import api.log.exc.WebAppExceptionWithReason;
import api.model.AverageApiResponse;
import api.model.StudentDetails;
import api.model.TopStudentResponse;
import api.model.UploadApiResponse;
import api.service.DataService;
import dal.StudentModelDAO;
import dal.exception.ApiErrorReasonCode;
import dal.model.AverageApiResponseDO;
import dal.model.StudentDetailsDO;
import dal.model.TopStudentResponseDO;
import dal.model.UploadApiResponseDO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/api")
public class StudentResource {
    @POST
    @Path("/studentdetails")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<UploadApiResponse> uploadStudentRecords( List<StudentDetails> studentDetails) {
        StudentModelDAO studentModelDAO = DataService.getInstance().getDAOFactory().getStudentModelDAO();
        List<StudentDetailsDO> studentModel = ApiModelConvertor.convertApiModelToDalModel(studentDetails);
        List<UploadApiResponseDO> resp = studentModelDAO.postData(studentModel);
        return ApiModelConvertor.convertToApiUploadResponse(resp);
    }

    @GET
    @Path("/avg")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AverageApiResponse getAvegOfStudent() {
        StudentModelDAO studentModelDAO = DataService.getInstance().getDAOFactory().getStudentModelDAO();
        AverageApiResponseDO resp = studentModelDAO.getStudentAvg();
        if(resp.getTotalStudent() == 0) {
            throw new WebAppExceptionWithReason(ApiErrorReasonCode.NO_STUDENTS_ARE_PRESENT, "NO STUDENT IS PRESENT", Response.Status.NOT_FOUND);
        }
        return ApiModelConvertor.convertToApiAvgResponse(resp);
    }

    @GET
    @Path("/topstudents")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TopStudentResponse> topStudentResponse() {
        StudentModelDAO studentModelDAO = DataService.getInstance().getDAOFactory().getStudentModelDAO();
       List<TopStudentResponseDO> resp = studentModelDAO.getToStudent();
       if( resp.size() == 0 ) {
           throw new WebAppExceptionWithReason(ApiErrorReasonCode.NO_STUDENTS_ARE_PRESENT, "NO STUDENT IS PRESENT SO NO TOP STUDENT", Response.Status.NOT_FOUND);
       }
        return ApiModelConvertor.convertTopStudentResponse(resp);
    }

}
