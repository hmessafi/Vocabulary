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
import pl.nikowis.entities.User;
import pl.nikowis.entities.Word;
import pl.nikowis.exceptions.UserHasNoWordsException;
import pl.nikowis.services.SessionService;
import pl.nikowis.services.WordService;

import java.util.List;

/**
 * Quiz view.
 * Created by nikowis on 2016-08-21.
 *
 * @author nikowis
 */
@SpringView(name = QuizView.VIEW_NAME)
public class QuizView extends CustomComponent implements View {

    public static final String VIEW_NAME = "quiz";

    private SessionService sessionService;
    private WordService wordService;

    private TextField original, translated;
    private Button finish, quit, next;
    private Grid wordGrid;
    private ProgressBar progressBar;

    private User user;
    private List<Word> wordList;
    private int wordsDoneCounter;
    private int allWordsCount;
    private Word currentWord;
    private boolean correctWords[];

    @Autowired
    public QuizView(SessionService sessionService, WordService wordService) {
        this.sessionService = sessionService;
        this.wordService = wordService;
        user = sessionService.getUser();
        wordList = wordService.findWorstWords(user.getId());
        allWordsCount = wordList.size();
        correctWords = new boolean[allWordsCount];

        if (allWordsCount == 0) {
            throw new UserHasNoWordsException();
        }

        currentWord = wordList.get(wordsDoneCounter);

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
        progressBar.setCaption("Quiz progress");
        progressBar.setValue(0.0f);
        progressBar.setWidthUndefined();
        setupGrid();

        original = new TextField();
        original.setCaption("Original");
        original.setEnabled(false);
        original.setValue(currentWord.getOriginal());
        translated = new TextField();
        translated.setCaption("translated");

        finish = new Button("Finish quiz.");
        finish.addClickListener(clickEvent -> finishQuiz());

        quit = new Button("Quit quiz");
        quit.addClickListener(clickEvent -> quitQuiz());
        next = new Button("Go to next word");
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
        wordGrid = new Grid("Quiz Summary");
        wordGrid.setVisible(false);
        BeanItemContainer<Word> wordContainer = new BeanItemContainer<Word>(Word.class);
        wordContainer.addAll(wordList);
        wordGrid.setContainerDataSource(wordContainer);
        wordGrid.getColumn("id").setHidden(true);
        wordGrid.getColumn("user").setHidden(true);
        wordGrid.getColumn("progress").setHidden(true);
    }

    private void goToNext() {
        progressBar.setValue(progressBar.getValue() + 1.0f / allWordsCount);
        commitAndCheckWord();
        switchToNextWord();
        if (wordsDoneCounter + 1 >= allWordsCount) {
            changeToFinishButton();
        }
    }

    private void commitAndCheckWord() {
        if (translated.getValue().equals(currentWord.getTranslated())) {
            currentWord.incrementProgress();
            wordService.save(currentWord);
            correctWords[wordsDoneCounter] = true;
        }
    }

    private void switchToNextWord() {
        translated.setValue("");
        wordsDoneCounter++;
        currentWord = wordList.get(wordsDoneCounter);
        original.setValue(currentWord.getOriginal());
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
        hideQuizFields();
        createSummary();
    }

    private void hideQuizFields() {
        progressBar.setVisible(false);
        original.setVisible(false);
        translated.setVisible(false);
        finish.setVisible(false);
    }

    private void createSummary() {
        wordGrid.setVisible(true);
    }

    private void redirect(String viewName) {
        getUI().getNavigator().navigateTo(viewName);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //empty
    }
}
