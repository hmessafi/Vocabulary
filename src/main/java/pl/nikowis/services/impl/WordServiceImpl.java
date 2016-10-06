package pl.nikowis.services.impl;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.nikowis.entities.Word;
import pl.nikowis.repositories.WordRepository;
import pl.nikowis.services.WordService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by nikowis on 2016-08-18.
 *
 * @author nikowis
 */
@Service
public class WordServiceImpl implements WordService {

    @Autowired
    private WordRepository wordsRepository;


    @Override
    public Word save(Word word) {
        Preconditions.checkNotNull(word);
        return wordsRepository.save(word);
    }

    @Override
    public List<Word> findByUserId(Long userId) {
        Preconditions.checkNotNull(userId);
        return wordsRepository.findByUserId(userId);
    }

    @Override
    public void delete(Word word) {
        Preconditions.checkNotNull(word);
        Preconditions.checkNotNull(word.getId());
        wordsRepository.delete(word);
    }

    @Override
    public List<Word> findWorstWords(Long userId) {
        Preconditions.checkNotNull(userId);
        return wordsRepository.findTop10ByUserIdOrderByProgressAsc(userId);
    }

    @Override
    public List<Word> findBestWords(Long userId) {
        Preconditions.checkNotNull(userId);
        return wordsRepository.findTop10ByUserIdOrderByProgressDesc(userId);
    }

    @Override
    public List<Word> findLatestWords(Long userId) {
        Preconditions.checkNotNull(userId);
        return wordsRepository.findTop10ByUserIdOrderByCreateDateDesc(userId);
    }

    @Override
    public List<Word> findRandomWords(Long userId, int count) {
        Preconditions.checkNotNull(userId);
        List<Word> allWords = wordsRepository.findByUserId(userId);
        int allWordsCount;
        List<Word> selectedWords = new ArrayList<>();
        Random rand = new Random();
        while (count > 0) {
            count--;
            allWordsCount = allWords.size();
            if (allWordsCount == 1) {
                selectedWords.add(allWords.remove(0));
                break;
            }
            selectedWords.add(allWords.remove(rand.nextInt(allWordsCount-1)));
        }
        return selectedWords;
    }

    @Override
    public long count(Long userId) {
        Preconditions.checkNotNull(userId);
        return wordsRepository.countByUserId(userId);
    }

    @Override
    public Long getTotalScore(Long userId) {
        Preconditions.checkNotNull(userId);
        Long res = wordsRepository.countTotalScore(userId);
        return res == null ? new Long(0) : res;
    }
}
