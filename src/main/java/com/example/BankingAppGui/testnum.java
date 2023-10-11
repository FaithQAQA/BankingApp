package com.example.BankingAppGui;
public class testnum {
    public static void main(String[] args) {
        int cardnum = 34342423;
        String s = String.valueOf(cardnum);

        // Create a StringBuilder to build the masked string
        StringBuilder maskedString = new StringBuilder();

        // Iterate over the characters in the original string
        for (int i = 0; i < s.length() - 4; i++) {
            // Replace characters with asterisks (*) except the last four
            maskedString.append('*');
        }

        // Append the last four characters
        maskedString.append(s.substring(s.length() - 4));

        // Convert the StringBuilder back to a String
        String maskedCardNumber = maskedString.toString();

        System.out.println(maskedCardNumber);
    }
}
