package mathexpr;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystem;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.RootNode;

/**
 *
 * @author naoki
 */
public class MathNodes {
    
    @TypeSystem(long.class)
    static abstract class MathTypes {
    }
    
    @TypeSystemReference(MathTypes.class)
    static abstract class MathNode extends Node {
        abstract Object executeGeneric(VirtualFrame frame);
    }

    static class MathRootNode extends RootNode {
        private MathNode body;
        private String name;

        public MathRootNode(
                TruffleLanguage<?> language, FrameDescriptor frameDescriptor, 
                MathNode body, String name) {
            super(language, frameDescriptor);
            this.body = body;
            this.name = name;
        }
        
        @Override
        public Object execute(VirtualFrame frame) {
            return body.executeGeneric(frame);
        }
        
    }
    
    @NodeInfo(shortName = "value")
    static class LongNode extends MathNode {
        private long value;

        private LongNode(long value) {
            this.value = value;
        }
        
        static LongNode of(String v) {
            return new LongNode(Long.parseLong(v.trim()));
        }
        
        long executeLong(VirtualFrame frame) {
            return value;
        }

        @Override
        Object executeGeneric(VirtualFrame frame) {
            return value;
        }
    }
    
    @NodeInfo(shortName = "+")
    @NodeChild("leftNode")
    @NodeChild("rightNoode")
    public static abstract class AddNode extends MathNode {
        @Specialization
        public long add(long left, long right) {
            return left + right;
        }
        
        public Object add(Object left, Object right) {
            return null;
        }
    }
}