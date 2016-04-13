package es.dmr.flink.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.dmr.flink.model.Place;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.util.serialization.DeserializationSchema;
import org.apache.flink.streaming.util.serialization.SerializationSchema;

import java.io.IOException;

/**
 * Created by dmartinez on 4/13/16.
 */
public class PlaceSerializer implements DeserializationSchema<Place>, SerializationSchema<Place> {
    private static final long serialVersionUID = 1L;

    ObjectMapper mapper = new ObjectMapper();

    public PlaceSerializer() {
    }

    public Place deserialize(byte[] message) {
        try {
            return mapper.readValue(new String(message), Place.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEndOfStream(Place nextElement) {
        return false;
    }

    public byte[] serialize(Place place) {
        try {
            return  mapper.writeValueAsString(place).getBytes();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TypeInformation<Place> getProducedType() {
        return TypeInformation.of(Place.class);
    }
}
