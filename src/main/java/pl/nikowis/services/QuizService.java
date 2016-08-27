package pl.nikowis.services;

import pl.nikowis.entities.Quiz;

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
     */
    Quiz createQuiz();

    /**
     * Saves a quiz.
     * @return saved quiz
     */
    Quiz save(Quiz quiz);

}
