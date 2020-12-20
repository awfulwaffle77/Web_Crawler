package ro.mta;

import java.io.IOException;
import java.util.Iterator;

public class CustomThread implements Runnable {
    URLDownloader u;
    int pageNb;
    String url;
    int thread_id;
    int numberOfThreads;

    String rootDir;
    int nbOfPages;

    public CustomThread(URLDownloader urlDownloader, int pageNb, String url, int thread_id, int numberOfThreads) {
        this.u = urlDownloader;
        this.pageNb = pageNb;
        this.url = url;
        this.thread_id = thread_id;
        this.numberOfThreads = numberOfThreads;
    }

    public CustomThread(String rootDir, int nbOfPages, String url){
        this.rootDir = rootDir;
        this.nbOfPages = nbOfPages;
        this.url = url;
        //this.thread_id = thread_id;
        //this.numberOfThreads = numberOfThreads;
    }

    @Override
    public void run() {
        /*if (pageNb == u.getMAX_NB_OF_PAGES())
            return;

        u.downloadURL(url);
        u.getVisitedPages().add(url);

        int start = thread_id * u.getPagesToVisit().size() / numberOfThreads;
        int end = (thread_id + 1) * u.getPagesToVisit().size() / numberOfThreads;
        //String fileName = getDirectoryName(url) + getFileName(url);
        //if(fileName.contains(".html"))
        //    extractLinks(fileName, getDirectoryName(url), url);
        for (int i = start; i < end; i++) {
            String l = u.getPagesToVisit().get(i);
            try {
                u.getPagesToVisit().remove(i);
                u.download(l, pageNb + 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        try {
        URLDownloader u = new URLDownloader(rootDir,nbOfPages, numberOfThreads);
        u.download(url,0);

        //for(Iterator<String> it = urls.iterator(); it.hasNext();){
                //u.download(it.next(),0);

        //    }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
