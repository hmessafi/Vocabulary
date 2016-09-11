package pl.nikowis.services.impl;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.nikowis.entities.Quiz;
import pl.nikowis.entities.QuizAnswer;
import pl.nikowis.entities.Word;
import pl.nikowis.repositories.QuizRepository;
import pl.nikowis.repositories.WordRepository;
import pl.nikowis.services.QuizService;
import pl.nikowis.services.SessionService;

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
    private WordRepository wordRepository;

    @Autowired
    private SessionService sessionService;

    @Override
    public Quiz createQuiz(List<Word> wordList) {
        Preconditions.checkNotNull(wordList);
        Quiz quiz = new Quiz();
        quiz.setUser(sessionService.getUser());
        quiz.setAnswers(createQuizAnswers(wordList));
        return quiz;
    }

    private List<QuizAnswer> createQuizAnswers(List<Word> wordList) {
        List<QuizAnswer> answers = new ArrayList<>();
        for(Word w : wordList) {
            answers.add(new QuizAnswer(w));
        }
        return answers;
    }

    @Override
    public Quiz save(Quiz quiz) {
        Preconditions.checkNotNull(quiz);
        for(QuizAnswer answer : quiz.getAnswers()) {
            wordRepository.save(answer.getWord());
        }
        return quizRepository.save(quiz);
    }

    @Override
    public long count(Long userId) {
        Preconditions.checkNotNull(userId);
        return quizRepository.count();
    }
}
