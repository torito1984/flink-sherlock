package es.dmr.flink.nlp;

/**
 * Created by osboxes on 4/9/16.
 */

import es.dmr.flink.model.Place;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

/**
 * Implements the location extractor based on English  NLP
 */
public final class LocationExtractor extends RichFlatMapFunction<String, Place> {
    private static final long serialVersionUID = 1L;
    private StanfordNLPCoreExtractor extractor;

    @Override
    public void open(Configuration parameters) throws Exception {
        extractor = new StanfordNLPCoreExtractor();
    }

    @Override
    public void flatMap(String value, Collector<Place> out) {
        // emit the locations detected
        if(!StringUtils.isEmpty(value))
            extractor.getLocations(value).stream().forEach(place -> out.collect(new Place(place, 1)));
    }
}