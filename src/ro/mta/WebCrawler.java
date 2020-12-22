package ro.mta;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import ro.mta.commands.AbstractCommand;
import ro.mta.commands.CommandFactory;
import ro.mta.Logger;

import static ro.mta.Sitemap.*;

public class WebCrawler {

    private static PostProcessor postProcessor;

    private HashMap<String, String> rules = new HashMap<>();

    public static void main(String[] args) throws IOException {

        interpretConfig("f.conf");

        AbstractCommand command = CommandFactory.CreateCommand(args);
        command.execute();
    }

    /**
     * interpretare fisier de configurare
     * se va respecta formatul "regula":"valoare" , urmand ca regulile din fisier sa fie salvate intr-un
     * obiect de forma HashMap <String, String>
     */

    public static void interpretConfig(String filename) throws FileNotFoundException {
        List<String> list = new ArrayList<String>();
        List<String> namesList = new ArrayList<String>();
        List<String> valsList = new ArrayList<String>();
        FileReader reader = new FileReader(filename);
        try (BufferedReader in = new BufferedReader(reader)) {
            String line;
            HashMap<String, String> rules = new HashMap<String, String>();
            while ((line = in.readLine()) != null) {
                String[] pair = line.split("=");
                rules.put(pair[0], pair[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
