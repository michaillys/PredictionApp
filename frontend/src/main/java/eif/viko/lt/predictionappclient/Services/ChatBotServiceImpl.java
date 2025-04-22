package eif.viko.lt.predictionappclient.Services;

import eif.viko.lt.predictionappclient.SecureStorage;
import eif.viko.lt.predictionappclient.Dto.ChatBotResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatBotServiceImpl {

    private final ChatBotService chatBotService;

    public ChatBotServiceImpl() {
        this.chatBotService = RetrofitClient.getInstance().create(ChatBotService.class);
    }


    public void sendMessage(String question, ChatBotCallback callback) {
        String token = SecureStorage.getToken();

        chatBotService.askChatBot("Bearer " + token, question).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ChatBotResponse> call, Response<ChatBotResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChatBotResponse body = response.body();
                    callback.onResponse(body.getBestCategory(), body.getAllCategories());
                } else {
                    callback.onFailure("Klaida. Kodas: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ChatBotResponse> call, Throwable t) {
                callback.onFailure("Tinklo klaida: " + t.getMessage());
            }
        });
    }
}
