package eif.viko.lt.predictionappserver.Repositories;

import eif.viko.lt.predictionappserver.Entities.GradeHistory;
import eif.viko.lt.predictionappserver.Entities.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeHistoryRepository extends JpaRepository<GradeHistory, Long> {
    List<GradeHistory> findByUser(ChatUser user);
}
