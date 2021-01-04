package wyyl1.chap01;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author aoe
 * @date 2021/1/4
 */
public class Chap05Test {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(12, 2, 9, 3, 3, 5);

        System.out.println(list.stream()
                .takeWhile(i -> i > 2)
                .collect(toList()));
        // [12]

        System.out.println("------------------------------------");
        System.out.println(list.stream()
                .dropWhile(i -> i > 2)
                .collect(toList()));
        // [2, 9, 3, 3, 5]

        System.out.println("------------------------------------");
        System.out.println(list.stream()
                .distinct()
                .collect(toList()));

        System.out.println("------------------------------------");
        System.out.println(list.stream()
                .limit(2)
                .collect(toList()));

        System.out.println("------------------------------------");
        System.out.println(list.stream()
                .skip(2)
                .collect(toList()));

        // 使用 flatMap 找出单词列表中各不相同的字符
        System.out.println("------------------------------------");
        List<String> words = Arrays.asList("Hello", "World");
        List<String> uniqueCharacters = words.stream()
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(toList());
        System.out.println(uniqueCharacters);
        // [H, e, l, o, W, r, d]

        // 给定两个数字列表，如何返回所有的数对呢？
        // 例如，给定[1,2,3]和[3,4]，应返回 [(1,3), (1,4), (2,3), (2,4), (3,3), (3,4)]
        System.out.println("------------------------------------");
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);
        List<int[]> pairs = numbers1.stream()
                .flatMap(i -> numbers2.stream()
                        .map(j -> new int[]{i, j}))
                .collect(toList());
        pairs.forEach(pair -> {
            System.out.println(Arrays.toString(pair));
        });

        System.out.println((1 % 1));
        System.out.println((2 % 1));
        System.out.println((3 % 1));
        System.out.println((3.1 % 1));
    }
}
