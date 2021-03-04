package wyyl1;

import modernjavainaction.chap16.Shop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author aoe
 * @date 2021/2/20
 */
public class Chap16Test {

    private final List<Shop> shops = getShops(100);

    private final Executor executor =
            Executors.newFixedThreadPool(Math.min(shops.size(), 100), r -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            });

    public List<String> findPrices(String product) {
        List<CompletableFuture<String>> priceFutures =
                shops.stream()
                        // 使用 CompletableFuture 以异步方式及时每种商品的价格
                        .map(shop -> CompletableFuture.supplyAsync(
                                () -> shop.getName() + " price is" + shop.getPrice(product)
                        ))
                        .collect(Collectors.toList());
        return priceFutures.stream()
                // 等待所有异步操作结束
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public List<String> findPricesBest(String product) {
        List<CompletableFuture<String>> priceFutures =
                shops.stream()
                        // 使用 CompletableFuture 以异步方式及时每种商品的价格
                        .map(shop -> CompletableFuture.supplyAsync(() -> shop.getName() + " price is" + shop.getPrice(product), executor)
                        )
                        .collect(Collectors.toList());
        return priceFutures.stream()
                // 等待所有异步操作结束
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        Chap16Test test = new Chap16Test();
        test.findPricesBest("car");
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("findPrices done in " + duration + " msecs");
        // 15209 1181

        System.out.println("img2azLkxSdQQi6X09KDWm8fr-1t@NT0".length());
        System.out.println("fdd25f95-4892-4d6b-aca9-7939bc6e9baa-1486198766695".length());
    }

    private List<Shop> getShops(int count) {
        List<Shop> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            list.add(new Shop("Shop" + i));
        }
        return list;
    }

}
