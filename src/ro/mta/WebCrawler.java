package ro.mta;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

import ro.mta.commands.AbstractCommand;
import ro.mta.commands.CommandFactory;

public class WebCrawler {

    private static PostProcessor postProcessor;


    public static void main(String[] args) {

        AbstractCommand command = CommandFactory.CreateCommand(args);
        command.execute();

    }
    
    public static void interpretConfig(String filename) {
        List<String> list = new ArrayList<String>();
        List<String> namesList = new ArrayList<String>();
        List<String> valsList = new ArrayList<String>();
 	FileReader reader = new FileReader(filename);
        try(BufferedReader in = new BufferedReader(reader))
        {
            String line;
            while((line = in.readLine())!=null){
                String[] pair = line.split("=");
               namesList.add(pair[0]);
                valsList.add(pair[1]);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }	
}
