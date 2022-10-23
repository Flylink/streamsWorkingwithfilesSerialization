import java.io.*;

public class Basket implements Serializable {
    private static final long serialVersionUID = 1L;

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
        System.out.println("Ваша корзина:");
        for (int i = 0; i < products.length; i++) {
            if (amountOfProductsInBasket[i] != 0) {
                totalPrice = totalPrice + (prices[i] * amountOfProductsInBasket[i]);
                System.out.println(
                        products[i] + " " + amountOfProductsInBasket[i] + " шт. " + prices[i] + " руб/шт. "
                                + (amountOfProductsInBasket[i] * prices[i]) + " рублей в сумме");
            }
        }
        System.out.println("Итого: " + totalPrice + " рублей");
        totalPrice = 0;
    }

    public void saveTxt(File textFile) throws IOException {
        BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(textFile)));
        for (int i = 0; i < amountOfProductsInBasket.length; i++) {
            buff.write(amountOfProductsInBasket[i] + ";");
        }
        buff.close();
    }

    public void saveBin(File file) {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(this);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    static Basket loadFromBinFile(File file) {
        Basket basket = null;
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            basket = (Basket) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return basket;
    }

    public static String loadFromTxtFile(File textFile) throws IOException {
        BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(textFile)));
        String buffString = " ";
        while (buff.ready()) {
            buffString = buff.readLine();
        }
        buff.close();
        return buffString;
    }

    public String[] getProducts() {
        return products;
    }
}