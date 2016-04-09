package es.dmr.flink.model;

/**
 * Created by osboxes on 4/8/16.
 */
/**
 * This is the POJO (Plain Old Java Object) that is being used for all the
 * operations. As long as all fields are public or have a getter/setter, the
 * system can handle them
 */
public class Word {

    private String word;
    private Integer frequency;

    public Word() {
    }

    public Word(String word, int i) {
        this.word = word;
        this.frequency = i;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "(" + word + "," + frequency + ")";
    }
}