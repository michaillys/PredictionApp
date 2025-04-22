package eif.viko.lt.predictionappserver.TrainModels;

import opennlp.tools.doccat.*;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class TrainChatBot {
    public static void main(String[] args) {
        try {

            InputStream inputFile = TrainChatBot.class.getResourceAsStream("/static/chat_training_data.txt");
            if (inputFile == null) {
                throw new FileNotFoundException("Training file not found in /static/chat_training_data.txt");
            }


            ObjectStream<String> lineStream = new PlainTextByLineStream(() -> inputFile, "UTF-8");
            ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

            DoccatFactory factory = new DoccatFactory();
            TrainingParameters params = new TrainingParameters();
            params.put(TrainingParameters.ITERATIONS_PARAM, 100);
            params.put(TrainingParameters.CUTOFF_PARAM, 1);

            DoccatModel model = DocumentCategorizerME.train("lt", sampleStream, params, factory);

            // Trained model output path
            String outputDir = Paths.get("backend", "src", "main", "resources", "static", "trained_models").toString();

            Files.createDirectories(Paths.get(outputDir));

            try (OutputStream out = new FileOutputStream(outputDir + "/chatbot-model.bin")) {
                model.serialize(out);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Saving model to: " + outputDir);

            System.out.println("Model training complete!");

        } catch (Exception e) {
            System.err.println("Training failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
