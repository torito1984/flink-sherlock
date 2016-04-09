package es.dmr.flink.wordcount;

import es.dmr.flink.model.Word;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;


public class WordCountPojo {
	
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
			DataStream<Word> counts = text.flatMap(new Tokenizer()) // split up the lines into Word objects
					.keyBy("word").sum("frequency"); // group by the field word and sum up the frequency
			counts.writeAsText(params.get("output"));
			// execute program
			env.execute("Streaming WordCount");
		} else {
			System.out.println("Usage: WordCount --input <path> --output <path>");
			System.exit(-1);
		}
	}

	// *************************************************************************
	// USER FUNCTIONS
	// *************************************************************************

	/**
	 * Implements the string tokenizer that splits sentences into words as a
	 * user-defined FlatMapFunction. The function takes a line (String) and
	 * splits it into multiple pairs in the form of "(word,1)" ({@code Tuple2<String,
	 * Integer>}).
	 */
	public static final class Tokenizer implements FlatMapFunction<String, Word> {
		private static final long serialVersionUID = 1L;

		@Override
		public void flatMap(String value, Collector<Word> out) {
			// normalize and split the line
			String[] tokens = value.toLowerCase().split("\\W+");

			// emit the pairs
			for (String token : tokens) {
				if (token.length() > 0) {
					out.collect(new Word(token, 1));
				}
			}
		}
	}

}
