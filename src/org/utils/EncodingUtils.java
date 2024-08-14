package org.utils;

import java.util.Base64;

public class EncodingUtils {
    public static String encodeInvitation(String ip, String port) {
        // Concatenate IP and port with a separator
        String data = ip + ":" + port;

        // Encode the concatenated string using Base64
        String encodedData = Base64.getEncoder().encodeToString(data.getBytes());

        // Add the custom prefix "kpt://"
        return "kpt://" + encodedData;
    }

    public static String[] decodeInvitation(String encodedString) {
        // Remove the custom prefix "kpt://"
        String encodedData = encodedString.replace("kpt://", "");

        // Decode the Base64-encoded string
        byte[] decodedBytes = Base64.getDecoder().decode(encodedData);

        // Convert the decoded bytes back to a string
        String decodedString = new String(decodedBytes);

        // Split the decoded string into IP and port using the separator ":"
        String[] decodedArray = decodedString.split(":", 2);

        // Ensure that both IP and port are present
        if (decodedArray.length == 2) {
            return decodedArray;
        } else {
            // Handle invalid input
            throw new IllegalArgumentException("Invalid encoded string");
        }
    }

}
