import com.ibm.streams.operator.AbstractOperator;
import com.ibm.streams.operator.OperatorContext;
import com.ibm.streams.operator.OutputTuple;
import com.ibm.streams.operator.StreamingInput;
import com.ibm.streams.operator.Tuple;
import com.ibm.streams.operator.StreamingOutput;
import java.io.*;
import java.util.*;

public class DeSimilarDocs extends AbstractOperator {

  private ArrayList<Integer[]> results;
  private int previous;
  private int k;

  public DeSimilarDocs() {
    k = 9;
    this.results = new ArrayList<Integer[]>(50);
    this.previous = -1;
  }

  @Override
  public void initialize(OperatorContext context) throws Exception {
    super.initialize(context);
  }

  public void process(StreamingInput stream, Tuple tuple) throws Exception {
    final StreamingOutput<OutputTuple> output = getOutput(0);
    String tstring = tuple.getString("time");
    String nstring = tuple.getString("name");

    // name is a filename, so read it in and put it in a buffer
    byte[] file = readFile(nstring);
    int hashes[] = new int[4];
    for (int j = 0; j < file.length; j++) {
      for (int l = 0; l < hashes.length; l++) {
        int h = hash(file, j, this.k, l);
        hashes[l] = h;
      }
    }
    this.results.add(hashes);

    int current = parseTime(tstring);
    if (this.previous == -1) {
      //very first tuple
      this.previous = current;
    } else {

      if (current - previous >= 60*60) {
        //one hour has passed

        //compare all documents and clear the buffers once done
        processResults();
      }
    }
  }

  private void processResults() {


    //output.submit(tuple);
  }

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
    time += hour + ":";
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

  private int parseTime(String time) {
    String comps[] = time.split(":");
    int hour = comps[0];
    int minute = comps[1];
    int second = comps[2];
    return hour*60*60+minute*60+second;
  }

  public byte[] readFile(String name) {
    File file = new File(name);
    RandomAccessFile f = new RandomAccessFile(file, "r");
    try {
      // Get and check length
      long longlength = f.length();
      int length = (int) longlength;
      // Read file and return data
      byte[] data = new byte[length];
      f.readFully(data);
      return data;
    } finally {
      f.close();
    }

  }

 /*
  * Below is a hash function that can be used throughout your Application
  * to help with minhashing. New hash functions can be derived by using a 
  * new seed value.
  */
  private int hash(byte[] content, int start, int stop, int seed){

    int m = 0x5bd1e995;
    int r = 24;

    int len = stop - start;
    byte[] work_array = null;

    int h = seed ^ len;

    int offset = 0;

    work_array = new byte[4];
    while(len >= 4)
    {

      ByteBuffer buf = ByteBuffer.wrap(Arrays.copyOfRange(content, offset+start, offset+start + 4));
      //ByteBuffer buf = ByteBuffer.wrap(content.substring(offset+start, offset+start + 4).getBytes());

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