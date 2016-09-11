package pl.nikowis.services;

import pl.nikowis.entities.Quiz;
import pl.nikowis.entities.Word;

import java.util.List;

/**
 * Service for quizes.
 * Created by nikowis on 2016-08-26.
 *
 * @author nikowis
 */
public interface QuizService {
    /**
     * Creates and fills up a new Quiz instance.
     * @return quiz
     * @param wordList list of words to use
     */
    Quiz createQuiz(List<Word> wordList);

    /**
     * Saves a quiz.
     * @return saved quiz
     */
    Quiz save(Quiz quiz);

    /**
     * Counts user quizes.
     * @param userId user
     * @return number of quizes
     */
    long count(Long userId);

}
