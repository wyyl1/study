package wyyl1.chap01;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 3.8.1 比较器复合
 * @author aoe
 * @date 2020/12/25
 */
public class Chap03Sort {

    public static void main(String[] args) {
        List<Apple> inventory = new ArrayList<>();
        inventory.addAll(Arrays.asList(
                new Apple(80, Color.GREEN_2),
                new Apple(155, Color.GREEN_2),
                new Apple(155, Color.YELLOW_3),
                new Apple(155, Color.RED_1),
                new Apple(120, Color.RED_1)
        ));

        inventory.sort(Comparator.comparing(Apple::getWeight));
        System.out.println(inventory);

        inventory.sort(Comparator.comparing(Apple::getWeight)
                .reversed());
        System.out.println(inventory);

        inventory.sort(Comparator.comparing(Apple::getWeight)
                .reversed()
                .thenComparing(Apple::getColor));
        System.out.println(inventory);
    }

    private static class Apple {
        private int weight = 0;
        private Color color;

        public Apple(int weight, Color color) {
            this.weight = weight;
            this.color = color;
        }

        public int getWeight() {
            return weight;
        }

        public Color getColor() {
            return color;
        }

        @Override
        public String toString() {
            return String.format("Apple{color=%s, weight=%d}", color, weight);
        }
    }

    private enum Color {
        RED_1,
        GREEN_2,
        YELLOW_3
    }
}
