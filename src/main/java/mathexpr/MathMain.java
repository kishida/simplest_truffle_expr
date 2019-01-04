package mathexpr;

/**
 *
 * @author naoki
 */
public class MathMain {
    public static void main(String[] args) {
        String exp = "12+34+56+aa+rand";
        MathCommand.main(new String[]{exp});
    }
}
