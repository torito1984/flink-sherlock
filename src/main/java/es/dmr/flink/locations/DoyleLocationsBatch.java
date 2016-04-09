package es.dmr.flink.locations;

import es.dmr.flink.nlp.LocationExtractor;
import es.dmr.flink.model.Place;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;

/**
 * Created by osboxes on 4/8/16.
 */
public class DoyleLocationsBatch {


    // *************************************************************************
    // PROGRAM
    // *************************************************************************

    public static void main(String[] args) throws Exception {

        // Checking input parameters
        final ParameterTool params = ParameterTool.fromArgs(args);

        // set up the execution environment
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        // make parameters available in the web interface
        env.getConfig().setGlobalJobParameters(params);

        // get input data
        DataSet<String> text;
        if (params.has("input") && params.has("output")) {
            // read the text file from given input path
            text = env.readTextFile(params.get("input"));
            text.flatMap(new LocationExtractor()) // extract locations from different episodes
                    .groupBy("place").reduce(new LocationCounter())
                    .writeAsText(params.get("output")); // group by the field place and sum up the frequency

            // execute program
            env.execute("Extraction of Locations from Sherlock");
        } else {
            System.out.println("Usage: WordCount --input <path> --output <path>");
            System.exit(-1);
        }
    }

    public static class LocationCounter implements ReduceFunction<Place> {
        @Override
        public Place reduce(Place in1, Place in2) {
            return new Place(in1.getPlace(), in1.getFrequency() + in2.getFrequency());
        }
    }
}
