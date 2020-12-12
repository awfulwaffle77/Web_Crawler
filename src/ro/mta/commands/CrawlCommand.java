package ro.mta.commands;

import ro.mta.URLDownloader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * crawl command implements the crawling and downloading of the selected urls
 * command syntax: crawler crawl config.conf url.txt
 * - config.conf - configuration file for the crawler
 * - url.txt - text file which contains the web sites to be downloaded
 */

public class CrawlCommand extends AbstractCommand {

    private String configFile;
    /**
     * config.conf:
     * root_dir
     * max_nb_of_pages
     */
    private String urlFile;

    private String rootDir;
    private int nbOfPages;

    private ArrayList<String> urls;

    /**
     *
     * @param args - args[1] - config file name
     *             - args[2] - file which contains url to be crawled
     */

    public CrawlCommand(String[] args) {
        super(args);
        configFile = args[1];
        urlFile = args[2];

        try{
            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            String line;

            line = reader.readLine();
            rootDir = line;

            line = reader.readLine();
            nbOfPages = Integer.parseInt(line);

            reader.close();
            reader = new BufferedReader(new FileReader(urlFile));

            urls = new ArrayList<String>();

            while ((line = reader.readLine()) != null){
                urls.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println(ioe.getMessage());
        }
    }

    @Override
    public void execute() {
        try{
            URLDownloader u = new URLDownloader(rootDir,nbOfPages);
            for(Iterator<String> it=urls.iterator(); it.hasNext();){
                u.download(it.next(),0);
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
            System.out.println(ioe.getMessage());
        }
    }
}
