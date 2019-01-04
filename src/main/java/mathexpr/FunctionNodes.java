package mathexpr;

import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.RootNode;
import java.util.Random;
import mathexpr.MathNodes.MathNode;

/**
 *
 * @author naoki
 */
public class FunctionNodes {
    static class MathFunction {
        private final RootCallTarget callTarget;

        public MathFunction(RootCallTarget callTarget) {
            this.callTarget = callTarget;
        }
        
    }
    static class FuncRootNode extends RootNode {

        MathNode function;
        
        public FuncRootNode(TruffleLanguage<?> language, FrameDescriptor frameDescriptor,
                MathNode function) {
            super(language, frameDescriptor);
            this.function = function;
        }
        
        @Override
        public Object execute(VirtualFrame frame) {
            return function.executeGeneric(frame);
        }
        
    }
    static abstract class RandNode extends MathNode {
        
        static Random rand = new Random();
        @Specialization
        public long rnd() {
            return rand.nextInt(10);
        }
    }
    
    static class InvokeNode extends MathNode {
        MathFunction function;

        public InvokeNode(MathFunction function) {
            this.function = function;
        }
        
        @Override
        Object executeGeneric(VirtualFrame frame) {
            return function.callTarget.call();
        }
        
    }
}
