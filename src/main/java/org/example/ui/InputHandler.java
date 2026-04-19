package org.example.ui;

import java.util.Scanner;

public class InputHandler {
    Scanner scanner;

    public InputHandler() {
        scanner = new Scanner(System.in);
    }

    public String readSingleChoice(String output, String... allowedWords) {
        while(true) {
            System.out.println(output);
            String input = scanner.nextLine().trim();

            for (String word : allowedWords) {
                if (input.equals(word)) {
                    return input;
                }
            }
            System.out.println("Niepoprawny wybor, sprobuj ponownie.");
        }
    }

    public String[] getMultipleStrings(String output, int amountOfWords) {
        while(true) {
            System.out.println(output);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Wartosc nie moze byc pusta.");
                continue;
            }
            String[] split = input.split(" ");
            if (split.length == amountOfWords) {
                return split;
            } else {
                System.out.println("Podano zla ilosc slow.");
            }
        }
    }
}
