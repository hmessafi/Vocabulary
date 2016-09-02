package pl.nikowis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    List<Word> findTop10ByUserIdOrderByProgressAsc(Long userId);

    @Query("select count(w) from Word w where w.user.id= :userId")
    long countByUserId(@Param("userId")Long userId);

    @Query("select sum(w.progress) from Word w where w.user.id= :userId")
    Long countTotalScore(@Param("userId")Long userId);
}
