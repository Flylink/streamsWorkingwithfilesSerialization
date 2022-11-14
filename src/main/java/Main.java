import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {

        boolean shouldLoad = true;
        String loadFile = null;
        String loadFormat = null;
        boolean shouldSave = true;
        String saveFile = null;
        String saveFormat = null;
        boolean shouldLog = true;
        String logFile = null;

        try {
            File xmlFile = new File("shop.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList loadList = doc.getElementsByTagName("load");
            for (int i = 0; i < loadList.getLength(); i++) {
                Node node = loadList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    shouldLoad = Boolean.parseBoolean(eElement.getElementsByTagName("enabled")
                            .item(0).getTextContent());
                    loadFile = eElement.getElementsByTagName("fileName")
                            .item(0).getTextContent();
                    loadFormat = eElement.getElementsByTagName("format")
                            .item(0).getTextContent();
                }
            }

            NodeList saveList = doc.getElementsByTagName("save");
            for (int i = 0; i < saveList.getLength(); i++) {
                Node node = saveList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    shouldSave = Boolean.parseBoolean(eElement.getElementsByTagName("enabled")
                            .item(0).getTextContent());
                    saveFile = eElement.getElementsByTagName("fileName")
                            .item(0).getTextContent();
                    saveFormat = eElement.getElementsByTagName("format")
                            .item(0).getTextContent();
                }
            }

            NodeList logList = doc.getElementsByTagName("log");
            for (int i = 0; i < saveList.getLength(); i++) {
                Node node = logList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    shouldLog = Boolean.parseBoolean(eElement.getElementsByTagName("enabled")
                            .item(0).getTextContent());
                    logFile = eElement.getElementsByTagName("fileName")
                            .item(0).getTextContent();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        File txtFile = new File("basket.txt");
        if (loadFile == null) throw new AssertionError();
        File jsonFile = new File(loadFile);
        if (logFile == null) throw new AssertionError();
        File csvFile = new File(logFile);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        String[] products = new String[]{"Яблоко", "Помидор", "Апельсин", "Груша"};
        int[] prices = new int[]{30, 50, 70, 40};
        Basket basket = new Basket(products, prices);
        ClientLog clientLog = new ClientLog();

        if (shouldLoad && loadFormat.equals("json")) {
            if (!jsonFile.createNewFile()) {
                try (JsonReader reader = new JsonReader(new FileReader(jsonFile))) {
                    basket = gson.fromJson(reader, Basket.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                basket.printCart();
            }
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
            if (shouldLog) {
                clientLog.exportAsCSV(csvFile);
            }
            if (saveFormat != null) {
                if (shouldSave && saveFormat.equals("json")) {
                    try (FileWriter jsonWriter = new FileWriter(jsonFile)) {
                        jsonWriter.write(gson.toJson(basket));
                        jsonWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (shouldLoad) {
                    basket.saveTxt(txtFile);
                }
            }
        }
    }
}