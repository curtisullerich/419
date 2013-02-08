/**
  *****************************************
  *****************************************
  * Cpr E 419 - Lab 2 *********************
  * For question regarding this code,
  * please contact:
  * Srikanta Tirthapura (snt@iastate.edu)
  * Arko Provo Mukherjee (arko@iastate.edu)
  *****************************************
  *****************************************
  */

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


/*
 * @author Curtis Ullerich <curtisu@iastate.edu>
 */
public class Bigrams extends Configured implements Tool {
	
	public static void main ( String[] args ) throws Exception {
		
		int res = ToolRunner.run(new Configuration(), new Bigrams(), args);
		System.exit(res); 
		
	} // End main
	
	public int run ( String[] args ) throws Exception {
		
		String input = "/datasets/Lab2/Shakespeare";    // Change this accordingly
		String temp = "/users/curtisu/lab2/exp2.32/tmp";  // Change this accordingly
		String output = "/users/curtisu/lab2/output/exp2.32/";  // Change this accordingly
		
		int reduce_tasks = 2;  // The number of reduce tasks that will be assigned to the job
		Configuration conf = new Configuration();
		
		// Create job for round 1
		
		// Create the job
		Job job_one = new Job(conf, "Bigrams Program Round One"); 
		
		// Attach the job to this Bigrams
		job_one.setJarByClass(Bigrams.class); 
		
		// Fix the number of reduce tasks to run
		// If not provided, the system decides on its own
		job_one.setNumReduceTasks(reduce_tasks);
		
		// The datatype of the Output Key 
		// Must match with the declaration of the Reducer Class
		job_one.setOutputKeyClass(Text.class); 
		
		// The datatype of the Output Value 
		// Must match with the declaration of the Reducer Class
		job_one.setOutputValueClass(IntWritable.class);
		
		// The class that provides the map method
		job_one.setMapperClass(Map_One.class); 
		
		// The class that provides the reduce method
		job_one.setReducerClass(Reduce_One.class);
		
		// Decides how the input will be split
		// We are using TextInputFormat which splits the data line by line
		// This means wach map method recieves one line as an input
		job_one.setInputFormatClass(TextInputFormat.class);  
		
		// Decides the Output Format
		job_one.setOutputFormatClass(TextOutputFormat.class);
		
		// The input HDFS path for this job
		// The path can be a directory containing several files
		// You can add multiple input paths including multiple directories
		FileInputFormat.addInputPath(job_one, new Path(input)); 
		// FileInputFormat.addInputPath(job_one, new Path(another_input_path)); // This is legal
		
		// The output HDFS path for this job
		// The output path must be one and only one
		// This must not be shared with other running jobs in the system
		FileOutputFormat.setOutputPath(job_one, new Path(temp));
		// FileOutputFormat.setOutputPath(job_one, new Path(another_output_path)); // This is not allowed
		
		// Run the job
		job_one.waitForCompletion(true); 
		
		
		// Create job for round 2
		// The output of the previous job can be passed as the input to the next
		// The steps are as in job 1
		
		Job job_two = new Job(conf, "Bigrams Program Round Two"); 
		job_two.setJarByClass(Bigrams.class); 
		job_two.setNumReduceTasks(reduce_tasks); 
		
		job_two.setOutputKeyClass(Text.class); 
		job_two.setOutputValueClass(Text.class);
		
		// If required the same Map / Reduce classes can also be used
		// Will depend on logic if separate Map / Reduce classes are needed
		// Here we show separate ones
		job_two.setMapperClass(Map_Two.class); 
		job_two.setReducerClass(Reduce_Two.class);
		
		job_two.setInputFormatClass(TextInputFormat.class); 
		job_two.setOutputFormatClass(TextOutputFormat.class);
		
		// The output of previous job set as input of the next
		FileInputFormat.addInputPath(job_two, new Path(temp)); 
		FileOutputFormat.setOutputPath(job_two, new Path(output));
		
		// Run the job
		job_two.waitForCompletion(true); 
		
		return 0;
		
	} // End run

	
	// The Map Class
	// The input to the map method would be a LongWritable (long) key and Text (String) value
	// Notice the class declaration is done with LongWritable key and Text value
	// The TextInputFormat splits the data line by line.
	// The key for TextInputFormat is nothing but the line number and hence can be ignored
	// The value for the TextInputFormat is a line of text from the input
	// The map method can emit data using context.write() method
	// However, to match the class declaration, it must emit Text as key and IntWritable as value
	public static class Map_One extends Mapper<LongWritable, Text, Text, IntWritable>  {

