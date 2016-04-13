package es.dmr.flink.locations;

import es.dmr.flink.nlp.LocationExtractor;
import es.dmr.flink.model.Place;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created by osboxes on 4/8/16.
 */
public class DoyleLocationsWindow {


    // *************************************************************************
    // PROGRAM
    // *************************************************************************

    public static void main(String[] args) throws Exception {

        // Checking input parameters
        final ParameterTool params = ParameterTool.fromArgs(args);

        // set up the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // make parameters available in the web interface
        env.getConfig().setGlobalJobParameters(params);

        // get input data
        DataStream<String> text;
        if (params.has("input") && params.has("output")) {
            // read the text file from given input path
            text = env.readTextFile(params.get("input"));
            DataStream<Place> counts = text.flatMap(new LocationExtractor()) // extract locations from different episodes
                    .keyBy("place")
                    .countWindow(20) // window in order to emit regularly the count (Click to see)
                    .sum("frequency"); // group by the field place and sum up the frequency
            counts.writeAsText(params.get("output"));
            // execute program
            env.execute("Extraction of Locations from Sherlock");
        } else {
            System.out.println("Usage: WordCount --input <path> --output <path>");
            System.exit(-1);
        }
    }
}
