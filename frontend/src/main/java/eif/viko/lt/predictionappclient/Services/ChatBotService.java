package eif.viko.lt.predictionappclient.Services;

import eif.viko.lt.predictionappclient.Dto.ChatBotResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ChatBotService {
    @GET("/chatbot/ask")
    Call<ChatBotResponse> askChatBot(
            @Header("Authorization") String authHeader,
            @Query("question") String question
    );

}
