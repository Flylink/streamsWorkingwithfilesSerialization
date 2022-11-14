import java.io.*;

public class Basket {

    protected String[] products;
    protected int[] prices;
    protected int totalPrice;
    protected int[] amountOfProductsInBasket;

    public Basket(String[] products, int[] prices) {
        this.products = products;
        this.prices = prices;
        this.amountOfProductsInBasket = new int[products.length];
    }

    public void addToCart(int productNum, int amount) {
        amountOfProductsInBasket[productNum] += amount;
    }

    public void printCart() {
        totalPrice = 0;
        System.out.println("Ваша корзина:");
        for (int i = 0; i < products.length; i++) {
            if (amountOfProductsInBasket[i] != 0) {
                totalPrice = totalPrice + (prices[i] * amountOfProductsInBasket[i]);
                System.out.println(products[i] + " " + amountOfProductsInBasket[i] + " шт. " + prices[i] + " руб/шт. " + (amountOfProductsInBasket[i] * prices[i]) + " рублей в сумме");
            }
        }
        System.out.println("Итого: " + totalPrice + " рублей");
    }

    public void saveTxt(File textFile) {
        try (BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(textFile)))) {
            for (String product : products) {
                buff.write(product + ";");
            }
            buff.write("\n");
            for (int price : prices) {
                buff.write(price + ";");
            }
            buff.write("\n");
            for (int j : amountOfProductsInBasket) {
                buff.write(j + ";");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Basket loadFromTxtFile(File textFile) {
        String productArr = " ";
        String pricesString = " ";
        String amountString = " ";
        try (BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(textFile)))) {
            while (buff.ready()) {
                productArr = buff.readLine();
                pricesString = buff.readLine();
                amountString = buff.readLine();
                buff.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] products = productArr.split(";");
        String[] pricesArr = pricesString.split(";");
        int[] prices = new int[pricesArr.length];
        for (int i = 0; i < prices.length; i++) {
            prices[i] = Integer.parseInt(pricesArr[i]);
        }
        String[] amountArr = amountString.split(";");
        int[] amount = new int[amountArr.length];
        for (int i = 0; i < amount.length; i++) {
            amount[i] = Integer.parseInt(amountArr[i]);
        }
        Basket basket = new Basket(products, prices);
        basket.amountOfProductsInBasket = amount;
        return basket;
    }

    public String[] getProducts() {
        return products;
    }

    public int[] getPrices() {
        return prices;
    }
}