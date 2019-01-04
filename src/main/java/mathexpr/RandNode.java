package mathexpr;

import com.oracle.truffle.api.dsl.Specialization;
import java.util.Random;

/**
 *
 * @author naoki
 */
public abstract class RandNode extends MathNodes.MathNode {
    
    static Random rand = new Random();

    @Specialization
    public long rnd() {
        return rand.nextInt(10);
    }
    
}
