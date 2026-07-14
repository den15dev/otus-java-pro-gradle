package ru.otus.java.pro.hw04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        String[] words = {"One", "Two", "Three", "Four", "Five"};

        swap(words, 2, 3);
        System.out.println(Arrays.toString(words));

        ArrayList<String> arrList = toArrayList(words);
        System.out.println(arrList);

        String[] words2 = {"One", "Two", "Three", "Four", "Five", "Three", "Six", "Five", "Seven", "Five"};
        Map<String, Integer> occurrences = countOccurrences(words2);
        System.out.println(occurrences);
        System.out.println(occurrences.keySet());
    }


    public static <T> void swap(T[] array, int index1, int index2) {
        T temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }


    public static <T> ArrayList<T> toArrayList(T[] arr) {
        return new ArrayList<>(Arrays.asList(arr));
    }


    public static Map<String, Integer> countOccurrences(String[] arr) {
        Map<String, Integer> map = new HashMap<>();
        for (String word : arr) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }

        return map;
    }
}
