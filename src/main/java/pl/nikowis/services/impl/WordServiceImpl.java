package pl.nikowis.services.impl;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.nikowis.entities.Word;
import pl.nikowis.repositories.WordRepository;
import pl.nikowis.services.WordService;

import java.util.List;

/**
 * Created by nikowis on 2016-08-18.
 *
 * @author nikowis
 */
@Service
public class WordServiceImpl implements WordService {

    @Autowired
    private WordRepository repository;


    @Override
    public Word save(Word word) {
        Preconditions.checkNotNull(word);
        return repository.save(word);
    }

    @Override
    public List<Word> findByUserId(Long userId) {
        Preconditions.checkNotNull(userId);
        return repository.findByUserId(userId);
    }

    @Override
    public void delete(Word word) {
        Preconditions.checkNotNull(word);
        Preconditions.checkNotNull(word.getId());
        repository.delete(word);
    }
}
