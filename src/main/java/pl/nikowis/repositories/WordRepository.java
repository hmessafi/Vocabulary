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

    /**
     * Finds all user words.
     * @param userId user id
     * @return list of words
     */
    List<Word> findByUserId(Long userId);

    /**
     * Finds 10 worst progressed words by user id.
     * @param userId id of the user
     * @return list of words
     */
    List<Word> findTop10ByUserIdOrderByProgressAsc(Long userId);

    /**
     * Finds 10 best progressed words by user id.
     * @param userId id of the user
     * @return list of words
     */
    List<Word> findTop10ByUserIdOrderByProgressDesc(Long userId);

    /**
     * Finds 10 latest added user words.
     * @param userId user id
     * @return list of words
     */
    List<Word> findTop10ByUserIdOrderByCreateDateDesc(Long userId);

    /**
     * Counts the number of user words.
     * @param userId user id
     * @return total number of words
     */
    @Query("select count(w) from Word w where w.user.id= :userId")
    long countByUserId(@Param("userId")Long userId);


    /**
     * Sums the progress from every user word.
     * @param userId user id
     * @return sum of all user word progress
     */
    @Query("select sum(w.progress) from Word w where w.user.id= :userId")
    Long countTotalScore(@Param("userId")Long userId);
}
