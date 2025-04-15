package harbour.anna;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JavaWebhook {
    public static void main(String[] args) {
        try {
            String webhook = "https://webhook.site/8c2a7c66-2ffa-4b2c-8075-a0e700055808";
            String payload = "{\"content\": The thing is available\"}";
            // Set up connection
            URL url = new URL(webhook);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            POST REQUEST
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

//            SEND PAYLOAD
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Check response
            int responseCode = connection.getResponseCode();
            System.out.println("Webhook POST response code: " + responseCode);

            if (responseCode == 200) {
                System.out.println("Success! Webhook sent.");
            } else {
                System.out.println("Something went wrong.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}