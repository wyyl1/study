package wyyl1.chap01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

/**
 * @author aoe
 * @date 2021/1/5
 */
public class Chap08Test {
    public static void main(String[] args) {
        computingOnMaps();
    }

    private static void computingOnMaps() {
        Map<String, List<String>> friendsToMovies = new HashMap<>();

        String friend = "小刚";
        friendsToMovies.clear();
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
}