    //constants used to avoid extraneous object construction
    private final IntWritable one = new IntWritable(1);
	  private final Text bigram = new Text();
		
		// The map method
		public void map(LongWritable key, Text value, Context context) 
								throws IOException, InterruptedException  {
			
			// The TextInputFormat splits the data line by line.
			// So each map method receives one line from the input
			String line = value.toString();

      //use this as a placeholder for {? . !}
		  String del = " SENTENCEDELIMITER ";

      //the previous token    
      String prev = null;
  
      //break up the input with a tokenizer
      //so we don't exceed memory using replaceAll()
			StringTokenizer tokens = new StringTokenizer(line);
			while (tokens.hasMoreTokens()) {
        String s = tokens.nextToken();

        //process the token
        s = s.toLowerCase();

        //maintain placeholders for sentence-delimiters
		    s = s.replaceAll("\\?", del);
		    s = s.replaceAll("!", del);
		    s = s.replaceAll("\\.", del);

        //nuke all non-word characters
		    s = s.replaceAll("[^\\w]", " ");
		    s = s.replaceAll("_", " ");

        //use a period for the delimit (not really necessary)
		    s = s.replaceAll(del, " . ");

        //nuke any extra whitespace (not really necessary)
		    s = s.replaceAll("\\s+", " ");

        //split by spaces
		    String arr[] = s.split(" ");

        //in case any originally space-delimited token had multiple words, break it up
        //examples could be "isn't" and "mother-in-law"
        for (String t : arr) {
          
          if ( prev == null || prev.equals(".") || t.equals(".") || prev.equals("") || t.equals("")) {
            //sentence boundary here
          } else {
            //yay it's a bigram
            bigram.set(prev + " " + t);
            context.write(bigram, one);
          }
          prev = t;
        }
			} // End while

		} // End method "map"
	} // End Class Map_One
	
	
	// The reduce class
	// The key is Text and must match the datatype of the output key of the map method
	// The value is IntWritable and also must match the datatype of the output value of the map method
	public static class Reduce_One extends Reducer<Text, IntWritable, Text, IntWritable>  {
		
		// The reduce method
		// For key, we have an Iterable over all values associated with this key
		// The values come in a sorted fashion.
		public void reduce(Text key, Iterable<IntWritable> values, Context context) 
											throws IOException, InterruptedException  {
			
			int sum = 0;

      //sum the values and regurgitate			
			for (IntWritable val : values) {
				int value = val.get();
				sum += value;
			}
			
      context.write(key, new IntWritable(sum));
			
		} // End method "reduce" 
		
	} // End Class Reduce_One
	
	// The second Map Class
	public static class Map_Two extends Mapper<LongWritable, Text, Text, Text>  {
		
		private final Text textkey = new Text("key");
		
		public void map(LongWritable key, Text value, Context context) 
				throws IOException, InterruptedException  {
      //echo all values with a single key so they all go to a single reducer
			context.write(textkey, value);
		}  // End method "map"
		
	}  // End Class Map_Two
	
	// The second Reduce class
	public static class Reduce_Two extends Reducer<Text, Text, Text, IntWritable>  {
		
		public void reduce(Text key, Iterable<Text> values, Context context) 
				throws IOException, InterruptedException  {
			
      //keep track of the ten highest value so far
      Map<String,Integer> best = new HashMap<String, Integer>();

			for (Text t : values) {
			  String s = t.toString();
			  
			  //each line is of format "hello world \t 9001"
			  String splitval[] = s.split("\t");
        int val = Integer.parseInt(splitval[1]);
        String bigram = splitval[0];

		    if (best.size() < 10) {
		      //for the first ten values
		      best.put(bigram, val);
		    } else {
		    
		      //find the current minimum out of the top ten
          Map.Entry<String,Integer> min = null;
          for (Map.Entry<String,Integer> e : best.entrySet()) {
            if (min == null) {
              //for the first value
              min = e;
            } else {
		          if (e.getValue() < min.getValue()) {
		            min = e;
		          }
		        }
          }

          // we have the min. See if we just found a better value.
          if (val > min.getValue()) {
            best.remove(min.getKey());
            best.put(bigram, val);
          }
		    }
			}
			
			//echo the top ten values to the file
			for (Map.Entry<String, Integer> e : best.entrySet()) {
			  context.write(new Text(e.getKey()), new IntWritable(e.getValue()));
			}
		}  // End method "reduce"
		
	}  // End Class Reduce_Two

}
