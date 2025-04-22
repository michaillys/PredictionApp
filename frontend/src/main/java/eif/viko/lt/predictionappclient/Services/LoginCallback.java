package eif.viko.lt.predictionappclient.Services;

public interface LoginCallback {
    void onLoginSuccess(String token);
    void onLoginFailure(String errorMessage);
}