package wyyl1.chap01;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * @author aoe
 * @date 2020/12/14
 */
public class Chap03Test {

    public static void main(String[] args) {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("a");
        stringList.add("b");
        stringList.add("c");

        stringList.stream().forEach(str -> {
            if (str.equals("b")) {
                return; // only skips this iteration.
            }

            System.out.println(str);
        });

        // 3.8.3 函数复合

        /*Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        // 数学上写作：g(f(x))
        Function<Integer, Integer> h = f.andThen(g);
        // 结果是 4
        int result = h.apply(1);
        System.out.println(result);*/

        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        // 数学上写作：f(g(x))
        Function<Integer, Integer> h = f.compose(g);
        // 结果是 3
        int result = h.apply(1);
        System.out.println(result);
    }
}
