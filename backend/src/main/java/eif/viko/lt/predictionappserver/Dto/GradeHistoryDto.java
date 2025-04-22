package eif.viko.lt.predictionappserver.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeHistoryDto {
    private double attendance;
    private double assignments;
    private double midterm;
    private double finalExam;
    private String predictedGrade;
    private String timestamp;
}
