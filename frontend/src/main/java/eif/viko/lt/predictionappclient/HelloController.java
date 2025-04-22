package eif.viko.lt.predictionappclient;

import com.google.gson.Gson;
import eif.viko.lt.predictionappclient.Dto.ChatBotResponse;
import eif.viko.lt.predictionappclient.Dto.GradePredictionRequest;
import eif.viko.lt.predictionappclient.Dto.UserProfile;
import eif.viko.lt.predictionappclient.Services.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import retrofit2.Call;
import retrofit2.Response;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Tab profileTab;

    @FXML
    private VBox profilePanelBox;

    @FXML
    private Button saveProfileBtn;

    @FXML
    private TextField password;

    @FXML
    private TextField username;

    @FXML
    private Button loginBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private VBox authPanelBox;


    @FXML
    private Text mainTabLabel;

    @FXML
    private Tab chatTab;

    @FXML
    private Tab predictionTab;

    @FXML
    private Tab registerTab;

    @FXML
    private TextArea chatBotAnswerTextArea;

    @FXML
    private TextField chatBotMessageInput;

    @FXML
    private TextField registerUsername;

    @FXML
    private TextField registerPassword;

    @FXML
    private TextField registerConfirmPassword;

    @FXML
    private TextField registerEmail;

    @FXML
    private Button registerBtn;

    @FXML
    private Text chatBotCategoryText;




    private final AuthServiceImpl authService = new AuthServiceImpl();

    private final ChatBotServiceImpl chatBotService = new ChatBotServiceImpl();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //SecureStorage.clearToken();
        boolean isAuthenticated = SecureStorage.getToken() == null;
        logoutBtn.setVisible(true);
        authPanelBox.setVisible(isAuthenticated);
        chatTab.setDisable(isAuthenticated);
        predictionTab.setDisable(isAuthenticated);
        profileTab.setDisable(true);
        mainTabLabel.setText(SecureStorage.getToken());
        chatBotAnswerTextArea.setText("Sveiki! Užduokite klausimą iš Java programavimo kalbos.\n");

        //Enter simbolio paspaudimas
        chatBotMessageInput.setOnKeyPressed(this::handleKeyPress);

    }



    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            askChatBot(new ActionEvent()); // Reuse your existing handler
            chatBotMessageInput.clear();   // Clear input
        }
    }


    @FXML
    protected void askChatBot(ActionEvent event) {
        String question = chatBotMessageInput.getText();
        if (question == null || question.trim().isEmpty()) {
            chatBotAnswerTextArea.appendText("⚠ Įveskite klausimą!\n");
            return;
        }

        String token = SecureStorage.getToken(); // JWT from login
        ChatBotService chatBotService = RetrofitClient.getInstance().create(ChatBotService.class);

        chatBotService.askChatBot("Bearer " + token, question).enqueue(new Callback<ChatBotResponse>() {
            @Override
            public void onResponse(Call<ChatBotResponse> call, Response<ChatBotResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChatBotResponse botResponse = response.body();

                    chatBotAnswerTextArea.appendText("🧠 Kategorija: " + botResponse.getBestCategory() + "\n");
                    chatBotCategoryText.setText("Kategorija: " + botResponse.getBestCategory());

                    chatBotAnswerTextArea.appendText("📚 Galimos temos: " + String.join(", ", botResponse.getAllCategories()) + "\n\n");
                } else {
                    chatBotAnswerTextArea.appendText("⚠ Nepavyko gauti atsakymo. Kodas: " + response.code() + "\n");
                }
            }

            @Override
            public void onFailure(Call<ChatBotResponse> call, Throwable throwable) {
                chatBotAnswerTextArea.appendText("❌ Klaida: " + throwable.getMessage() + "\n");
            }
        });
    }



    @FXML
    void login(ActionEvent event) {

        String user = username.getText();
        String pass = password.getText();

        if (user != null && pass != null)
            authService.login(user, pass, new LoginCallback() {
                @Override
                public void onLoginSuccess(String token) {
                    authPanelBox.setVisible(false);
                    mainTabLabel.setText("Sveiki prisijungę");
                    logoutBtn.setVisible(true);
                    registerTab.setDisable(true);
                    profileTab.setDisable(false);
                    chatTab.setDisable(false);
                    predictionTab.setDisable(false);
                    loadProfile();
                }

                @Override
                public void onLoginFailure(String errorMessage) {
                    chatTab.setDisable(false);
                    predictionTab.setDisable(false);
                }
            });
    }

    @FXML
    void logout(ActionEvent event) {
        SecureStorage.clearToken();
        authPanelBox.setVisible(true);
        logoutBtn.setVisible(false);
        chatTab.setDisable(true);
        predictionTab.setDisable(true);
        profileTab.setDisable(true);
        registerTab.setDisable(false);
    }

    @FXML
    void register(ActionEvent event) {
        String usernameInput = registerUsername.getText();
        String passwordInput = registerPassword.getText();
        String confirmPassword = registerConfirmPassword.getText();
        String emailInput = registerEmail.getText();

        if (!passwordInput.equals(confirmPassword)) {
            mainTabLabel.setText("Slaptažodžiai nesutampa.");
            return;
        }

        authService.register(usernameInput, passwordInput, emailInput, new LoginCallback() {
            @Override
            public void onLoginSuccess(String token) {
                authPanelBox.setVisible(false);
                mainTabLabel.setText("Registracija sėkminga. Prisijungta.");
                logoutBtn.setVisible(true);
                chatTab.setDisable(false);
                predictionTab.setDisable(false);
            }

            @Override
            public void onLoginFailure(String errorMessage) {
                mainTabLabel.setText("Klaida registruojantis: " + errorMessage);
            }
        });
    }

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextArea bioField;

    private void loadProfile() {
        String token = SecureStorage.getToken();
        if (token == null) {
            System.out.println("No token found.");
            return;
        }

        UserService userService = RetrofitClient.getInstance().create(UserService.class);
        userService.getProfile("Bearer " + token).enqueue(new retrofit2.Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfile profile = response.body();
                    firstNameField.setText(profile.getFirstName());
                    lastNameField.setText(profile.getLastName());
                    bioField.setText(profile.getBio());
                } else {
                    System.out.println("Failed to load profile: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                System.out.println("Error loading profile: " + t.getMessage());
            }
        });
    }

    @FXML
    protected void saveProfile() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String bio = bioField.getText();

        if (firstName == null || lastName == null || bio == null) {
            System.out.println("Please fill all fields.");
            return;
        }

        UserProfile updatedProfile = new UserProfile();
        updatedProfile.setFirstName(firstName.trim());
        updatedProfile.setLastName(lastName.trim());
        updatedProfile.setBio(bio.trim());

        String token = SecureStorage.getToken();
        if (token == null || token.isEmpty()) {
            System.out.println("User not authenticated. JWT token is missing.");
            return;
        }

        UserService userService = RetrofitClient.getInstance().create(UserService.class);

        userService.updateProfile(updatedProfile, "Bearer " + token).enqueue(new Callback<Void>() {

            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    System.out.println("Profile updated successfully!");
                } else {
                    System.out.println("Failed to update profile. Status code: " + response.code());
                }
            }


            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("Error updating profile: " + t.getMessage());
            }
        });
    }


    @FXML private TextField attendanceField;
    @FXML private TextField assignmentsField;
    @FXML private TextField midtermField;
    @FXML private TextField finalExamField;
    @FXML private Text predictionResultText;

    @FXML
    protected void predictGrade() {
        try {
            int attendance = Integer.parseInt(attendanceField.getText());
            int assignments = Integer.parseInt(assignmentsField.getText());
            int midterm = Integer.parseInt(midtermField.getText());
            int finalExam = Integer.parseInt(finalExamField.getText());

            String token = SecureStorage.getToken(); // JWT token
            GradePredictionRequest request = new GradePredictionRequest(attendance, assignments, midterm, finalExam);

            System.out.println(new Gson().toJson(request)); // Debug print

            RetrofitClient.getInstance()
                    .create(UserService.class)
                    .predictGrade("Bearer " + token, request)
                    .enqueue(new Callback<Map<String, String>>() {
                        @Override
                        public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                String grade = response.body().get("predictedGrade");
                                predictionResultText.setText("Prognozuotas pažymys: " + grade);
                            } else {
                                predictionResultText.setText("Nepavyko prognozuoti. Kodas: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Map<String, String>> call, Throwable throwable) {
                            predictionResultText.setText("Klaida: " + throwable.getMessage());
                        }
                    });

        } catch (NumberFormatException e) {
            predictionResultText.setText("Prašome įvesti galiojančius skaičius.");
        }
    }


}