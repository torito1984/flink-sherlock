package es.dmr.flink.wordcount.test;

import es.dmr.flink.wordcount.WordCount;
import org.apache.flink.streaming.util.StreamingProgramTestBase;
import org.apache.flink.test.testdata.WordCountData;

public class WordCountTest   extends StreamingProgramTestBase {

	protected String textPath;
	protected String resultPath;

	@Override
	protected void preSubmit() throws Exception {
		textPath = createTempFile("text.txt", WordCountData.TEXT);
		resultPath = getTempDirPath("result");
	}

	@Override
	protected void postSubmit() throws Exception {
		compareResultsByLinesInMemory(WordCountData.STREAMING_COUNTS_AS_TUPLES, resultPath);
	}

	@Override
	protected void testProgram() throws Exception {
		WordCount.main(new String[]{
				"--input", textPath,
				"--output", resultPath});
	}
}
