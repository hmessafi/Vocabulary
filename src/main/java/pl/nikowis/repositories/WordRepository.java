package pl.nikowis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nikowis.entities.Word;

import java.util.List;

/**
 * Repository for words.
 * Created by nikowis on 2016-08-18.
 *
 * @author nikowis
 */
@Repository
public interface WordRepository extends JpaRepository<Word, Long>{

    List<Word> findByUserId(Long userId);
}
