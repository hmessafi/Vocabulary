package pl.nikowis.services;

import pl.nikowis.entities.Word;

import java.util.List;

/**
 * Created by nikowis on 2016-08-18.
 *
 * @author nikowis
 */
public interface WordService {
    /**
     * Saves the word to the database.
     * @param word word to save.
     * @return saved word.
     */
    Word save(Word word);

    /**
     * Finds the list of user's words.
     * @param userId user id
     * @return list of words
     */
    List<Word> findByUserId(Long userId);
}
