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

/**
 * @author Curtis Ullerich <curtisu@iastate.edu>
 */
public class Hops extends Configured implements Tool {
	
	public static void main ( String[] args ) throws Exception {
		
		int res = ToolRunner.run(new Configuration(), new Hops(), args);
		System.exit(res); 
		
	} // End main
	
	public int run ( String[] args ) throws Exception {
		
		String input =  "/datasets/Lab3/Graph";    // Change this accordingly
		//String input =  "/users/curtisu/lab3/dat2";    // Change this accordingly
		String temp =   "/users/curtisu/lab3/exp1.24tmp";  // Change this accordingly
		String temp2 =   "/users/curtisu/lab3/exp1.24tmp2";  // Change this accordingly
		String output = "/users/curtisu/lab3/exp1.24output";  // Change this accordingly
		
		int reduce_tasks = 2;  // The number of reduce tasks that will be assigned to the job
		Configuration conf = new Configuration();
		
		// Create job for round 1
		
		// Create the job
		Job job_one = new Job(conf, "Hops Program Round One"); 
		
		// Attach the job to this Bigrams
		job_one.setJarByClass(Hops.class); 
		
		// Fix the number of reduce tasks to run
		// If not provided, the system decides on its own
		job_one.setNumReduceTasks(reduce_tasks);
		
		// The datatype of the Output Key 
		// Must match with the declaration of the Reducer Class
		job_one.setOutputKeyClass(Text.class); 
		
		// The datatype of the Output Value 
		// Must match with the declaration of the Reducer Class
		job_one.setOutputValueClass(Text.class);
		
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
		
		Job job_two = new Job(conf, "Hops Program Round Two"); 
		job_two.setJarByClass(Hops.class);
		job_two.setNumReduceTasks(reduce_tasks); 
		
		job_two.setOutputKeyClass(Text.class); 
		job_two.setOutputValueClass(Text.class);
		
		// If required the same Map / Reduce classes can also be used
		// Will depend on logic if separate Map / Reduce classes are needed
		// Here we show separate ones
		job_two.setMapperClass(Map_Two.class); 
		job_two.setReducerClass(Reduce_One.class); //TODO intentionally used the same reducer twice!
		
		job_two.setInputFormatClass(TextInputFormat.class); 
		job_two.setOutputFormatClass(TextOutputFormat.class);
		
		// The output of previous job set as input of the next
		FileInputFormat.addInputPath(job_two, new Path(temp)); 
		FileOutputFormat.setOutputPath(job_two, new Path(temp2));
		
		// Run the job
		job_two.waitForCompletion(true); 
		
		Job job_three = new Job(conf, "Hops Program Round Three");
		job_three.setJarByClass(Hops.class);
		job_three.setNumReduceTasks(1);
		job_three.setOutputKeyClass(Text.class);
		job_three.setOutputValueClass(IntWritable.class);
		job_three.setMapperClass(Map_Three.class);
		job_three.setReducerClass(Reduce_Three.class);
		job_three.setInputFormatClass(TextInputFormat.class);
		job_three.setOutputFormatClass(TextOutputFormat.class);
		
    job_three.setMapOutputKeyClass(Text.class);
    job_three.setMapOutputValueClass(Text.class);
		
		
		FileInputFormat.addInputPath(job_three, new Path(temp2));
		FileOutputFormat.setOutputPath(job_three, new Path(output));
		
		job_three.waitForCompletion(true);
		
		return 0;
	}
	
	public static class Map_One extends Mapper<LongWritable, Text, Text, Text>  {

		public void map(LongWritable key, Text value, Context context) 
								throws IOException, InterruptedException  {
			String line[] = value.toString().split("\\s+");
			String a = line[0];
			String b = line[1];
			
			// use > and < to indicate downstream and upstream from the key, respectively
      context.write(new Text(a), new Text(">"+b));
      context.write(new Text(b), new Text("<"+a));
		} 
	} 
	
	public static class Reduce_One extends Reducer<Text, Text, Text, Text>  {
		
		public void reduce(Text key, Iterable<Text> values, Context context) 
											throws IOException, InterruptedException  {

      String cat = "";
      //sum the values and regurgitate			
			for (Text val : values) {
			  cat += val + ",";
			}
			
			cat = cat.substring(0, cat.length()-1);
			
      context.write(key, new Text(cat));
		} 
		
	} 
	
	public static class Map_Two extends Mapper<LongWritable, Text, Text, Text>  {
		
		public void map(LongWritable key, Text value, Context context) 
				throws IOException, InterruptedException  {
		  
		  String line[] = value.toString().split("\t");
		  String middle = line[0];
		  String values[] = line[1].split(",");

      //find the set of upstream and downstream vertices
      Set<String> upstreams = new HashSet<String>();
      Set<String> downstreams = new HashSet<String>();
      for (String s : values) {
        if (s.charAt(0) == '<') {
          upstreams.add(s);
        } else {
          downstreams.add(s);
        }
      }

      Set<String> outputs = new HashSet<String>();
      //build all possible edge sets of one and two hops
		  for (String up : upstreams) {
		    for (String down : downstreams) {
		      context.write(new Text(down.substring(1)), new Text(middle));
		      context.write(new Text(down.substring(1)), new Text(up.substring(1)+"-"+middle));
		      context.write(new Text(middle), new Text(up.substring(1)));
		    }
		  }
		}
	}  
	
	/* I'm re-using Reduce_One here instead
	public static class Reduce_Two extends Reducer<Text, Text, Text, IntWritable>  {
		
		public void reduce(Text key, Iterable<Text> values, Context context) 
				throws IOException, InterruptedException  {
		  context.write(
		} 
		
	} */ 
	public static class Map_Three extends Mapper<LongWritable, Text, Text, Text>  {

    Text t = new Text("key");

		public void map(LongWritable key, Text value, Context context) 
								throws IOException, InterruptedException  {
		  String line[] = value.toString().split("\t");
		  //Text keytext = new Text(line[0]);
		  String values[] = line[1].split(",");
		  
		  Set<String> vals = new HashSet<String>();
		  
      for (String s : values) {
        //split around the vertex delimiter so I can count the unique
        //vertices, not just one-hops and two-hops separately
        String patents[] = s.split("-");
        for (String u : patents) {
          vals.add(u);
        }
        //vals.add(s);
      }
      context.write(t, new Text(line[0] + "&" + vals.size()));
		}
	} 
	
	public static class Reduce_Three extends Reducer<Text, Text, Text, IntWritable>  {
		
		public void reduce(Text key, Iterable<Text> values, Context context)
											throws IOException, InterruptedException {

      //count them all
      //keep track of the ten highest value so far
      Map<String,Integer> best = new HashMap<String, Integer>();

			for (Text t : values) {
			  String s = t.toString();

			  //each line is of format "0934-091348 \t 9001"
			  String splitval[] = s.split("&");
        int val = Integer.parseInt(splitval[1]);
        String patent = splitval[0];

		    if (best.size() < 10) {
		      //for the first ten values
		      best.put(patent, val);
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
            best.put(patent, val);
          }
		    }
			}

			//echo the top ten values to the file
			for (Map.Entry<String, Integer> e : best.entrySet()) {
			  context.write(new Text(e.getKey()), new IntWritable(e.getValue()));
			}
		}
	}
}
