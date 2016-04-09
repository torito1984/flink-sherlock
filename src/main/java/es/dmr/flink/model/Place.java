package es.dmr.flink.model;

/**
 * Created by osboxes on 4/8/16.
 */
/**
 * This is the POJO (Plain Old Java Object) that is being used for all the
 * operations. As long as all fields are public or have a getter/setter, the
 * system can handle them
 */
public class Place {

    private String place;
    private Integer frequency;

    public Place() {
    }

    public Place(String word, int i) {
        this.place = word;
        this.frequency = i;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "(" + place + "," + frequency + ")";
    }
}