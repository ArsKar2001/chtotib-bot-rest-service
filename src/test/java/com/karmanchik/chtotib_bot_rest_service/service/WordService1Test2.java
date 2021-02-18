package com.karmanchik.chtotib_bot_rest_service.service;

import com.karmanchik.chtotib_bot_rest_service.service.word.WordServiceImpl;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class WordService1Test2 {
    private static final File FILE_1 = new File("src\\main\\resources\\files\\Расписание 1-2 курс 2 семестр 2020-2021 уч год.docx");
    private static final File FILE_2 = new File("src\\main\\resources\\files\\Расписание 3-4 курса 2 семестр 2020-2021 уч год.docx");
    WordServiceImpl wordService = new WordServiceImpl();

    @Test
    void test_File1() {
        try (FileInputStream stream = new FileInputStream(FILE_1)) {
            final String text = wordService.getText(stream);
            final var lists = textToLists(text);
            lists.forEach(strings -> strings.forEach(System.out::println));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_File2() {
        try (FileInputStream stream = new FileInputStream(FILE_2)) {
            final String text = wordService.getText(stream);
            final var lists = textToLists(text);
            lists.forEach(strings -> strings.forEach(System.out::println));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<List<String>> textToLists(String text) {
        final String rText = text.replace('\t', ';');
        final String[] sText = rText.split("\n");

        List<String> stringList = new LinkedList<>(Arrays.asList(sText));
        stringList.removeIf(String::isBlank);

        final var listLists = createListLists(stringList);
        final var sListLists = splitListLists(listLists);
        final var cListLists = correctingListLists(sListLists);
        return cListLists;
    }

    private List<List<String>> createListLists(List<String> stringList) {
        List<List<String>> listLists = new LinkedList<>();
        List<String> list = new LinkedList<>();

        Pattern pattern = Pattern.compile("/\\S\\.\\S\\.\\s.+?/");
        final String removeStr = "\u0424.\u0418.\u041E.";
        stringList.removeIf(s -> s.contains(removeStr));
        for (String str : stringList) {
            if (pattern.matcher(str).find()) {
                listLists.add(new LinkedList<>(list));
                list.clear();
            } else {
                list.add(str);
            }
        }
        listLists.get(0).addAll(list);
        return listLists;
    }

    private List<List<String>> splitListLists(List<List<String>> listLists) {
        List<String> listRight = new LinkedList<>();
        List<String> listLeft = new LinkedList<>();
        List<List<String>> lls = new LinkedList<>();

        for (var list : listLists) {
            for (String str : list) {
                List<String> ls1 = new LinkedList<>();
                List<String> ls2 = new LinkedList<>();
                final String[] splitStr = str.split(";", -5);
                for (int i = 0; i < splitStr.length; i++) {
                    if (i < splitStr.length / 2) ls1.add(splitStr[i].trim().isBlank() ? "-" : splitStr[i].trim());
                    else ls2.add(splitStr[i].trim().isBlank() ? "-" : splitStr[i].trim());
                }
                final String s1 = String.join(";", ls1).trim();
                final String s2 = String.join(";", ls2).trim();

                listLeft.add(s1);
                listRight.add(s2);
            }
            lls.add(new LinkedList<>(listLeft));
            lls.add(new LinkedList<>(listRight));
            listLeft.clear();
            listRight.clear();
        }
        return lls;
    }

    private List<List<String>> correctingListLists(List<List<String>> sListLists) {
        List<String> ls = new LinkedList<>();
        List<List<String>> lls = new LinkedList<>();
        Pattern pt = Pattern.compile("[А-Я]+(\\s+|\\s+-|-|-\\s+|\\s+-\\s+)\\d{1,2}(\\s+|\\s+-|-|-\\s+|\\s+-\\s+)\\S{1,2}");
        String s1 = "";
        String s2 = "";

        sListLists.removeIf(list -> list.get(0).split("\\s").length < 2);
        for (var list : sListLists) {
            for (String str : list) {
                Matcher matcher = pt.matcher(str);
                if (matcher.find()) {
                    s1 = str.substring(matcher.start(), matcher.end());
                } else {
                    final String[] strings = str.split(";", -5);
                    if (!strings[0].equals("-")) s2 = strings[0];
                    else {
                        str = str.substring(1);
                        str = s2 + str;
                    }
                    str = s1 + ";" + str;
                    ls.add(str);
                }
            }
            lls.add(new LinkedList<>(ls));
            ls.clear();
        }
        return lls;
    }
}