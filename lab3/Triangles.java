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
public class Triangles extends Configured implements Tool {
	
	public static void main ( String[] args ) throws Exception {
		
		int res = ToolRunner.run(new Configuration(), new Triangles(), args);
		System.exit(res); 
		
	} // End main
	
	public int run ( String[] args ) throws Exception {
		
		String input =  "/datasets/Lab3/Graph";    
		//String input =  "/users/curtisu/lab3/dat2";
		
		String base = "/users/curtisu/lab3/exp2/10/";
		String temp = base + "tmp";
		String temp2 = base + "tmp2";
		String output = base + "output";
		
		int reduce_tasks = 3;  // The number of reduce tasks that will be assigned to the job
		Configuration conf = new Configuration();
		
		// Create job for round 1
		
		// Create the job
		Job job_one = new Job(conf, "Triangles Program Round One"); 
		
		// Attach the job to this Bigrams
		job_one.setJarByClass(Triangles.class); 
		
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
		
		Job job_two = new Job(conf, "Triangles Program Round Two"); 
		job_two.setJarByClass(Triangles.class);
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
		FileOutputFormat.setOutputPath(job_two, new Path(temp2));
		
		// Run the job
		job_two.waitForCompletion(true); 
		
		Job job_three = new Job(conf, "Triangles Program Round Three");
		job_three.setJarByClass(Triangles.class);
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
			
			if (a.compareTo(b) != 0) {
			  context.write(new Text(a), new Text(b));
			  context.write(new Text(b), new Text(a));
			} else {
			  //context.write(new Text("removed"), new Text(a + "," +b));
			}
      /*			
			int result = a.compareTo(b);
			
			if (result < 0) {
			  context.write(new Text(a), new Text(b));
			} else if (result > 0) {
			  context.write(new Text(b), new Text(a));
			} else {
			  //they're identical. do not want.
			}
			*/
		} 
	} 
	
	public static class Reduce_One extends Reducer<Text, Text, Text, Text>  {
		
		public void reduce(Text key, Iterable<Text> values, Context context) 
											throws IOException, InterruptedException  {

      String cat = "";
      int count = 0;
      //sum the values and regurgitate			
			for (Text val : values) {
			  cat += val + ",";
			  count++;
			}
			
			if (count < 2) {
			  //no triangles are possible
  			//context.write(key, new Text("removed: " + cat));
			} else {
  			cat = cat.substring(0, cat.length()-1);
        context.write(key, new Text(cat));
			}
			
		} 
		
	} 
	
	public static class Map_Two extends Mapper<LongWritable, Text, Text, Text>  {
		
		private Text have = new Text("have");
		
		public void map(LongWritable key, Text value, Context context) 
				throws IOException, InterruptedException  {
		  
		  String line[] = value.toString().split("\t");
		  String v = line[0];
		  String values[] = line[1].split(",");

      Set<String> haves = new HashSet<String>(values.length);

      for (String s : values) {
        if (v.compareTo(s) < 0) {
          haves.add(v+"-"+s);
        } else {
          haves.add(s+"-"+v);
        }
      }
      
      for (String s : haves) {
        context.write(new Text(s), new Text(have));
      }
      
      Set<String> havenots = new HashSet<String>(values.length);
      
      for (int i = 0; i < values.length; i++) {
        for (int j = i+1; j < values.length; j++) {
          if (values[i].compareTo(values[j]) < 0) {
            havenots.add(values[i] + "-" + values[j]);
            //TODO might be adding values like "c-c" here
          } else {
            havenots.add(values[j] + "-" + values[i]);
          }
        }
      }
      
      for (String s : havenots) {
        context.write(new Text(s), new Text(v));
      }
      
		}
	}  
	
	
	public static class Reduce_Two extends Reducer<Text, Text, Text, IntWritable>  {
		
		private IntWritable one = new IntWritable(1);
		private Text a = new Text("a");
		
		public void reduce(Text key, Iterable<Text> values, Context context) 
				throws IOException, InterruptedException  {
			
		  Set<String> tris = new HashSet<String>();
		  Set<String> wants = new HashSet<String>();
	    String pair[] = key.toString().split("-");
	    String a = pair[0];
	    String b = pair[1];
	    
	    boolean has = false;
		  for (Text t : values) {
		    String s = t.toString();
		    if (s.equals("have")) {
		      has = true;
		    } else {
		      wants.add(s);
		    }
		  }
		  
		  if (has) {
		    for (String c : wants) {
		      if (a.compareTo(b) < 0 && b.compareTo(c) < 0) {
		        tris.add(a + "-" + b + "-" + c);
		      }
		      /*
		      String tri = "";
		      if (a.compareTo(b) < 0 && a.compareTo(c) < 0) {
		        tri += a + "-";
		        if (b.compareTo(c) < 0) {
		          tri += b + "-" + c;
		        } else {
		          tri += c + "-" + b;
		        }
		      } else if (b.compareTo(a) < 0 && b.compareTo(c) < 0) {
		        tri += b + "-";
		        if (a.compareTo(c) < 0) {
		          tri += a + "-" + c;
		        } else {
		          tri += c + "-" + a;
		        }
		      } else {
		        tri += c + "-";
		        if (a.compareTo(b) < 0) {
		          tri += a + "-" + b;
		        } else {
		          tri += b + "-" + a;
		        }
		      }
		      tris.add(tri);*/
		    }
		    
		    for (String s : tris) {
		       context.write(new Text(s), one);// to write the triangle
		      //context.write(a, one);
		    }
		  } else {
		    //the desired edge does not exist
		    //output nothing
		  }
		} 
		
	}  
	public static class Map_Three extends Mapper<LongWritable, Text, Text, Text>  {

    Text t = new Text("key");

		public void map(LongWritable key, Text value, Context context) 
								throws IOException, InterruptedException  {
		  String line[] = value.toString().split("\t");
		  
      context.write(t, new Text(line[0]));
		}
	} 
	
	public static class Reduce_Three extends Reducer<Text, Text, Text, IntWritable>  {
		
		public void reduce(Text key, Iterable<Text> values, Context context)
											throws IOException, InterruptedException {
      int count = 0;
		  for (Text t : values) {
		    count++;
		  }
		  context.write(new Text("Triangles"), new IntWritable(count));
		}
	}
}
