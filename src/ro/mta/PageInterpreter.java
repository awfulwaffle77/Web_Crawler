package ro.mta;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageInterpreter {
    private Map<String, String> rules = new HashMap<>();
    private int maxRules = 0;

    public PageInterpreter(String filename) throws IOException {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);

            for(String line: lines){
                rules.put(line.split(":")[0], line.split(":")[1]);
                maxRules++;
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

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
