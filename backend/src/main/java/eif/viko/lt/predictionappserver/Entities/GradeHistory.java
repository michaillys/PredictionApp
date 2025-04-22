package eif.viko.lt.predictionappserver.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double attendance;
    private double assignments;
    private double midterm;
    private double finalExam;
    private String predictedGrade;
    private LocalDateTime timestamp;

    @ManyToOne
    private ChatUser user;
}
