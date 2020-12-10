package wyyl1.chap01;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aoe
 * @date 2020/12/8
 */
public class Chap01Test {

    public static void main(String[] args) {
        File[] hiddenFiles = new File(".").listFiles(File::isHidden);

        List<Integer> list = Arrays.asList(9, 3, 5);
        list = list.stream().sorted(Comparator.comparing(Integer::intValue, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        System.out.println(list);
    }
}
