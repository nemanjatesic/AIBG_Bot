import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
//        String JSON = loadJSONFromFile("src" + File.separator +"Podaci.json");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.enableComplexMapKeySerialization();
        Gson gson = gsonBuilder.create();

        String connect = MyHttp.sendConnect(Constants.PLAYER_ID, Constants.GAME_ID);
        Game game = gson.fromJson(connect, Game.class);
        game.init(gson);
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
