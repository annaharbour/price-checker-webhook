# Price Checker

## Capture Web Page

Checks price of an item via an itemURL, set a threshhold double.

Make an Http GET request to url and use BufferedReader to read the page and StringBuilder to create a string from the page content.

## Check Price

Use regex to find the price of the item from the string we've created

Convert the raw price to a double 

If price is below the threshold you set, dispatch the webhook

### The Webhook

POST request to webhook URL.
