package pl.nikowis.ui;

import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.vaadin.data.Item;
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
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ProgressBarRenderer;
import com.vaadin.ui.themes.Reindeer;

import org.springframework.beans.factory.annotation.Autowired;

import elemental.json.JsonValue;
import pl.nikowis.entities.User;
import pl.nikowis.entities.Word;
import pl.nikowis.exceptions.EmptyFieldException;
import pl.nikowis.services.SessionService;
import pl.nikowis.services.WordService;
import pl.nikowis.ui.base.I18nCustomComponent;

/**
 * View for adding and browsing words.
 * Created by nikowis on 2016-08-20.
 *
 * @author nikowis
 */
@SpringView(name = WordListView.VIEW_NAME)
public class WordListView extends I18nCustomComponent implements View {

    public static final String VIEW_NAME = "wordList";
    private final String COL_ORIGINAL = "original";
    private final String COL_TRANSLATED = "translated";
    private final String COL_PROGRESS = "progress";
    private final String COL_DELETE = "delete";

    @Autowired
    private WordService wordService;
    @Autowired
    private SessionService sessionService;

    private Grid words;
    private TextField original, translated;
    private Button submit, home, quiz;
    private FieldGroup fieldGroup;

    private Word word;
    private User user;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        initializeComponents();

        setSizeFull();
        HorizontalLayout addWordForm = new HorizontalLayout(original, translated);
        addWordForm.setCaption(getMessage("wordListView.addWordForm.title"));
        addWordForm.setSpacing(true);

        VerticalLayout wordsFormAndGrid = new VerticalLayout(addWordForm, submit, words, home, quiz);
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

        home = new Button(getMessage("wordListView.home"));
        home.addClickListener(clickEvent -> redirect(HomeView.VIEW_NAME));

        quiz = new Button(getMessage("wordListView.quiz"));
        quiz.addClickListener(clickEvent -> redirect(QuizView.VIEW_NAME));

        original = new TextField(getMessage("wordListView.original"));
        original.setValidationVisible(false);
        original.addValidator(o -> checkNotEmpty((String) o));
        original.setNullRepresentation("");
        translated = new TextField(getMessage("wordListView.translated"));
        translated.setValidationVisible(false);
        translated.addValidator(o -> checkNotEmpty((String) o));
        translated.setNullRepresentation("");
        submit = new Button(getMessage("wordListView.submit"));
        submit.addClickListener(clickEvent -> commitFieldGroup());

        BeanItem<Word> bean = new BeanItem<Word>(word);
        fieldGroup = new FieldGroup(bean);
        fieldGroup.bindMemberFields(this);

        words = new Grid(getMessage("wordListView.words.title"));
        initializeGridContent();
        words.setWidthUndefined();
        words.setSelectionMode(Grid.SelectionMode.NONE);
        setupGridColumns();
    }

    private void redirect(String viewName) {
        getUI().getNavigator().navigateTo(viewName);
    }

    private void setupGridColumns() {
        words.getColumn(COL_DELETE).setRenderer(new ButtonRenderer(event -> removeWord((Word) event.getItemId())));
        words.getColumn(COL_PROGRESS).setRenderer(new ProgressBarRenderer() {
            @Override
            public JsonValue encode(Double value) {
                if (value != null) {
                    value = value / 5;
                }
                return super.encode(value);
            }
        });
        words.setColumns(
                COL_ORIGINAL
                , COL_TRANSLATED
                , COL_PROGRESS
                , COL_DELETE
        );
    }

    private void initializeGridContent() {
        BeanItemContainer<Word> wordContainer = new BeanItemContainer<Word>(Word.class);
        wordContainer.addAll(wordService.findByUserId(user.getId()));
        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(wordContainer);
        gpc.addGeneratedProperty(COL_DELETE, new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                return getMessage("wordListView.words.delete"); // The caption
            }

            @Override
            public Class<String> getType() {
                return String.class;
            }
        });
        words.setContainerDataSource(gpc);
    }

    private void removeWord(Word word) {
        wordService.delete(word);
        initializeGridContent();
    }

    private void checkNotEmpty(String o) {
        if (Strings.isNullOrEmpty(o)) {
            throw new EmptyFieldException();
        }
    }

    private void commitFieldGroup() {
        original.setValidationVisible(true);
        translated.setValidationVisible(true);
        try {
            fieldGroup.commit();
        } catch (FieldGroup.CommitException e) {
            System.out.println(e.getMessage());
            return;
        }
        wordService.save(word);
        word.setId(null);
        initializeGridContent();
        original.setValidationVisible(false);
        translated.setValidationVisible(false);
    }
}
