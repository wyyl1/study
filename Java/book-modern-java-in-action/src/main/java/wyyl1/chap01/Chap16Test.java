package wyyl1.chap01;

import modernjavainaction.chap16.Shop;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author aoe
 * @date 2021/2/20
 */
public class Chap16Test {

    private final List<Shop> shops = Arrays.asList(
            new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll"),
            new Shop("ShopEasy"));

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

    public static void main(String[] args) {
        Chap16Test test = new Chap16Test();
        List<String> list = test.findPrices("car");
        System.out.println(list);
    }
}
