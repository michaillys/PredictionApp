package eif.viko.lt.predictionappserver.Dto;

import lombok.Data;

@Data
public class GradeRequestDto {
    private double attendance;
    private double assignments;
    private double midterm;
    private double finalExam;
}
