package pl.nikowis.ui;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
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
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ProgressBarRenderer;
import com.vaadin.ui.themes.Reindeer;
import elemental.json.JsonValue;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nikowis.entities.User;
import pl.nikowis.entities.Word;
import pl.nikowis.services.SessionService;
import pl.nikowis.services.WordService;

/**
 * View for adding and browsing words.
 * Created by nikowis on 2016-08-20.
 *
 * @author nikowis
 */
@SpringView(name = WordListView.VIEW_NAME)
public class WordListView extends CustomComponent implements View {

    public static final String VIEW_NAME = "wordList";

    private WordService wordService;
    private SessionService sessionService;


    private Grid wordGrid;
    private TextField original, translated;
    private Button submitButton, homeButton, quizButton;
    private FieldGroup fieldGroup;

    private Word word;
    private User user;

    @Autowired
    public WordListView(WordService wordService, SessionService sessionService) {
        this.wordService = wordService;
        this.sessionService = sessionService;

        initializeComponents();

        setSizeFull();
        HorizontalLayout wordsForm = new HorizontalLayout(original, translated);
        wordsForm.setCaption("Add new word :");
        wordsForm.setSpacing(true);

        VerticalLayout wordsFormAndGrid = new VerticalLayout(wordsForm, submitButton, wordGrid, homeButton, quizButton);
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

        homeButton = new Button("Return home");
        homeButton.addClickListener(clickEvent -> redirect(HomeView.VIEW_NAME));

        quizButton = new Button("Start quiz");
        quizButton.addClickListener(clickEvent -> redirect(QuizView.VIEW_NAME));

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
        fieldGroup.bindMemberFields(this);

        wordGrid = new Grid("List of your words");
        initializeGridContent();
        wordGrid.setWidthUndefined();
        wordGrid.setSelectionMode(Grid.SelectionMode.NONE);
        setupGridColumns();
    }

    private void redirect(String viewName) {
        getUI().getNavigator().navigateTo(viewName);
    }

    private void setupGridColumns() {
        wordGrid.getColumn("id").setHidden(true);
        wordGrid.getColumn("user").setHidden(true);
        wordGrid.getColumn("user").setEditable(false);
        wordGrid.getColumn("delete").setRenderer(new ButtonRenderer(event -> removeWord((Word) event.getItemId())));
        wordGrid.getColumn("progress").setRenderer(new ProgressBarRenderer(){
            @Override
            public JsonValue encode(Double value) {
                if (value != null) {
                    value = value / 5;
                }
                return super.encode(value);
            }
        });
        wordGrid.setColumnOrder("original", "translated", "progress", "delete");
    }

    private void initializeGridContent() {
        BeanItemContainer<Word> wordContainer = new BeanItemContainer<Word>(Word.class);
        wordContainer.addAll(wordService.findByUserId(user.getId()));
        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(wordContainer);
        gpc.addGeneratedProperty("delete", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                return "Delete"; // The caption
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });
        wordGrid.setContainerDataSource(gpc);
    }

    private void removeWord(Word word) {
        wordService.delete(word);
        initializeGridContent();
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
            System.out.println(e.getMessage());
            return;
        }
        wordService.save(word);
        word.setId(null);
        initializeGridContent();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //empty
    }
}
