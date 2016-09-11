package pl.nikowis.services;

import pl.nikowis.entities.Word;

import java.util.List;

/**
 * Service for words.
 * Created by nikowis on 2016-08-18.
 *
 * @author nikowis
 */
public interface WordService {
    /**
     * Saves the word to the database.
     *
     * @param word word to save.
     * @return saved word.
     */
    Word save(Word word);

    /**
     * Finds the list of user's words.
     *
     * @param userId user userId
     * @return list of words
     */
    List<Word> findByUserId(Long userId);

    /**
     * Deletes the word from the database.
     *
     * @param word word to delete
     */
    void delete(Word word);

    /**
     * Finds the list of 10 words with least progress.
     *
     * @param userId words owner
     * @return list of words
     */
    List<Word> findWorstWords(Long userId);

    /**
     * Finds the list of 10 words with best progress.
     *
     * @param userId words owner
     * @return list of words
     */
    List<Word> findBestWords(Long userId);

    /**
     * Finds the list of 10 most recently added words.
     *
     * @param userId words owner
     * @return list of words
     */
    List<Word> findLatestWords(Long userId);

    /**
     * Finds the list of n random words.
     *
     * @param userId words owner
     * @param count number of words
     * @return list of words
     */
    List<Word> findRandomWords(Long userId, int count);

    /**
     * Counts user words.
     *
     * @param userId user
     * @return number of words
     */
    long count(Long userId);

    /**
     * Counts all the points from every user word.
     *
     * @param userId user userId
     * @return sum of points from every word
     */
    Long getTotalScore(Long userId);
}
