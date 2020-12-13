package ro.mta;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import ro.mta.commands.AbstractCommand;
import ro.mta.commands.CommandFactory;

import static ro.mta.Sitemap.*;

public class WebCrawler {

    private static PostProcessor postProcessor;

    private HashMap<String, String> rules = new HashMap<>();

    public static void main(String[] args) throws IOException {

        interpretConfig("f.conf");
       // PageInterpreter pageInterpreter = new PageInterpreter("F:\\Facultate\\IP\\Web_Crawler\\robots.txt");
       // pageInterpreter.isPageCorrect(s);
        AbstractCommand command = CommandFactory.CreateCommand(args);
        command.execute();


        String fileToAppend = "C:\\Users\\Bogdan\\Documents\\GitHub\\sitemap.txt";
        Create_File(fileToAppend); //fisierul de output

        String firstdirpath = "C:\\Users\\Bogdan\\Documents\\GitHub\\"; //Calea pentru directorul principal
        File firstdir = new File(firstdirpath);
        try {
            if (firstdir.exists() && firstdir.isDirectory()) {
                File files[] = firstdir.listFiles();
                usingFileOutputStream(firstdirpath, fileToAppend);
                usingFileOutputStream("\r\n", fileToAppend);
                createSitemap(files, 0, 0, fileToAppend);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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
