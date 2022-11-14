import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException{
        File file = new File("basket.json");
        File csvFile = new File("log.csv");
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String[] products = new String[]{"Яблоко", "Помидор", "Апельсин", "Груша"};
        int[] prices = new int[]{30, 50, 70, 40};
        Basket basket = new Basket(products, prices);
        ClientLog clientLog = new ClientLog();

        if (!file.createNewFile()) {
            try (JsonReader reader = new JsonReader(new FileReader(file))) {
                basket = gson.fromJson(reader, Basket.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            basket.printCart();
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Список возможных товаров для покупки");
        for (int i = 0; i < basket.getProducts().length; i++) {
            System.out.println((i + 1) + ". " + products[i] + " " + prices[i] + " руб/шт");
        }

        while (true) {
            System.out.println("Выберите товар и количество или введите `end`");
            String input = scanner.nextLine();
            if (input.equals("end")) {
                break;
            }
            try {
                String[] inputArr = input.split(" ");
                if (inputArr.length != 2) {
                    System.out.println("Ошибка ввода! Должно быть введено 2 числа или end!");
                    continue;
                }
                int productNum = Integer.parseInt(inputArr[0]) - 1;
                if ((productNum + 1) > products.length || (productNum + 1) <= 0) {
                    System.out.println("Номер товара от 1 до " + products.length);
                    continue;
                }
                int amount = Integer.parseInt(inputArr[1]);
                if (amount <= 0) {
                    System.out.println("Количество товара должно быть больше 0!");
                }
                basket.addToCart(productNum, amount);
                clientLog.log(++productNum, amount);
                gson.toJson(basket);
                basket.printCart();
            } catch (NumberFormatException e) {
                System.out.println("Введите два числа, а было введено: " + input);
                continue;
            }
            clientLog.exportAsCSV(csvFile);
            try (FileWriter jsonWriter = new FileWriter(file)) {
                jsonWriter.write(gson.toJson(basket));
                jsonWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}