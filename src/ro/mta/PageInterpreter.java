package ro.mta;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageInterpreter {

    /**
     * rules - un map de tipul "cheie":"valoare", citit din fisierul robots.txt
     * maxRules - numarul de reguli care exista in fisierul robots.txt, adica nr de linii
     */
    private Map<String, String> rules = new HashMap<>();
    private int maxRules = 0;

    /**
     *
     * @param filename - numele fisierului din care se va citi(robots.txt)
     * @throws IOException
     */
    public PageInterpreter(String filename) throws IOException {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
            lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);

            for(String line: lines){
                rules.put(line.split(":")[0], line.split(":")[1]);
                maxRules++;
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param htmlString - contentul html, cu fiecare linie terminata in \n sau \r
     * @return - retureaza true daca pagina respecta formatul regulilor din fisier, false daca nu
     */
    boolean isPageCorrect(String htmlString){ // if all rules apply, returns true
        int correctiveness = 0;

       if(rules.get("contains") != null){
           // contains:cerc,mta,romania
           StringBuilder toMatch = new StringBuilder();
           String finalMatch;
           for(String x : rules.get("contains").split(",")){
               toMatch.append(x);
               toMatch.append('|');
           }
           finalMatch = toMatch.substring(0,toMatch.length()-1);
           String patternString = "\\b(" + finalMatch + ")\\b";
           Pattern pattern = Pattern.compile(patternString);

           Matcher matcher = pattern.matcher(htmlString);

           if (matcher.find()) { // finds at least one of the words
               correctiveness++;
           }

       }
       if(rules.get("max_lines") != null){
           int maxSize = Integer.parseInt(rules.get("max_lines"));
           int lines = htmlString.split("\r\n|\r|\n").length;

           if(lines < maxSize){
               correctiveness++;
           }

       }

        return correctiveness == maxRules;
    }

    /**
     *
     * @param htmlString - contentul html, cu fiecare linie terminata in \n sau \r
     * @return - returneaza stringul html, formatat dupa reguli
     */
    String formatPages(String htmlString){
        StringBuilder finalPage = new StringBuilder();

        String[] splittedHtmlString = htmlString.split("\r\n|\r|\n");
        int maxLines = splittedHtmlString.length;

        if(rules.get("max_lines") != null){
            maxLines = Integer.parseInt(rules.get("max_lines"));
        }

        String[] maxLinedString = new String[maxLines];
        for(int i = 0; i < maxLines; i++){
            maxLinedString[i] = splittedHtmlString[i];
        }

        if(rules.get("contains") != null){
            String[] containsList = rules.get("contains").split(",");
            for(String line : maxLinedString){
              for(String element : containsList){
                  if(line.contains(element)){
                      finalPage.append(line);
                      break;
                  }
              }
          }
       }
        return finalPage.toString();
    }
}
