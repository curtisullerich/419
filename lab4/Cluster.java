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
import java.nio.ByteBuffer;

/**
 * @author Curtis Ullerich <curtisu@iastate.edu>
 */
public class Cluster extends Configured implements Tool {
	
	public static void main ( String[] args ) throws Exception {
		
		int res = ToolRunner.run(new Configuration(), new Cluster(), args);
		System.exit(res); 
		
	} // End main

	public int run ( String... args ) throws Exception {
		
		String input =  args[0];
		String runnumber = args[1];
		//String input =  "/users/curtisu/lab3/dat2";
		String temp =   "/users/curtisu/lab4/exp"+runnumber+"tmp";
		String temp2 =   "/users/curtisu/lab4/exp"+runnumber+"tmp2";
		String output = "/users/curtisu/lab4/exp"+runnumber+"output";
		
		int reduce_tasks = 2;  // The number of reduce tasks that will be assigned to the job
		Configuration conf = new Configuration();
		
		// Create job for round 1
		
		// Create the job
		Job job_one = new Job(conf, "Cluster Program Round One"); 
		
		// Attach the job to this Bigrams
		job_one.setJarByClass(Cluster.class); 
		
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
		
		Job job_two = new Job(conf, "Cluster Program Round Two"); 
		job_two.setJarByClass(Cluster.class);
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
		
		return 0;
	}
	
	public static class Map_One extends Mapper<LongWritable, Text, Text, Text>  {

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException  {
			int k = 9;  //the size of the char-shingle
			int hashes[] = new int[4]; //the number of hash functions to use in the combined key
			String line = value.toString();

			String id = "";
			id = line.substring(0, line.indexOf('-'));

			String firstShingle = line.substring(line.indexOf('-')+1, line.indexOf('-')+1+k);
			//System.out.println("first shingle: " + firstShingle);
			for (int j = 0; j < hashes.length; j++) {
				hashes[j] = Map_One.hash(firstShingle.getBytes(), j);
			}
			//int seeds[] = {42, 17, 100, 7, 13, 21};
			//hash all shingles
			for (int i = line.indexOf('-')+1; i < line.length()-k+1; i++) {
				String shingle = line.substring(i, i+k);
				for (int j = 0; j < hashes.length; j++) {
					int h = hash(shingle.getBytes(), j);

					//always keep the mins
					if (h < hashes[j]) {
						hashes[j] = h;
					}
				}
			}

			//cat the hashes
			String hashstring = "";
			for (int s : hashes) {
				hashstring += s + ",";
			}

			hashstring = hashstring.substring(0, hashstring.length() - 1);
			// output
			// hash0,hash1,hash2,hash3            documentID-oiusdASgFyHfNaAiNSuJlWJsMdfguygILFUGAFOIUAWGFouygfoiuagfuyagsdfiuG
			context.write(new Text(hashstring), value);//output the minhash set along with the original value
		}
		/*
	 * Below is a hash function that can be used throughout your Application
	 * to help with minhashing. New hash functions can be derived by using a 
	 * new seed value.
	 */
		private static int hash(byte[] b_con, int i_seed){

			String content = new String(b_con);
			//System.out.println("hashing with content = " + content);
			int seed = i_seed;
			int m = 0x5bd1e995;
			int r = 24;

			int len = content.length();
			byte[] work_array = null;

			int h = seed ^ content.length();

			int offset = 0;

			work_array = new byte[4];
			while( len >= 4)
			{
				ByteBuffer buf = ByteBuffer.wrap(content.substring(offset, offset + 4).getBytes());

				int k = buf.getInt();
				k = k * m;
				k ^= k >> r;
				k *= m;
				h *= m;
				h ^= k;

				offset += 4;
				len -= 4;
			}

			switch(len){
				case 3: h ^= work_array[2] << 16;
				case 2: h ^= work_array[1] << 8;
				case 1: h ^= work_array[0];
				h *= m;
			}

			h ^= h >> 13;
			h *= m;
			h ^= h >> 15;

			return h;
		}
	}
	
	public static class Reduce_One extends Reducer<Text, Text, Text, Text>  {
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException  {
			String ids = "";
			String doc = "";

			for (Text val : values) {
				String line = val.toString();

				//if we're looking at the first element
				if (ids.equals("")) {
					doc += line.substring(line.indexOf('-')+1);
				}

				//grab all the ids and shove them together
				ids += line.substring(0, line.indexOf('-'));
				//System.out.println("adding id: " + line.substring(0, line.indexOf('-')));
				ids += ",";
			}
			//remove trailing comma
			ids = ids.substring(0, ids.length()-1);
			//System.out.println("all ids: " + ids);
			//keep doc IDs and one sample

			// output
			// hash0-hash1-hash2-hash3       documentID0,documentID1,documentID3         this is my representative document
			context.write(key, new Text(ids + "\t" + doc));
		}
	}
	
