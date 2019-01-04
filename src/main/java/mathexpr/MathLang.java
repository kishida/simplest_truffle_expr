package mathexpr;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.debug.DebuggerTags;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.instrumentation.ProvidedTags;
import com.oracle.truffle.api.instrumentation.StandardTags;
import mathexpr.FunctionNodes.MathFunction;
import mathexpr.RandNode;
import mathexpr.RandNodeGen;
import mathexpr.MathNodes.LongNode;
import mathexpr.MathNodes.MathNode;
import mathexpr.MathNodes.MathRootNode;
import mathexpr.MathNodesFactory.VariableNodeGen;

/**
 *
 * @author naoki
 */
@TruffleLanguage.Registration(name = "MathLang", id = "mathlang",
        defaultMimeType = MathLang.MIME_TYPE, characterMimeTypes = MathLang.MIME_TYPE)
@ProvidedTags({StandardTags.CallTag.class, StandardTags.StatementTag.class, 
    StandardTags.RootTag.class, DebuggerTags.AlwaysHalt.class})
public class MathLang extends TruffleLanguage<MathLang.MathLangContext>{
    public static final String MIME_TYPE = "application/x-mathlang";
    
    public static class MathLangContext {
    }

    @Override
    protected CallTarget parse(ParsingRequest request) throws Exception {
        String source = request.getSource().getCharacters().toString();
        String[] nums = source.split("\\+");
        FrameDescriptor frame = new FrameDescriptor();

        MathNode node = parseNode(frame, nums[nums.length - 1]);
        for (int i = nums.length - 2; i >= 0; --i) {
            node = MathNodesFactory.AddNodeGen.create(parseNode(frame, nums[i]), node);
        }
        MathRootNode root = new MathRootNode(this, frame, node);
        return Truffle.getRuntime().createCallTarget(root);
    }
    
    static MathFunction RAND_FUNC;
    MathFunction createBuiltin(FrameDescriptor frame) {
        if (RAND_FUNC == null) {
            RandNode rand = RandNodeGen.create();
            RAND_FUNC = new MathFunction(Truffle.getRuntime().createCallTarget(
                    new FunctionNodes.FuncRootNode(this, frame, rand)));
        }
        return RAND_FUNC;
    }
    
    MathNode parseNode(FrameDescriptor frame, String value) {
        try {
            return LongNode.of(value);
        } catch (NumberFormatException ex) {
            if ("rand".equals(value)) {
                return new FunctionNodes.InvokeNode(createBuiltin(frame));
            } else {
                return VariableNodeGen.create(frame.findOrAddFrameSlot(value));
            }
        }
    }
    
    @Override
    protected MathLangContext createContext(Env env) {
        return new MathLangContext();
    }

    @Override
    protected boolean isObjectOfLanguage(Object object) {
        return false;
    }
    
}
