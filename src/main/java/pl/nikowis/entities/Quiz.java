package pl.nikowis.entities;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Quiz database representation.
 * Created by nikowis on 2016-08-26.
 *
 * @author nikowis
 */
@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany
    @Cascade(CascadeType.ALL)
    private List<QuizAnswer> answers;

    @ManyToOne
    private User user;

    public int size() {
        return answers.size();
    }

    public QuizAnswer getAnswer(int index) {
        return answers.get(index);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<QuizAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuizAnswer> answers) {
        this.answers = answers;
    }
}
