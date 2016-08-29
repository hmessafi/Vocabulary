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
import pl.nikowis.services.I18n;
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
    private I18n i18n;

    private Grid words;
    private TextField original, translated;
    private Button submit, home, quiz;
    private FieldGroup fieldGroup;

    private Word word;
    private User user;

    @Autowired
    public WordListView(WordService wordService, SessionService sessionService, I18n i18n) {
        this.wordService = wordService;
        this.sessionService = sessionService;
        this.i18n = i18n;

        initializeComponents();

        setSizeFull();
        HorizontalLayout addWordForm = new HorizontalLayout(original, translated);
        addWordForm.setCaption(i18n.getMessage("wordListView.addWordForm.title", getLocale()));
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

        home = new Button(i18n.getMessage("wordListView.home", getLocale()));
        home.addClickListener(clickEvent -> redirect(HomeView.VIEW_NAME));

        quiz = new Button(i18n.getMessage("wordListView.quiz", getLocale()));
        quiz.addClickListener(clickEvent -> redirect(QuizView.VIEW_NAME));

        original = new TextField(i18n.getMessage("wordListView.original", getLocale()));
        original.setValidationVisible(false);
        original.addValidator(o -> checkNotEmpty((String) o));
        original.setNullRepresentation("");
        translated = new TextField(i18n.getMessage("wordListView.translated", getLocale()));
        translated.setValidationVisible(false);
        translated.addValidator(o -> checkNotEmpty((String) o));
        translated.setNullRepresentation("");
        submit = new Button(i18n.getMessage("wordListView.submit", getLocale()));
        submit.addClickListener(clickEvent -> commitFieldGroup());

        BeanItem<Word> bean = new BeanItem<Word>(word);
        fieldGroup = new FieldGroup(bean);
        fieldGroup.bindMemberFields(this);

        words = new Grid(i18n.getMessage("wordListView.words.title", getLocale()));
        initializeGridContent();
        words.setWidthUndefined();
        words.setSelectionMode(Grid.SelectionMode.NONE);
        setupGridColumns();
    }

    private void redirect(String viewName) {
        getUI().getNavigator().navigateTo(viewName);
    }

    private void setupGridColumns() {
        words.getColumn("id").setHidden(true);
        words.getColumn("user").setHidden(true);
        words.getColumn("user").setEditable(false);
        words.getColumn("delete").setRenderer(new ButtonRenderer(event -> removeWord((Word) event.getItemId())));
        words.getColumn("progress").setRenderer(new ProgressBarRenderer(){
            @Override
            public JsonValue encode(Double value) {
                if (value != null) {
                    value = value / 5;
                }
                return super.encode(value);
            }
        });
        words.setColumnOrder("original", "translated", "progress", "delete");
    }

    private void initializeGridContent() {
        BeanItemContainer<Word> wordContainer = new BeanItemContainer<Word>(Word.class);
        wordContainer.addAll(wordService.findByUserId(user.getId()));
        GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(wordContainer);
        gpc.addGeneratedProperty("delete", new PropertyValueGenerator<String>() {
            @Override
            public String getValue(Item item, Object itemId, Object propertyId) {
                return i18n.getMessage("wordListView.words.delete", getLocale()); // The caption
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
            throw new Validator.InvalidValueException("Field must not be empty");
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

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        //empty
    }
}
