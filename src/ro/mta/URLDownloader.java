package ro.mta;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Aceasta clasa realizeaza descarcarea unei pagini web si
 * extragerea hyperlink-urilor din pagina descarcata.
 *
 * @author Mihai Paun
 */

public class URLDownloader {

    private int maxNbOfPages;
    private Set<String> visitedPages = new HashSet<String>();
    private ArrayList<String> pagesToVisit = new ArrayList<String>();

    private String getDirectoryName(String url){
        String[] buffer = url.split("/");
        buffer[2] = ".\\" + buffer[2] + "\\";

        return buffer[2];
    }

    private String getFileName(String url){
        String[] buffer = url.split("/");
        String fn = buffer[buffer.length-1];

        if (!isFile(fn))
            fn += ".html";

        return fn;
    }

    // TODO: verific daca fisierul poate fi descarcat

    private boolean isFile(String link){
        if (link.contains(".pdf"))
            return true;
        return false;
    }

    // TODO: replace hyperlinks with path

    public void extractLinks(String fileName, String path) throws IOException {
        String content = new Scanner(new File(fileName)).useDelimiter("\\Z").next();
        Pattern linkPattern = Pattern.compile("href=\"(.*?)\"",  Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
        Matcher pageMatcher = linkPattern.matcher(content);
        String link;

        while (pageMatcher.find()){
            link = pageMatcher.group(1);

            if (!visitedPages.contains(link) && !pagesToVisit.contains(link) && link.contains("http")) {
                pagesToVisit.add(link);
                System.out.println(link);
            }
        }
    }

    private void downloadURL(String url){
        try{
            URL u = new URL(url);
            InputStream is = u.openStream();
            String fileName = getFileName(url);

            String path = getDirectoryName(url);
            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            System.out.println("filename " + fileName);
            System.out.println("path "+ path);

            path = path + fileName;

            File fout = new File(path);
            FileOutputStream fos = new FileOutputStream(fout);

            int length = -1;
            byte[] buffer = new byte[1024];
            while ((length = is.read(buffer)) > -1){
                fos.write(buffer,0,length);
            }

            is.close();
            fos.close();
        }
        catch (MalformedURLException url_e){
            url_e.printStackTrace();
        }
        catch (IOException io_e) {
            io_e.printStackTrace();
        }
    }

    public void download(String url, int pageNb) throws IOException {
        if(pageNb == maxNbOfPages)
            return;

        downloadURL(url);
        visitedPages.add(url);
        String fileName = getDirectoryName(url) + getFileName(url);
        if(fileName.contains(".html"))
            extractLinks(fileName, getDirectoryName(url));
        for (Iterator<String> it=pagesToVisit.iterator(); it.hasNext();){
            String l = it.next();
            it.remove();
            download(l,pageNb+1);
        }
    }
}
