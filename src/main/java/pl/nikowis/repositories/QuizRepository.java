package pl.nikowis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.entities.Quiz;

/**
 * Repository for quizes.
 * Created by nikowis on 2016-08-26.
 *
 * @author nikowis
 */
@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
