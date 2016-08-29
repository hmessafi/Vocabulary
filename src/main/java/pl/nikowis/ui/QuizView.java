package pl.nikowis.ui;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nikowis.entities.Quiz;
import pl.nikowis.entities.QuizAnswer;
import pl.nikowis.exceptions.UserHasNoWordsException;
import pl.nikowis.services.I18n;
import pl.nikowis.services.QuizService;

/**
 * Quiz view.
 * Created by nikowis on 2016-08-21.
 *
 * @author nikowis
 */
@SpringView(name = QuizView.VIEW_NAME)
public class QuizView extends CustomComponent implements View {

    public static final String VIEW_NAME = "quiz";

    private QuizService quizService;

    private I18n i18n;

    private TextField original, translated;
    private Button finish, quit, next;
    private Grid wordGrid;
    private ProgressBar progressBar;

    private int answersDoneCounter;
    private int allWordsCount;
    private QuizAnswer currentAnswer;

    private Quiz quiz;

    @Autowired
    public QuizView(QuizService quizService, I18n i18n) {
        this.quizService = quizService;
        this.i18n = i18n;

        answersDoneCounter = 0;

        quiz = quizService.createQuiz();
        allWordsCount = quiz.size();

        if (allWordsCount == 0) {
            throw new UserHasNoWordsException();
        }

        currentAnswer = quiz.getAnswer(answersDoneCounter);

        initializeComponents();

        setSizeFull();

        HorizontalLayout wordForm = new HorizontalLayout(original, translated);
        wordForm.setSpacing(true);
        wordForm.setSizeUndefined();

        VerticalLayout fields = new VerticalLayout(progressBar, wordForm, next, finish, wordGrid, quit);
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();

        VerticalLayout mainLayout = new VerticalLayout(fields);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        mainLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(mainLayout);
    }

    private void initializeComponents() {
        progressBar = new ProgressBar();
        progressBar.setCaption(i18n.getMessage("quizView.progressBar", getLocale()));
        progressBar.setValue(0.0f);
        progressBar.setWidthUndefined();
        setupGrid();

        original = new TextField();
        original.setCaption(i18n.getMessage("quizView.original", getLocale()));
        original.setEnabled(false);
        original.setValue(currentAnswer.getWord().getOriginal());
        translated = new TextField();
        translated.setCaption(i18n.getMessage("quizView.translated", getLocale()));

        finish = new Button(i18n.getMessage("quizView.finish", getLocale()));
        finish.addClickListener(clickEvent -> finishQuiz());

        quit = new Button(i18n.getMessage("quizView.quit", getLocale()));
        quit.addClickListener(clickEvent -> quitQuiz());
        next = new Button(i18n.getMessage("quizView.next", getLocale()));
        next.addClickListener(clickEvent -> goToNext());

        if (allWordsCount < 2) {
            finish.setVisible(true);
            next.setVisible(false);
        } else {
            finish.setVisible(false);
            next.setVisible(true);
        }
    }

    private void setupGrid() {
        wordGrid = new Grid(i18n.getMessage("quizView.wordGrid.title", getLocale()));
        wordGrid.setVisible(false);
        wordGrid.setSelectionMode(Grid.SelectionMode.NONE);
        BeanItemContainer<QuizAnswer> wordContainer = new BeanItemContainer<QuizAnswer>(QuizAnswer.class);
        wordContainer.addNestedContainerProperty("word.original");
        wordContainer.addNestedContainerProperty("word.translated");
        wordContainer.addAll(quiz.getAnswers());
        wordGrid.setContainerDataSource(wordContainer);
        wordGrid.getColumn("id").setHidden(true);
        wordGrid.getColumn("word").setHidden(true);
        wordGrid.getColumn("correct").setHidden(true);
        wordGrid.getColumn("userAnswer").setHeaderCaption("Your answer");
        wordGrid.setColumnOrder("word.original", "word.translated", "userAnswer");
    }

    private void goToNext() {
        progressBar.setValue(progressBar.getValue() + 1.0f / allWordsCount);
        commitAndCheckWord();
        switchToNextWord();
        if (answersDoneCounter + 1 >= allWordsCount) {
            changeToFinishButton();
        }
    }

    private void commitAndCheckWord() {
        currentAnswer.setUserAnswer(translated.getValue());
        if (translated.getValue().equals(currentAnswer.getWord().getTranslated())) {
            currentAnswer.getWord().incrementProgress();
            currentAnswer.setCorrect(true);
        }
    }

    private void switchToNextWord() {
        translated.setValue("");
        answersDoneCounter++;
        currentAnswer = quiz.getAnswer(answersDoneCounter);
        original.setValue(currentAnswer.getWord().getOriginal());
    }

    private void changeToFinishButton() {
        next.setVisible(false);
        finish.setVisible(true);
    }

    private void quitQuiz() {
        redirect(HomeView.VIEW_NAME);
    }

    private void finishQuiz() {
        progressBar.setValue(1.0f);
        commitAndCheckWord();
        quizService.save(quiz);
        hideQuizFields();
        showSummary();
    }

    private void hideQuizFields() {
        progressBar.setVisible(false);
        original.setVisible(false);
        translated.setVisible(false);
        finish.setVisible(false);
    }

    private void showSummary() {
        wordGrid.setVisible(true);
        int count = 0;
        wordGrid.setRowStyleGenerator(rowReference -> {
            if (((QuizAnswer) rowReference.getItemId()).isCorrect()) {
                return "highlight-green";
            } else {
                return "highlight-red";
            }
        });
    }

    private void redirect(String viewName) {
        getUI().getNavigator().navigateTo(viewName);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //empty
    }
}
