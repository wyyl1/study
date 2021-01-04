package wyyl1.chap01;

import java.util.function.Function;

/**
 * @author aoe
 * @date 2020/12/25
 */
public class Chap03Letter {

    public static String addHeader(String text) {
        return "Header: " + text;
    }

    public static String addFooter(String text) {
        return " Footer " + text;
    }

    public static String checkSpelling(String text) {
        return text.replaceAll("labda", "lambda");
    }

    public static void assemblyLine1(){
        String text = "流水线1";
        Function<String, String> addHeader = Chap03Letter::addHeader;
        Function<String, String> transformationPipeline = addHeader
                .andThen(Chap03Letter::checkSpelling)
                .andThen(Chap03Letter::addFooter);
        System.out.println(transformationPipeline.apply(text));
        // 输出：Footer Header: 流水线1
    }

    public static void assemblyLine2(){
        String text = "流水线2";
        Function<String, String> addHeader = Chap03Letter::addHeader;
        Function<String, String> transformationPipeline = addHeader
                .compose(Chap03Letter::checkSpelling)
                .compose(Chap03Letter::addFooter);
        System.out.println(transformationPipeline.apply(text));
        // 输出：Header:  Footer 流水线2
    }

    public static void main(String[] args) {
        assemblyLine1();
        assemblyLine2();
    }
}
