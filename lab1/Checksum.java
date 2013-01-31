import java.io.*;
import java.lang.*;
import java.util.*;
import java.net.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;

public class Checksum {

	public static void main(String[] args) throws Exception {

		// The system configuration
		Configuration conf = new Configuration();
		// Get an instance of the Filesystem
		FileSystem fs = FileSystem.get(conf);
		String path_name = "/datasets/Lab1/bigdata";
		Path path = new Path(path_name);

    // open the file for reading
		FSDataInputStream file = fs.open(path);

    // read 1000 bytes from the file beginning at the given offset
    byte buffer[] = new byte[1000];
    file.readFully(5000000000L, buffer);

    //create the checksum    
    int chk = buffer[0];
    for (int i = 1; i < buffer.length; i++) {
      chk ^= buffer[i];
    }    

    System.out.println("Checksum: " + chk);    
		file.close();
		fs.close();
	}
}
