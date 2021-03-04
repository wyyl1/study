package wyyl1;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Map.entry;

/**
 * @author aoe
 * @date 2021/1/5
 */
public class Chap08Test {
    /*public static void main(String[] args) {
//        computingOnMaps();
//        computeIfPresentTest();
//        computeTest();
//        removeTest();
//        replaceAllTest();
//        mergingMaps();
//        mergingTest();
    }*/

    public static void main(String[] args) {
        String[] list = new String[]{"a", "b,c", "d,e,f", "g"};
        List<String> result = Arrays.stream(list)
                // 接受一个 Lambda，将元素转换成其他形式或提取信息
                .map(chars -> chars.split(","))
                // 让你把一个流中的每个值都换成另一个流，然后把所有的流连接起来成为一个流
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
        System.out.println(result);
        // 输出 [a, b, c, d, e, f, g]

        long random = System.currentTimeMillis() - 1610000000000L;
        System.out.println(random);

        String name = "两个3两个两个两个ss";
        System.out.println(name.length());
    }

    private static void computingOnMaps() {
        Map<String, List<String>> friendsToMovies = new HashMap<>();

        String friend = "小刚";
        friendsToMovies.computeIfAbsent(friend, name -> new ArrayList<>())
                .add("星球大战");
        System.out.println(friendsToMovies);
        // {小刚=[星球大战]}
        friendsToMovies.computeIfAbsent(friend, name -> new ArrayList<>())
                .add("星际争霸");
        System.out.println(friendsToMovies);
        // {小刚=[星球大战, 星际争霸]}
        friendsToMovies.computeIfAbsent(friend, name -> new ArrayList<>())
                .add("星际争霸");
        System.out.println(friendsToMovies);
        // {小刚=[星球大战, 星际争霸, 星际争霸]}
    }

    private static void computeIfPresentTest() {
        Map<String, List<String>> gameMap = new HashMap<>();

        String friend = "小刚";
        gameMap.computeIfAbsent(friend, name -> new ArrayList<>())
                .add("星球大战");
        System.out.println(gameMap);
        //{小刚=[星球大战]}
        gameMap.computeIfPresent(friend, (k, v) -> List.of("星际争霸"));
        System.out.println(gameMap);
        // {小刚=[星际争霸]}
        gameMap.computeIfPresent(friend, (k, v) -> List.of("暗黑3", "地球时代"));
        System.out.println(gameMap);
        // {小刚=[暗黑3, 地球时代]}
        gameMap.computeIfPresent("没有这个键", (k, v) -> List.of("消消乐"));
        System.out.println(gameMap);
        // {小刚=[暗黑3, 地球时代]}
    }

    private static void computeTest() {
        Map<String, List<String>> gameMap = new HashMap<>();

        String friend = "小刚";
        gameMap.computeIfAbsent(friend, name -> new ArrayList<>())
                .add("星球大战");
        System.out.println(gameMap);
        //{小刚=[星球大战]}
        gameMap.compute(friend, (k, v) -> List.of("星际争霸"));
        System.out.println(gameMap);
        // {小刚=[星际争霸]}
        gameMap.compute(friend, (k, v) -> List.of("暗黑3", "地球时代"));
        System.out.println(gameMap);
        // {小刚=[暗黑3, 地球时代]}
        gameMap.compute("又来一个键", (k, v) -> List.of("红警"));
        System.out.println(gameMap);
        // {小刚=[暗黑3, 地球时代], 又来一个键=[红警]}
    }

    private static void removeTest(){
        Map<String, String> gameMap = new HashMap<>();
        gameMap.put("暴雪1", "魔兽争霸");
        gameMap.put("暴雪2", "暗黑3");
        gameMap.put("暴雪3", "守望先锋");

        // 防止误删数据
        gameMap.remove("暴雪1", "疯狂的麦克斯");
        System.out.println(gameMap);
        // {暴雪1=魔兽争霸, 暴雪3=守望先锋, 暴雪2=暗黑3}
        gameMap.remove("暴雪1", "魔兽争霸");
        System.out.println(gameMap);
        // {暴雪3=守望先锋, 暴雪2=暗黑3}
    }

    private static void replaceAllTest(){
        Map<String, String> gameMap = new HashMap<>();
        gameMap.put("blizzard1", "starcraft2");
        gameMap.put("blizzard2", "diablo4");
        gameMap.put("blizzard3", "warcraft3");

        gameMap.replaceAll((k, v) -> v.toUpperCase());
        System.out.println(gameMap);
        // {blizzard3=WARCRAFT3, blizzard2=DIABLO4, blizzard1=STARCRAFT2}
        // 不能改变 key 的值
        gameMap.replaceAll((k, v) -> k.toUpperCase());
        System.out.println(gameMap);
        // {blizzard3=BLIZZARD3, blizzard2=BLIZZARD2, blizzard1=BLIZZARD1}
    }

    private static void mergingMaps() {
        Map<String, String> family = Map.ofEntries(
                entry("Teo", "Star Wars"),
                entry("Cristina", "James Bond"));
        Map<String, String> friends = Map.ofEntries(entry("Raphael", "Star Wars"));

        System.out.println("--> Merging the old way, 当键重复时会被覆盖");
        Map<String, String> everyone = new HashMap<>(family);
        everyone.putAll(friends);
        System.out.println(everyone);
        // {Cristina=James Bond, Raphael=Star Wars, Teo=Star Wars}

        Map<String, String> friends2 = Map.ofEntries(
                entry("Raphael", "Star Wars"),
                entry("Cristina", "Matrix"));

        System.out.println("--> Merging maps using merge(), 可以处理键重复时的情况");
        Map<String, String> everyone2 = new HashMap<>(family);
        friends2.forEach((k, v) -> everyone2.merge(k, v, (movie1, movie2) -> movie1 + " & " + movie2));
        System.out.println(everyone2);
        // {Raphael=Star Wars, Cristina=James Bond & Matrix, Teo=Star Wars}
    }

    private static void mergingTest() {
        Map<String, Integer> gameMap = new HashMap<>();
        String gameName = "巫师3";
        Integer count = gameMap.get(gameName);
        if (count == null) {
            gameMap.put(gameName, 1);
        } else {
            gameMap.put(gameName, count + 1);
        }
        System.out.println(gameMap);
        // {巫师3=1}

        // 简化代码
        gameMap.merge(gameName, 1, (k, v) -> v + 1);
        System.out.println(gameMap);
        // {巫师3=2}

        gameMap.merge("英雄无敌", 1, (k, v) -> v + 1);
        System.out.println(gameMap);
        // {英雄无敌=1, 巫师3=2}
    }

}