package mathexpr;

import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystem;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.FrameUtil;
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

        public MathRootNode(
                TruffleLanguage<?> language, FrameDescriptor frameDescriptor, 
                MathNode body) {
            super(language, frameDescriptor);
            this.body = body;
        }
        
        @Override
        public Object execute(VirtualFrame frame) {
            setup(frame);
            return body.executeGeneric(frame);
        }
        
        void setup(VirtualFrame frame) {
            final FrameDescriptor desc = frame.getFrameDescriptor();
            FrameSlot slotAa = desc.findOrAddFrameSlot("aa");
            desc.setFrameSlotKind(slotAa, FrameSlotKind.Long);
            frame.setLong(slotAa, 123);            
        }
    }
    
    @NodeInfo(shortName = "value")
    static class LongNode extends MathNode {
        private long value;

        private LongNode(long value) {
            this.value = value;
        }
        
        static LongNode of(String v) throws NumberFormatException {
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
    
    @NodeField(name = "slot", type = FrameSlot.class)
    public static abstract class VariableNode extends MathNode {
        abstract FrameSlot getSlot();
        
        @Specialization
        long readLong(VirtualFrame vf) {
            return FrameUtil.getLongSafe(vf, getSlot());
        }       
    }
}
