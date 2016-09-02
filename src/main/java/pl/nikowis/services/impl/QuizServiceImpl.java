package pl.nikowis.services.impl;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.nikowis.entities.Quiz;
import pl.nikowis.entities.QuizAnswer;
import pl.nikowis.entities.Word;
import pl.nikowis.repositories.QuizRepository;
import pl.nikowis.services.QuizService;
import pl.nikowis.services.SessionService;
import pl.nikowis.services.WordService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikowis on 2016-08-26.
 *
 * @author nikowis
 */
@Service
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private WordService wordService;

    @Override
    public Quiz createQuiz() {
        Quiz quiz = new Quiz();
        quiz.setUser(sessionService.getUser());
        quiz.setAnswers(createQuizAnswers());
        return quiz;
    }

    private List<QuizAnswer> createQuizAnswers() {
        List<QuizAnswer> answers = new ArrayList<>();
        List<Word> words = wordService.findWorstWords(sessionService.getUser().getId());
        for(Word w : words) {
            answers.add(new QuizAnswer(w));
        }
        return answers;
    }

    @Override
    public Quiz save(Quiz quiz) {
        Preconditions.checkNotNull(quiz);
        return quizRepository.save(quiz);
    }

    @Override
    public long count(Long userId) {
        Preconditions.checkNotNull(userId);
        return quizRepository.count();
    }
}
