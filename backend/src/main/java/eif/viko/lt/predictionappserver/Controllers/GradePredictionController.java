package eif.viko.lt.predictionappserver.Controllers;

import eif.viko.lt.predictionappserver.Dto.GradeHistoryDto;
import eif.viko.lt.predictionappserver.Dto.GradeRequestDto;
import eif.viko.lt.predictionappserver.Entities.ChatUser;
import eif.viko.lt.predictionappserver.Entities.GradeHistory;
import eif.viko.lt.predictionappserver.Repositories.ChatUserRepository;
import eif.viko.lt.predictionappserver.Repositories.GradeHistoryRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;

import java.io.InputStream;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/predict")
public class GradePredictionController {

    private final J48 tree;
    private final Instances data;

    public GradePredictionController() throws Exception {
        // Load the trained model from classpath
        InputStream modelStream = getClass().getResourceAsStream("/static/trained_models/grade-model.model");
        if (modelStream == null) {
            throw new Exception("Model file grade-model.model not found in resources.");
        }
        this.tree = (J48) SerializationHelper.read(modelStream);

        // Load the dataset from classpath
        InputStream csvStream = getClass().getResourceAsStream("/static/stud_grade_training_data.csv");
        if (csvStream == null) {
            throw new Exception("CSV training data not found in resources.");
        }
        CSVLoader loader = new CSVLoader();
        loader.setSource(csvStream);
        this.data = loader.getDataSet();
        this.data.setClassIndex(data.numAttributes() - 1);
    }

    @Autowired
    private GradeHistoryRepository historyRepository;

    @Autowired
    private ChatUserRepository chatUserRepository;

    @PostMapping("/grade")
    public ResponseEntity<Map<String, String>> predictGrade(@RequestBody GradeRequestDto input, Principal principal) {
        try {
            // Create instance from input
            Instance studentInstance = new DenseInstance(data.numAttributes());
            studentInstance.setValue(data.attribute("Attendance"), input.getAttendance());
            studentInstance.setValue(data.attribute("Assignments"), input.getAssignments());
            studentInstance.setValue(data.attribute("Midterm"), input.getMidterm());
            studentInstance.setValue(data.attribute("Final"), input.getFinalExam());
            studentInstance.setDataset(data);

            // Predict
            double prediction = tree.classifyInstance(studentInstance);
            String predictedGrade = data.classAttribute().value((int) prediction);

            // Get logged-in user
            ChatUser user = chatUserRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Save prediction to history
            GradeHistory history = GradeHistory.builder()
                    .attendance(input.getAttendance())
                    .assignments(input.getAssignments())
                    .midterm(input.getMidterm())
                    .finalExam(input.getFinalExam())
                    .predictedGrade(predictedGrade)
                    .timestamp(LocalDateTime.now())
                    .user(user)
                    .build();

            historyRepository.save(history);

            return ResponseEntity.ok(Map.of("predictedGrade", predictedGrade));


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }



    @GetMapping("/history")
    public ResponseEntity<List<GradeHistoryDto>> getUserHistory(Principal principal) {
        ChatUser user = chatUserRepository.findByEmail(principal.getName()).orElseThrow();
        List<GradeHistoryDto> dtoList = historyRepository.findByUser(user)
                .stream()
                .map(grade -> new GradeHistoryDto(
                        grade.getAttendance(),
                        grade.getAssignments(),
                        grade.getMidterm(),
                        grade.getFinalExam(),
                        grade.getPredictedGrade(),
                        grade.getTimestamp().toString()
                ))
                .toList();

        return ResponseEntity.ok(dtoList);
    }
}
