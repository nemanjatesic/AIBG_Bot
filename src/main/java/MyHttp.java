import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MyHttp {

    // one instance, reuse
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public static String sendConnect(int playerId, int gameId) throws Exception {
        String string = "http://localhost:9080/game/play?playerId=" + playerId;
        string += "&gameId=" + gameId;
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(string))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // print status code
        //System.out.println(response.statusCode());

        // print response body
        //System.out.println(response.body());

        return response.body();
    }

    public static String sendAction(int playerId, int gameId, String action) throws Exception {
        String string = "http://localhost:9080/doAction?playerId=" + playerId;
        string += "&gameId=" + gameId;
        string += "&action=" + action;

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(string))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // print status code
        //System.out.println(response.statusCode());

        // print response body
        //System.out.println(response.body());

        return response.body();
    }

    private void sendPost() throws Exception {

        // form parameters
        Map<Object, Object> data = new HashMap<>();
        data.put("username", "abc");
        data.put("password", "$2a$08$ZTCHu7kFlUzBjdTg/RJ7Buz2gz7ktSDCCsj3Bp8YxWKXqlzG3V90i");
        data.put("custom", "secret");
        data.put("email","Nemanja2017@gmail.com");
        data.put("ts", System.currentTimeMillis());

        HttpRequest request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(data))
                .uri(URI.create("http://localhost:8081/login"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // print status code
        System.out.println(response.statusCode());

        // print response body
        System.out.println(response.body());

    }

    private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        System.out.println(builder.toString());
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}
