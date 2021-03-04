package wyyl1;

import java.util.Random;
import java.util.function.IntUnaryOperator;

/**
 * @author aoe
 * @date 2021/3/2
 */
public class Currying {

    public static void main(String[] args) {
        System.out.println("正常思维计算 " + getCoin(1,2,lucky()));

        IntUnaryOperator buyCar = curryingCoin(100_000, 1);
        System.out.println("柯里化（减少一个参数） 买车 " + buyCar.applyAsInt(lucky()));

        IntUnaryOperator buyGun = curryingCoin(300_000, 6);
        System.out.println("柯里化（减少一个参数） 买枪 " + buyGun.applyAsInt(lucky()));

        IntUnaryOperator buyFruit = curryingCoin(2_000, 10);
        System.out.println("柯里化（减少一个参数） 买水果 " + buyFruit.applyAsInt(lucky()));

        // 参数：用户等级
        IntUnaryOperator buyMotorcycle = curryingBuy(1);
        // 参数：价格
        IntUnaryOperator luckyMotorcycle = curryingLucky(buyMotorcycle.applyAsInt(555_000));
        // 参数：幸运值
        System.out.println("柯里化（只有一个参数） 买摩托车 " + luckyMotorcycle.applyAsInt(lucky()));
    }

    // 正常思维
    /**
     * 获得金币奖励
     * @param price 商品价格
     * @param userLevel 用户等级
     * @param lucky 幸运值
     * @return
     */
    static int getCoin(int price, int userLevel, int lucky){
        return (price + userLevel) * lucky;
    }

    // 柯里化入门：减少一个参数
    static IntUnaryOperator curryingCoin(int price, int userLevel){
        // 金币奖励所有计算逻辑
        return (int lucky) -> (price + userLevel) * lucky;
    }

    // 柯里化进阶：拆分为一个参数
    // 购买逻辑
    static IntUnaryOperator curryingBuy(int userLevel) {
        // 金币奖励：价格、用户等级之间的逻辑
        return (int price) -> price + userLevel;
    }

    // 幸运值逻辑
    static IntUnaryOperator curryingLucky(int buyPrice) {
        // 金币奖励所有计算逻辑 = 购买逻辑 + 幸运值逻辑
        return (int lucky) -> buyPrice * lucky;
    }

    static int lucky(){
        Random random = new Random();
        return random.ints().findAny().getAsInt();
    }
}
