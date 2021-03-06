import com.ibm.streams.operator.*;
import java.util.*;
import java.io.*;
import java.nio.*;

//custom spl operator for processing a stream of documents
//and clustering them on a per-hour basis
public class DeSimilarDocs extends AbstractOperator {

  private Map<String, String> docmap; //key is concatenated hashes, value is representative document
  private Map<String, String> timemap; //key is concatenated hashes, value is timestamp
  private Map<String, Integer> counts; //key is concatenated hashes, value is number of docs in this cluster
  private int k; //the number of characters per shingle
  private int lastHour; //the number of the last hour output

  public DeSimilarDocs() {
    k = 9;
    this.docmap = new HashMap<String, String>(50);
    this.timemap = new HashMap<String, String>(50);
    this.counts = new HashMap<String, Integer>(50);
    lastHour = 0;
  }

  @Override
  public void initialize(OperatorContext context) throws Exception {
    super.initialize(context);
  }


  //helper function to dump the output collected so far
  private void out() throws Exception {
    final StreamingOutput<OutputTuple> output = getOutput(0);
    //compare all documents and clear the buffers once done
    processResults();
    //everything should be ready to output
    for (String akey : counts.keySet()) {
      OutputTuple o = output.newTuple();
      o.setString("time", timemap.get(akey));
      //o.setString("name", "key: " + akey + " filename: " + docmap.get(akey) + " cluster size: " + counts.get(akey));
      o.setString("name", docmap.get(akey));
      output.submit(o);
    }

    //reset to nothing
    docmap = new HashMap<String, String>(50);
    timemap = new HashMap<String, String>(50);
    counts = new HashMap<String, Integer>(50);
    lastHour++;
  }

  public void processPunctuation(StreamingInput<Tuple> port, StreamingData.Punctuation mark) throws Exception {
    if (mark == StreamingData.Punctuation.FINAL_MARKER) {
      out();
    }
  }


  //accept a tuple and process it
  public void process(StreamingInput stream, Tuple tuple) throws Exception {
    String tstring = tuple.getString("time");
    String nstring = tuple.getString("name");

    //conver the time into number of seconds since midnight
    int current = parseTime(tstring);
    if (current/(60*60) >= lastHour + 1) {
      //one hour has passed. output.
      out();
    }
    // name is a filename, so read it in and put it in a buffer
    byte[] file = readFile(nstring);
    String line = new String(file);
    int hashes[] = new int[4];
    String firstShingle = line.substring(0, k);
    for (int j = 0; j < hashes.length; j++) {
      hashes[j] = hash(firstShingle.getBytes(), j);
    }
    //hash all shingles
    for (int i = 0; i < line.length()-k+1; i++) {
      String shingle = line.substring(i, i+k);
      for (int j = 0; j < hashes.length; j++) {
        int h = hash(shingle.getBytes(), j);
        //always keep the mins
        if (h < hashes[j]) {
          hashes[j] = h;
        }
      }
    }

    //concatenate
    String key = "";
    for (int i = 0; i < hashes.length; i++) {
      key += hashes[i] + "-";
    }

    if (this.counts.containsKey(key)) {
      this.counts.put(key, counts.get(key) + 1);
    } else {
      this.counts.put(key, 1);
    }
    if (!this.docmap.containsKey(key)) {
      this.docmap.put(key, nstring);
      this.timemap.put(key, tstring);
    }

    //the final tuple. Because it doesn't officiall roll past midnight.
    //if (nstring.equals("File-119999")) {
    //  out();
    //}
  
  }

  //helper function to massage the collected statistics for the 
  //last hour into the desired output format
  private void processResults() {
    int max = 0;
    String maxkey = "";
    for (String key : counts.keySet()) {
      if (counts.get(key) > max) {
        max = counts.get(key);
        maxkey = key;
      } 
    } 

    Map<String, Integer> stragglers = new HashMap<String, Integer>();
    Map<String, Integer> keepers = new HashMap<String, Integer>();

    //split clusters into good and bad
    for (String key : counts.keySet()) {
      if (counts.get(key) < max*.6) {
        stragglers.put(key, counts.get(key));
      } else {
        keepers.put(key, counts.get(key));
      }
    }
    counts = new HashMap<String, Integer>();
    for (String key : keepers.keySet()) {
      counts.put(key, keepers.get(key));
    }

    //make sure that each file has a home in a large cluster
    for (String key : stragglers.keySet()) {
      hello: for (int i = 3; i > 0; i--) {
        for (String ckey : keepers.keySet()) {
          if (matchesBy(i, key, ckey)) {
            if (counts.containsKey(ckey)) {
              counts.put(ckey, stragglers.get(key) + counts.get(ckey));
            } else {
              counts.put(ckey, stragglers.get(key) + keepers.get(ckey));
            }
            break hello;
          }
        }
      }
    }
  }

  //return true if one and two contain at least threshold matching hashes, '-'-seperated
  public boolean matchesBy(int threshold, String one, String two) {
    String onehashes[] = one.substring(0, one.length()-1).split("-");
    String twohashes[] = two.substring(0, two.length()-1).split("-");

    int count = 0;
    for (int i = 0; i < onehashes.length; i++) {
      if (onehashes[i].equals(twohashes[i])) {
        count++;
      }
    }
    if (count >= threshold) {
      return true;
    }
    return false;
  }

  //convert the number of seconds into a time
  private String timeToString(int time) {
    int second = time % 60;
    time /= 60;
    int minute = time % 60;
    time /= 60;
    int hour = time;
    String str = "";
    if (hour < 10) {
      str += "0";
    }
    str += hour + ":";
    if (minute < 10) {
      str += "0";
    }
    str += minute + ":";
    if (second < 10) {
      str += "0";
    }
    str += second;
    return str;
  }

  //convert the time string into a number of seconds
  private int parseTime(String time) {
    String comps[] = time.split(":");
    int hour = Integer.parseInt(comps[0]);
    int minute = Integer.parseInt(comps[1]);
    int second = (int) Double.parseDouble(comps[2]);
    return hour*60*60+minute*60+second;
  }

  //read a file into a byte array
  public byte[] readFile(String name) throws java.io.IOException {
    File file = new File(name);
    RandomAccessFile f = new RandomAccessFile("/datasets/Lab10/Documents/" + file, "r");

    // Get and check length
    long longlength = f.length();
    int length = (int) longlength;
    // Read file and return data
    byte[] data = new byte[length];
    f.readFully(data);
    f.close();
    return data;

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