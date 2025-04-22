package eif.viko.lt.predictionappclient.Services;

import eif.viko.lt.predictionappclient.Dto.GradePredictionRequest;
import eif.viko.lt.predictionappclient.Dto.UserProfile;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface UserService {

    @PUT("/api/profile/me")
    Call<Void> updateProfile(@Body UserProfile profile, @Header("Authorization") String authHeader);

    @GET("/api/profile/me")
    Call<UserProfile> getProfile(@Header("Authorization") String authHeader);

    @POST("/api/predict/grade")
    Call<Map<String, String>> predictGrade(
            @Header("Authorization") String authHeader,
            @Body GradePredictionRequest request
    );

}
