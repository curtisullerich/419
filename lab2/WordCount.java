import java.io.*;
import java.lang.*;
import java.util.*;
import java.net.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat; 
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat; 
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat; 
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

// A simple Hadoop MapReduce program to count the number of occurence of all distinct words
// Learn more about Hadoop at: http://hadoop.apache.org/docs/r1.0.0/api/index.html


// The MapReduce WordCount program using the Tool interface
// The tool interface helps in handling of generic command line options
// If you provide any option that is specific for Hadoop,
// this would be read by the system and discarded.
// So your program need not process the generic MapReduce command line options
// Your program can directly use the specific command line arguments for your program
// and ignore the ones for the MapReduce framework
// Read more about the Tool Interface in : 
// http://hadoop.apache.org/docs/r1.0.0/api/org/apache/hadoop/util/Tool.html

public class WordCount extends Configured implements Tool {
	
	public static void main(String[] args) throws Exception {
		
        // Process all MapReduce options if any and then run the MapReduce program
		int res = ToolRunner.run(new Configuration(), new WordCount(), args);
		System.exit(res); 
	}
	
	public int run ( String[] args ) throws Exception {
		
		int reduce_tasks = 2;
        
        // Get system configuration
		Configuration conf = new Configuration();
		
        // Create a Hadoop Job
		Job job = new Job(conf, "wordcount using MapReduce"); 
        
        // Attach the job to this Class
		job.setJarByClass(WordCount.class); 
        
        // Number of reducers
		job.setNumReduceTasks(reduce_tasks);
        
        // Set the Output Key from the reducer
        // Must match with what the reducer outputs
        // using context.write()
		job.setOutputKeyClass(Text.class); 
        
        // Set the Output Value from the reducer
        // Must match with what the reducer outputs
        // using context.write()
		job.setOutputValueClass(IntWritable.class);
        
        // Set the Map class
		job.setMapperClass(Map.class); 
        
        // Set the Combiner class
        // The combiner class reduces the mapper output locally
        // All the outputs from the mapper having the same key are reduced locally
        // This helps in reducing communication time as reducers get only
        // one tuple per key per mapper
        // For this example, the Reduce logic is good enough as the combiner logic
        // Hence we use the same class. 
        // However, this is not neccessary and you can write separate Combiner class
        job.setCombinerClass(Reduce.class);
        
        // Set the reducer class
		job.setReducerClass(Reduce.class);
        
        // Set how the input is split
        // TextInputFormat.class splits the data per line
		job.setInputFormatClass(TextInputFormat.class); 
        
        // Output format class
		job.setOutputFormatClass(TextOutputFormat.class);
        
        // Input path
		FileInputFormat.addInputPath(job, new Path(args[0])); 
        
        // Output path
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        // Run the job
		job.waitForCompletion(true);
		
		return 0;
	} 
	
    // The Map Class
	// The input to the map method would be a LongWritable (long) key and Text (String) value
	// Notice the class declaration is done with LongWritable key and Text value
	// The TextInputFormat splits the data line by line.
	// The key for TextInputFormat is nothing but the line number and hence can be ignored
	// The value for the TextInputFormat is a line of text from the input
	// The map method can emit data using context.write() method
	// However, to match the class declaration, it must emit Text as key and IntWribale as value
	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable>  {
		
		private IntWritable one = new IntWritable(1);
		private Text word = new Text();
		
        // The map method 
		public void map(LongWritable key, Text value, Context context)
							throws IOException, InterruptedException  {
		
            // The TextInputFormat splits the data line by line.
            // So each map method receives one line from the input
			String line = value.toString();
                                
            // Tokenize to get the individual words
			StringTokenizer tokens = new StringTokenizer(line);
			
			while (tokens.hasMoreTokens()) {
				
				word.set(tokens.nextToken()); 
                
                context.write(word, one);
			} 		
		} 	
	} 
	
    // The reduce class
	// The key is Text and must match the datatype of the output key of the map method
	// The value is IntWritable and also must match the datatype of the output value of the map method
	public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable>  {
		
        // The reduce method
		// For key, we have an Iterable over all values associated with this key
		// The values come in a sorted fasion.
		public void reduce(Text key, Iterable<IntWritable> values, Context context) 
								throws IOException, InterruptedException  {
			int sum = 0;
			
			for (IntWritable val : values) {
				
				sum += val.get(); 
			}
			
			context.write(key, new IntWritable(sum));
		} 
		
	}
		
}