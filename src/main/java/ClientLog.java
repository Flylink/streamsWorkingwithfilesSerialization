import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {

    List<String[]> history = new ArrayList<>();
    String[] firstString = "productNum,amount".split(",");

    public void log(int productNum, int amount) {
        history.add(new String[]{String.valueOf(productNum), String.valueOf(amount)});
    }

    public void exportAsCSV(File txtFile) {
        try (CSVWriter writer = new CSVWriter((new FileWriter(txtFile, true)), ',',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            if (txtFile.length() == 0) {
                writer.writeNext(firstString);
            }
            writer.writeAll(history);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}