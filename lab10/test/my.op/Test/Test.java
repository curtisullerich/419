import com.ibm.streams.operator.AbstractOperator;
import com.ibm.streams.operator.OperatorContext;
import com.ibm.streams.operator.OutputTuple;
import com.ibm.streams.operator.StreamingInput;
import com.ibm.streams.operator.Tuple;
import com.ibm.streams.operator.StreamingOutput;
public class Test extends AbstractOperator {
  @Override
  public void initialize(OperatorContext context) throws Exception {
    super.initialize(context);
  }
  public void process(StreamingInput stream, Tuple tuple) throws Exception {
    final StreamingOutput<OutputTuple> output = getOutput(0);
    // Submit any tuple with count greater than 3
    if( tuple.getInt("count") > 3 ) {
      output.submit(tuple);
    }
  }
}