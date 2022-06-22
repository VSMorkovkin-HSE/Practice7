import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws IOException {
        URL url = makeUrl();
        String jsonString = makeJsonString(url);

        writeJsonToFile("file.json", jsonString);

        String text = getTextFromJson(jsonString);

        if (text != null) {
            System.out.println(text);
        }
    }

    private static URL makeUrl() throws MalformedURLException {
        Scanner scan = new Scanner(System.in);

        System.out.println("Введите поисковый запрос на английском:");
        String keyword = scan.nextLine();

        keyword = keyword.replace(' ', '_');

        URL url = new URL(
                "https://en.wikipedia.org/w/api.php?" +
                        "action=query" +
                        "&prop=extracts" +
                        "&exsentences=10" +
                        "&exlimit=1" +
                        "&titles=" + keyword +
                        "&explaintext=1" +
                        "&format=json" +
                        "&formatversion=2"
        );
        return url;
    }

    private static String makeJsonString(URL url) throws IOException {
        InputStream in = url.openStream();
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);

        StringBuilder jsonStringBuilder = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            jsonStringBuilder.append(line);
        }

        return jsonStringBuilder.toString();
    }

    private static void writeJsonToFile(String filename, String jsonString) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        bw.append(jsonString);
        bw.close();
    }

    private static String getTextFromJson(String jsonString) {
        String text = null;
        try {
            JSONObject jo = new JSONObject(jsonString);
            text = jo.getJSONObject("query").getJSONArray("pages").getJSONObject(0).getString("extract");
        } catch (JSONException e) {
            System.out.println("incorrect keyword");
        }
        return text;
    }
}