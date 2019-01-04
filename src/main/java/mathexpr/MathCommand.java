package mathexpr;

import org.graalvm.polyglot.Context;

/**
 *
 * @author naoki
 */
public class MathCommand {
    public static void main(String[] args) {
        Context ctx = Context.create("mathlang");
        System.out.println(ctx.eval("mathlang", args[0]));
    }
}
