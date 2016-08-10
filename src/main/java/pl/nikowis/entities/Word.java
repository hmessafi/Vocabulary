package pl.nikowis.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by nikowis on 2016-07-27.
 */
@Entity
public class Word {

    @Id
    private Long id;

    @NotNull
    private String original;

    @NotNull
    private String translated;

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getTranslated() {
        return translated;
    }

    public void setTranslated(String translated) {
        this.translated = translated;
    }
}
