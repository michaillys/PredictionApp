package eif.viko.lt.predictionappclient.Services;
import java.util.List;

public interface ChatBotCallback {
    void onResponse(String bestCategory, List<String> allCategories);
    void onFailure(String errorMessage);
}