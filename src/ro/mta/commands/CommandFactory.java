package ro.mta.commands;


import java.util.ArrayList;
import java.util.List;

public class CommandFactory {

    //Mesaje de eroare
    private static final String[] noCommandError = new String[]{"Please enter one of the existing commands"};
    private static final String[] wrongCommandIdentifierError = new String[]{"The command inserted was wrong"};

    //List de cuvinte cheie pentru comparatie
    private static final List<String> keywords = new ArrayList<>() {
        {
            add("crawl");
            add("list");
            add("search");
        }
    };

    public static AbstractCommand CreateCommand(String[] args){

        if (args.length == 0){
            return new InvalidCommand(noCommandError);
        }

        String cmdIdentifier = args[0];

        if (!keywords.contains(cmdIdentifier)){
            return new InvalidCommand(wrongCommandIdentifierError);
        }

        AbstractCommand toRet = null;

        //Toate argumentele sunt null deoarece nu sunt implementate comenzile
        switch (cmdIdentifier){
            case "crawl":{
                toRet = new CrawlCommand(args);
                break;
            }
            case "list":{
                toRet = new ListCommand(args);
                break;
            }
            case "search":{
                toRet = new SearchCommand(args);
                break;
            }
        }
        return toRet;
    }

}
