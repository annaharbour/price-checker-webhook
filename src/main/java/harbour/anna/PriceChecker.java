package harbour.anna;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PriceChecker {

    public static void main(String[] args) {
        try {
            String itemUrl = "https://www.ebay.com/itm/167402765654";
            double threshold = 700.0;

            // Step 1: curl the page
            URL url = new URL(itemUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder pageContent = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                pageContent.append(line);
            }
            reader.close();

            // Step 2: price check
            String html = pageContent.toString();
            String priceRegex = "x-price-primary.*?\\$([0-9,.]+)";
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(priceRegex);
            java.util.regex.Matcher matcher = pattern.matcher(html);

            if (matcher.find()) {
                String rawPrice = matcher.group(1).replace(",", "");
                double price = Double.parseDouble(rawPrice);

                System.out.println("Found price: $" + price);

                // Step 3: compare
                if (price < threshold) {
                    System.out.println("Thing is cheaper");
                    sendWebhook(price);
                } else {
                    System.out.println("Thing still expensive");
                }
            } else {
                System.out.println("Couldn't find price on page.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // dispatch webhook
    public static void sendWebhook(double price) {
        try {
            String webhookUrl = "https://webhook.site/8c2a7c66-2ffa-4b2c-8075-a0e700055808";
            String message = String.format("{\"content\": \"Price dropped to $%.2f!\"}", price);

            URL url = new URL(webhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = message.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Webhook sent.");
            } else {
                System.out.println("Webhook failed with response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
