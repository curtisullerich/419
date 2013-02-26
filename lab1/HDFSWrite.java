import java.io.*;
import java.lang.*;
import java.util.*;
import java.net.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.util.*;
public class HDFSWrite  {
    
public static void main ( String [] args ) throws Exception {
        
// The system configuration
Configuration conf = new Configuration(); 
// Get an instance of the Filesystem
FileSystem fs = FileSystem.get(conf);
String path_name = "/user/curtisu/Lab1/newfile";
Path path = new Path(path_name);
// The Output Data Stream to write into
FSDataOutputStream file = fs.create(path);
// Write some data
file.writeChars("The first Hadoop Program!");
// Close the file and the file system instance
file.close(); 
fs.close();
    }
}