	public static class Map_Two extends Mapper<LongWritable, Text, Text, Text>  {
		Text t = new Text("key");
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			//send to the same reducer
			context.write(t, value);
			//System.out.println("mappin value: " + value.toString());
		}
	}  
	
	public static class Reduce_Two extends Reducer<Text, Text, Text, Text>  {
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException  {
			System.out.println("starting reduce 2 : -0");
			List<Datum> data = new ArrayList<Datum>();
			
			//these values can be used to define various thresholds for halting
			int count = 0;
			int sum = 0;
			int imax = 0;
			for (Text t : values) {

				//parse the file
				String line = t.toString();
				String parts[] = line.split("\t");
				String hashes[] = parts[0].split(",");
				String[] ids = parts[1].split(",");
				String doc = parts[2];
				Datum d = new Datum(doc, hashes, ids);
				data.add(d);
				count++;
				sum += ids.length;
				if (ids.length > imax) {
					imax = ids.length;
				}

				//context.write(key, new Text(ids.length+""));
			}

			context.progress();
			System.out.println("reduce 2 : -1. Finished creating data.");

			//this is the number of hash functions used in map_one
			int numhashes = data.get(0).hashes.length;

			//allow clusters with this ratio of matching hashes to combine
			//note that this loses information in the values of the hashes of one of the clusters
			double ratio = 1./2.;
			for (int i = 0; i < data.size()-1; i++) {
				for (int j = i+1; j < data.size(); j++) {
					if (i != j && data.get(i).matchesBy((int)(numhashes*ratio), data.get(j))) {
						data.get(i).ids.addAll(data.get(j).ids);
						data.remove(j);
						j--;
					}
				}
			}

			double average = sum/(float)count;
			List<Datum> misfits = new ArrayList<Datum>();
			List<Datum> keepers = new ArrayList<Datum>();

			//divide clusters into misfits and keepers based on the size of the maximum cluster size
			double percentOfMax = 0.7;
			for (int i = 0; i < data.size(); i++) {
				if (data.get(i).ids.size() <= imax*percentOfMax) {
					misfits.add(data.get(i));
				} else {
					keepers.add(data.get(i));
				}
			}
			System.out.println("reduce 2 : -2. Finished splitting misfits and keepers.");
			System.out.println("there are " + misfits.size() + " misfits and " + keepers.size() + " keepers.");

			context.progress();
			
			int num = 0;
			//slowly relax the similarity requirements for combining a misfit into a keeper
			for (int i = 0; i < misfits.size(); i++) {
				kbr: for (int k = 4; k > 0; k--) {
					for (int j = 0; j < keepers.size(); j++) {
						if (misfits.get(i).matchesBy(k, keepers.get(j))) {
							System.out.println("found a home for misfit " + num++ + " with k=" + k);
							keepers.get(j).ids.addAll(misfits.get(i).ids);
							misfits.remove(i);
							i--;
							break kbr;
						}
					}
				}
			}
			System.out.println("reduce 2 : -3. Finished attempting to merge misfits into keepers.");
			System.out.println("there are still " + misfits.size() + " misfit.");
			context.progress();

			//as a last-ditch effort to find a home for misfits, stick them in with the cluster
			//that provides the highest jaccard similarity. This really shouldn't happen
			//on this data set.
			while (misfits.size() > 0) {
				double max = -1;
				int index = -1;
				for (int i = 0; i < keepers.size(); i++) {
					double jac = misfits.get(misfits.size()-1).jaccard(keepers.get(i));
					if (jac > max) {
						max = jac;
						index = i;
					}
				}
				
				context.progress();
				keepers.get(index).ids.addAll(misfits.get(misfits.size()-1).ids);
				misfits.remove(misfits.size()-1);
			}
			context.progress();
			System.out.println("reduce 2 : -4. Finished jaccarding misfits.");
			

			for (Datum d : keepers) {
				System.out.println(d.ids.size() + "\t" + d.ids.toString());
				String s= d.ids.toString();
				context.write(new Text(s.substring(1, s.length()-1)), new Text(d.doc));
			}

			System.out.println("reduce 2: -5. Done.");
		}

		/**
     * A class to contain a particular cluster and compute similarity between other clusters.
     *
		 */
		public class Datum {
			//a representative document
			public String doc;
			//the list of minhashes for this cluster
			public String[] hashes;
			//the list of ids in this cluster
			public ArrayList<String> ids;

			public Datum(String doc, String[] hashes, String[] ids) {
				this.doc = doc;
				this.hashes = hashes;
				this.ids = new ArrayList<String>();
				this.ids.addAll(Arrays.asList(ids));
			}

			//return the jaccard similarity between this and other
			public double jaccard(Datum other) {
				Set<String> first = new HashSet<String>();
				Set<String> union = new HashSet<String>();
				int k = 9;
				for (int i = 0; i < this.doc.length()-k; i++) {
					first.add(this.doc.substring(i, i+k));
					union.add(this.doc.substring(i, i+k));
				}
				for (int i = 0; i < other.doc.length()-k; i++) {
					union.add(other.doc.substring(i, i+k));
					if (!first.contains(other.doc.substring(i, i+k))) {
						first.remove(other.doc.substring(i, i+k));
					}
				}
				double jac = (double) first.size() / (double) union.size();
				return jac;
			}

			//return true if this and other contain at least threshold common minhashes
			public boolean matchesBy(int threshold, Datum other) {
				if (this.hashes.length != other.hashes.length) {
					throw new IllegalArgumentException("Data had different number of hashes.");
				}
				int count = 0;
				for (int i = 0; i < hashes.length; i++) {
					if (this.hashes[i].equals(other.hashes[i])) {
						count++;
					}
				}
				if (count >= threshold) {
					return true;
				}
				return false;
			}

		}
	}

}
