package pl.nikowis.ui;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nikowis.entities.User;
import pl.nikowis.entities.Word;
import pl.nikowis.services.SessionService;
import pl.nikowis.services.WordService;

/**
 * Created by nikowis on 2016-08-20.
 *
 * @author nikowis
 */
@SpringView(name = WordListView.VIEW_NAME)
public class WordListView extends CustomComponent implements View {

    public static final String VIEW_NAME = "wordList";

    private WordService wordService;
    private SessionService sessionService;


    private Grid wordsGrid;
    private TextField original, translated;
    private Button submitButton;
    private FieldGroup fieldGroup;

    Word word;
    User user;

    @Autowired
    public WordListView(WordService wordService, SessionService sessionService) {
        this.wordService = wordService;
        this.sessionService = sessionService;

        initializeComponents();

        setSizeFull();
        HorizontalLayout wordsForm = new HorizontalLayout(original, translated);
        wordsForm.setCaption("Add new word :");
        wordsForm.setSpacing(true);

        VerticalLayout wordsFormAndGrid = new VerticalLayout(wordsForm, submitButton, wordsGrid);
        wordsFormAndGrid.setSpacing(true);
        wordsFormAndGrid.setMargin(new MarginInfo(true, true, true, false));
        wordsFormAndGrid.setSizeUndefined();

        VerticalLayout mainLayout = new VerticalLayout(wordsFormAndGrid);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(wordsFormAndGrid, Alignment.MIDDLE_CENTER);
        mainLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        setCompositionRoot(mainLayout);
    }

    private void initializeComponents() {

        word = new Word();
        user = sessionService.getUser();
        word.setUser(user);
        original = new TextField("Original");
        original.addValidator(o -> checkNotEmpty((String) o));
        original.setNullRepresentation("");
        translated = new TextField("Translated");
        translated.addValidator(o -> checkNotEmpty((String) o));
        translated.setNullRepresentation("");
        submitButton = new Button("Add word");
        submitButton.addClickListener(clickEvent -> commitFieldGroup());

        BeanItem<Word> bean = new BeanItem<Word>(word);
        fieldGroup = new FieldGroup(bean);
        fieldGroup.addCommitHandler(new WordCommitHandler());
        fieldGroup.bindMemberFields(this);
        wordsGrid = new Grid("List of your words");
        wordsGrid.setSelectionMode(Grid.SelectionMode.NONE);
        populateGrid();
        wordsGrid.removeColumn("id");
        wordsGrid.removeColumn("user");
    }

    private void checkNotEmpty(String o) {
        if (Strings.isNullOrEmpty(o)) {
            throw new Validator.InvalidValueException("Field must not be empty");
        }
    }

    private void commitFieldGroup() {
        try {
            fieldGroup.commit();
        } catch (FieldGroup.CommitException e) {
            e.printStackTrace();
        }
    }

    private void populateGrid() {
        BeanItemContainer<Word> wordContainer = new BeanItemContainer<Word>(Word.class);
        wordContainer.addAll(wordService.findByUserId(user.getId()));
        wordsGrid.setContainerDataSource(wordContainer);
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //empty
    }

    private class WordCommitHandler implements FieldGroup.CommitHandler {
        @Override
        public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
            //empty
        }

        @Override
        public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
            wordService.save(word);
            word.setId(null);
            populateGrid();
        }
    }
}
