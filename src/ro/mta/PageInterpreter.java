package ro.mta;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class PageInterpreter {
    private Map<String, String> rules;
    List<String> lines;

    public PageInterpreter(String filename) throws IOException {
        try {
            lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
            for(String line: lines){
                rules.put(line.split(":")[0], line.split(":")[1]);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    boolean isPageCorrect(List<String> htmlString){
       if(rules.get("contains") != null){
          String val = rules.get("contains");
       }

       return true;
    }

    String formatPages(){
        return "formatted";
    }
}
