package ro.mta;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Aceasta clasa filtreaza sau verifica o pagina HTML, in functie
 * de fisierul de configurare. Acest fisier trebuie sa aiba
 * contains:{lista de cuvinte separate prin virgula}
 * max_lines:{numarul maxim de linii pe care vrem sa il aiba o pagina}
 *
 * @author Heroi Dorin
 */
public class PageInterpreter {

    /**
     * rules - un map de tipul "cheie":"valoare", citit din fisierul robots.txt
     * maxRules - numarul de reguli care exista in fisierul robots.txt, adica nr de linii
     */
    private Map<String, String> rules = new HashMap<>();
    private int maxRules = 0;
    Logger logger;

    /**
     * @param filename - numele fisierului din care se va citi(robots.txt)
     * @throws IOException
     */
    public PageInterpreter(String filename) throws IOException {
        try {
            logger = Logger.getInstance();
            List<String> lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
            lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);

            for (String line : lines) {
                rules.put(line.split(":")[0], line.split(":")[1]);
                maxRules++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param htmlString - contentul html, cu fiecare linie terminata in \n sau \r
     * @return - retureaza true daca pagina respecta formatul regulilor din fisier, false daca nu
     */
    boolean isPageCorrect(String htmlString) { // if all rules apply, returns true
        int correctiveness = 0;

        if (rules.get("contains") != null) {
            // contains:cerc,mta,romania
            StringBuilder toMatch = new StringBuilder();
            String finalMatch;
            for (String x : rules.get("contains").split(",")) {
                toMatch.append(x);
                toMatch.append('|');
            }
            finalMatch = toMatch.substring(0, toMatch.length() - 1);
            String patternString = "\\b(" + finalMatch + ")\\b";
            Pattern pattern = Pattern.compile(patternString);

            Matcher matcher = pattern.matcher(htmlString);

            if (matcher.find()) { // finds at least one of the words
                correctiveness++;
            }

        }
        if (rules.get("max_lines") != null) {
            int maxSize = Integer.parseInt(rules.get("max_lines"));
            int lines = htmlString.split("\r\n|\r|\n").length;

            if (lines < maxSize) {
                correctiveness++;
            }

        }

        return correctiveness == maxRules;
    }

    /**
     * @param htmlString - contentul html, cu fiecare linie terminata in \n sau \r
     * @return - returneaza stringul html, formatat dupa reguli
     */
    String formatPages(String htmlString) {
        StringBuilder finalPage = new StringBuilder();

        String[] splittedHtmlString = htmlString.split("\r\n|\r|\n");
        int maxLines = splittedHtmlString.length;

        if (rules.get("max_lines") != null) {
            maxLines = Integer.parseInt(rules.get("max_lines"));
            if (splittedHtmlString.length < maxLines) {
                maxLines = splittedHtmlString.length;
            }
        }

        String[] maxLinedString = new String[maxLines]; // verifica doar primele maxLines linii
        for (int i = 0; i < maxLines; i++) {
            maxLinedString[i] = splittedHtmlString[i];
        }

        StringBuilder toMatch = new StringBuilder();
        String finalMatch;
        for (String x : rules.get("contains").split(",")) {
            toMatch.append(x);
            toMatch.append('|');
        }
        finalMatch = toMatch.substring(0, toMatch.length() - 1);
        String patternString = "\\b(" + finalMatch + ")\\b";
        Pattern pattern = Pattern.compile(patternString);

        Matcher matcher;

        if (rules.get("contains") != null) { // ia doar liniile care contin cel putin unul din cuvintele cheie
            String[] containsList = rules.get("contains").split(",");
            for (String line : maxLinedString) {
                matcher = pattern.matcher(line);
                // CHECK HERE CAUSE IT DOES NOT WORK
                if (matcher.find()) {
                    finalPage.append(line);
                    finalPage.append('\n');
                }
            }
        }
        return finalPage.toString();
    }
}
