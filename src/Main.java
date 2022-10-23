import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        File file = new File("basket.bin");
        String[] products = new String[]{"Яблоко", "Помидор", "Апельсин", "Груша"};
        int[] prices = new int[]{30, 50, 70, 40};
        Basket basket = new Basket(products, prices);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Список возможных товаров для покупки");
        for (int i = 0; i < basket.getProducts().length; i++) {
            System.out.println((i + 1) + ". " + products[i] + " " + prices[i] + " руб/шт");
        }
        if (!file.createNewFile()) {
            basket = Basket.loadFromBinFile(file);
            basket.printCart();
        }

        int productCount;
        int amount;
        while (true) {
            System.out.println("Выберите товар и количество или введите `end`");
            String input = scanner.nextLine();
            if (input.equals("end")) {
                break;
            }
            try {
                String[] inputArr = input.split(" ");
                if (inputArr.length != 2) {
                    System.out.println("Ошибка ввода! Должно быть введено 2 числа!");
                    continue;
                }
                productCount = Integer.parseInt(inputArr[0]) - 1;
                if ((productCount + 1) > products.length || (productCount + 1) <= 0) {
                    System.out.println("Номер товара от 1 до " + products.length);
                    continue;
                }
                amount = Integer.parseInt(inputArr[1]);
                if (productCount <= 0) {
                    System.out.println("Количество товара должно быть больше 0!");
                }
                basket.addToCart(productCount, amount);
            } catch (NumberFormatException e) {
                System.out.println("Введите два числа, а было введено: " + input);
                continue;
            }
            basket.addToCart(productCount, amount);
            basket.printCart();
            basket.saveBin(file);
        }
    }
}