/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mathexpr;

import org.graalvm.polyglot.Context;

/**
 *
 * @author naoki
 */
public class MathMain {
    public static void main(String[] args) {
        String exp = "12+34+56";
        Context cont = Context.create("mathlang");
        System.out.println(cont.eval("mathlang", exp));
    }
}
