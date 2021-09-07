package wyyl1;

import akka.stream.impl.FanOut;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        List<Integer> userIds = Arrays.asList(20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 19);
        Collections.sort(userIds);
        System.out.println(userIds);
        String userIdsStr = "";
        for (Integer userId : userIds) {
            userIdsStr += userId + ",";
        }
        System.out.println(userIdsStr);

        String value = Long.toBinaryString(Long.MAX_VALUE);
        System.out.println(value + " " + value.length());

        int[] arr = IntStream.range(0, 365)
                .map(i -> i % 2)
                .toArray();
        System.out.println(arr.length);
        String arrStr = Arrays.toString(arr).replaceAll(" ", "");
        System.out.println(arrStr.length());
        System.out.println(arrStr);

        BigDecimal taskDuration = new BigDecimal("15");
        System.out.println(new BigDecimal("1").compareTo(taskDuration));
        System.out.println(new BigDecimal("15").compareTo(taskDuration));
        System.out.println(new BigDecimal("16").compareTo(taskDuration));

        System.out.println(LocalDate.now());
    }
}
