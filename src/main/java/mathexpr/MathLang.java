package mathexpr;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.TruffleLanguage;
import com.oracle.truffle.api.debug.DebuggerTags;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.instrumentation.ProvidedTags;
import com.oracle.truffle.api.instrumentation.StandardTags;
import mathexpr.MathNodes.LongNode;
import mathexpr.MathNodes.MathNode;
import mathexpr.MathNodes.MathRootNode;

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
        MathNode node = LongNode.of(nums[nums.length - 1]);
        for (int i = nums.length - 2; i >= 0; --i) {
            node = MathNodesFactory.AddNodeGen.create(LongNode.of(nums[i]), node);
        }
        MathRootNode root = new MathRootNode(this, new FrameDescriptor(), node, "main");
        return Truffle.getRuntime().createCallTarget(root);
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
