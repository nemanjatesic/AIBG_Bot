import com.google.gson.Gson;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        String JSON = loadJSONFromFile("src\\Podaci.json");
        Gson gson = new Gson();
    }

    private static String loadJSONFromFile(String path) throws Exception{
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine());
        }
        return sb.toString();
    }
}
