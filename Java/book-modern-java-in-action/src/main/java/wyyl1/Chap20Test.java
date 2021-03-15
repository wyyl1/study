package wyyl1;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author aoe
 * @date 2021/3/8
 */
public class Chap20Test {

    public static void main(String[] args) {
        unmodifiable();
    }

    public Chap20Test() {
    }

    private static void unmodifiable(){
        Set<Integer> numbers = new HashSet<>();
        Set<Integer> newNumbers = Collections.unmodifiableSet(numbers);

        newNumbers.add(1);
        System.out.println(newNumbers.size());
    }
}
