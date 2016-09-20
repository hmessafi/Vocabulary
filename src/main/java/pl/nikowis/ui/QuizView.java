package pl.nikowis.ui;



import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nikowis.entities.Word;
import pl.nikowis.services.SessionService;
import pl.nikowis.services.WordService;
import pl.nikowis.ui.base.I18nCustomComponent;

import java.util.List;

/**
 * Quiz view.
 * Created by nikowis on 2016-09-10.
 *
 * @author nikowis
 */
@SpringView(name = QuizView.VIEW_NAME)
public class QuizView extends I18nCustomComponent implements View {

    public static final String VIEW_NAME = "quiz";
    private final int RANDOM_QUIZ_WORD_COUNT = 10;

    @Autowired
    private WordService wordService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private QuizForm quizForm;

    private Button worstQuiz, bestQuiz, latestQuiz, randomQuiz;

    private VerticalLayout mainLayout, buttons;
    private List<Word> wordList;
    private Long currentUserId;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        initializeComponents();
        currentUserId = sessionService.getUser().getId();
        buttons = new VerticalLayout(
                worstQuiz
                , bestQuiz
                , latestQuiz
                , randomQuiz
        );

        buttons.setCaption(getMessage("quizView.title"));
        buttons.setSpacing(true);
        buttons.setMargin(new MarginInfo(true, true, true, false));
        buttons.setSizeUndefined();

        mainLayout = new VerticalLayout(buttons);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
        mainLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(mainLayout);
    }

    private void initializeComponents() {
        worstQuiz = new Button(getMessage("quizView.worstQuiz"), FontAwesome.EXCLAMATION_TRIANGLE);
        worstQuiz.addClickListener(clickEvent -> worstQuiz());
        bestQuiz = new Button(getMessage("quizView.bestQuiz"), FontAwesome.CHECK_SQUARE_O);
        bestQuiz.addClickListener(clickEvent -> bestQuiz());
        latestQuiz = new Button(getMessage("quizView.latestQuiz"), FontAwesome.CLOCK_O);
        latestQuiz.addClickListener(clickEvent -> latestQuiz());
        randomQuiz = new Button(getMessage("quizView.randomQuiz"), FontAwesome.RANDOM);
        randomQuiz.addClickListener(clickEvent -> randomQuiz());
    }

    private void randomQuiz() {
        wordList = wordService.findRandomWords(currentUserId, RANDOM_QUIZ_WORD_COUNT);
        initializeQuiz(wordList);
    }

    private void latestQuiz() {
        wordList = wordService.findLatestWords(currentUserId);
        initializeQuiz(wordList);
    }

    private void bestQuiz() {
        wordList = wordService.findBestWords(currentUserId);
        initializeQuiz(wordList);
    }

    private void worstQuiz() {
        wordList = wordService.findWorstWords(currentUserId);
        initializeQuiz(wordList);
    }

    private void initializeQuiz(List<Word> words) {
        quizForm.initializeForm(words);
        buttons.setVisible(false);
        mainLayout.addComponent(quizForm);
    }
}
