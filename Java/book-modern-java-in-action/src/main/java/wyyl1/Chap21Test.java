package wyyl1;

import akka.stream.impl.FanOut;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author aoe
 * @date 2021/3/9
 */
public class Chap21Test {

    public static void main(String[] args) {
        final int[] unrealImmutable = new int[]{1, 2};
        System.out.println(Arrays.toString(unrealImmutable));

        unrealImmutable[0] = 11;
        unrealImmutable[1] = 22;
        System.out.println(Arrays.toString(unrealImmutable));

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Collections.shuffle(list);
        System.out.println(list);

        int max = Integer.MAX_VALUE / 87200;
        System.out.println(max);

        List<Integer> intList = Arrays.asList(1, 2, 3, 4, 5, 6);
        System.out.println(list.stream().findAny().orElse(-1));

        // 🖤-旋转加速器
        System.out.println(Integer.MAX_VALUE);
    }
}
