import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Cluster extends Configured implements Tool{


	public static void main ( String[] args ) throws Exception {

		int res = ToolRunner.run(new Configuration(), new Cluster(), args);
		System.exit(res); 

	} // End main

	public int run ( String[] args ) throws Exception {
		
		/**
		 * You are not required to use the exact Mapping or Reducer layout or numbers as given in this file.
		 * Fill free to add or remove rounds of MapReduce as you need. This is just to serve as a starting 
		 * location.
		 */		

		String input = args[0];    // Change this accordingly
		String output = args[1];  // Change this accordingly
		
	   /*
		* Create your new jobs here as you've done in previous labs.
		*/
	
		return 0;

	} // End run

/*
 * Below is a hash function that can be used throughout your Application
 * to help with minhashing. New hash functions can be derived by using a 
 * new seed value.
 */
	private static int hash(byte[] b_con, int i_seed){

		String content = new String(b_con);

		int seed = i_seed;
		int m = 0x5bd1e995;
		int r = 24;

		int len = content.length();
		byte[] work_array = null;

		int h = seed ^ content.length();

		int offset = 0;

		while( len >= 4)
		{
			work_array = new byte[4];
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