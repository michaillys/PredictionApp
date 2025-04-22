package eif.viko.lt.predictionappserver.Controllers;


import eif.viko.lt.predictionappserver.Entities.ChatHistory;
import eif.viko.lt.predictionappserver.Repositories.ChatHistoryRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping("/chatbot")
public class ChatBotController {
    private DoccatModel model;

    public ChatBotController() throws IOException {
//        InputStream customModel = new FileInputStream("src/main/resources/static/trained_models/chatbot-model.bin");
        InputStream customModel = getClass().getResourceAsStream("/static/trained_models/chatbot-model.bin");
        if (customModel == null) {
            throw new IOException("chatbot-model.bin not found in resources!");
        }
        model = new DoccatModel(customModel);
    }

    // DTO
    @Getter
    @AllArgsConstructor
    public static class CategorizationResponse {
        private String bestCategory;
        private String[] allCategories;
    }

    @Autowired
    private ChatHistoryRepository chatHistoryRepository;

    @GetMapping("/ask")
    public ResponseEntity<CategorizationResponse> ask(@RequestParam String question) {
        // Save the question to chat history
        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setMessage(question);
        chatHistoryRepository.save(chatHistory);

        DocumentCategorizerME categorizer = new DocumentCategorizerME(model);
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(question);
        double[] outcomes = categorizer.categorize(tokens);

        // Get all categories
        String[] categories = new String[outcomes.length];
        for (int i = 0; i < outcomes.length; i++) {
            categories[i] = categorizer.getCategory(i);
        }

        // Wrap in DTO
        CategorizationResponse response = new CategorizationResponse(categorizer.getBestCategory(outcomes), categories);
        return ResponseEntity.ok(response);
    }


}



