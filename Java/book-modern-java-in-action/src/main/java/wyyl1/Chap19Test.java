package wyyl1;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author aoe
 * @date 2021/3/7
 */
public class Chap19Test {

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>();
        map.computeIfAbsent("a", (k) -> {
            Integer v = 1;
            System.out.println("k " + v);
            return v;
        });

        System.out.println(map.get("a"));
        System.out.println(map.get("a"));
        System.out.println(map.get("a"));
    }



}
