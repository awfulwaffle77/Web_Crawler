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

    // TODO: tratare erori de acces

    private String rootDir;
    private int MAX_NB_OF_PAGES = 5;
    private Set<String> visitedPages;
    private ArrayList<String> pagesToVisit;

    /**
     * URLDownloader class constructor
     * @param rootDir - root directory where the site is downloaded
     * @param MAX_NB_OF_PAGES - maximum number of pages to be downloaded
     */

    public URLDownloader(String rootDir, int MAX_NB_OF_PAGES) {
        this.rootDir = ".\\" + rootDir + "\\";
        this.MAX_NB_OF_PAGES = MAX_NB_OF_PAGES;
        this.visitedPages = new HashSet<String>();
        this.pagesToVisit = new ArrayList<String>();
    }

    // TODO: check if file can be downloaded

    private boolean isFile(String link){
        if (link.contains(".pdf"))
            return true;
        return false;
    }

    /**
     *
     * @param fileName - name of the file which is parsed to extract hyperlinks
     * @param path - path to the up-mentioned file
     * @param baseUrl - base url used for normalization
     * @throws IOException
     */

    private void extractLinks(String fileName, String path, String baseUrl) throws IOException {
        String content = new Scanner(new File(path + fileName)).useDelimiter("\\Z").next();
        String newContent = content;
        Pattern linkPattern = Pattern.compile("(src|href)=\"(.*?)\"",  Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
        Matcher pageMatcher = linkPattern.matcher(content);
        //String link;

        if(!content.toLowerCase().startsWith("<!doctype html>"))
            return;

        while (pageMatcher.find()){
            /*System.out.println(path+fileName);
            System.out.println(pageMatcher.group(0));
            System.out.println(pageMatcher.group(1));
            System.out.println(pageMatcher.group(2));*/
            String link = pageMatcher.group(2);
            if (!link.equals("#")){
                String old_link = link;

                link = normalize(link,baseUrl);

                if (!visitedPages.contains(link) && !pagesToVisit.contains(link)){
                    String[] buffer = link.split("://");
                    content = content.replaceAll(old_link,rootDir+"\\"+buffer[1]);
                    //content = newContent;
                    pagesToVisit.add(link);
                }
            }
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(path+fileName));
        writer.write(content);
        writer.close();
    }

    /**
     *
     * @param targetUrl - url from the href attribute
     * @param baseUrl - base url
     * @return - normalized url
     */

    private static String normalize(String targetUrl, String baseUrl) {
        if(targetUrl.startsWith("http://"))                   return targetUrl;
        if(targetUrl.startsWith("https://"))                  return targetUrl;
        if(targetUrl.toLowerCase().startsWith("javascript:")) return targetUrl;

        StringBuilder builder = new StringBuilder();

        if(!baseUrl.startsWith("http://") && !baseUrl.startsWith("https://")) {
            builder.append("http://");
        }

        //if is relative to root of server.
        if(targetUrl.startsWith("/")) {

            int endOfDomain = baseUrl.indexOf("/", 8);
            if(endOfDomain == -1){
                builder.append(baseUrl);
            } else {
                builder.append(baseUrl.substring(0, endOfDomain));
            }
            builder.append(targetUrl);

        } else {
            //find last directory of base url
            int lastDirSeparatorIndex = baseUrl.lastIndexOf("/");
            if(lastDirSeparatorIndex > 6){
                builder.append(baseUrl.substring(0, lastDirSeparatorIndex));
            } else {
                builder.append(baseUrl);
            }
            builder.append('/');
            builder.append(targetUrl);
        }

        // delete any fragment identifiers
        int fragmentIndex = builder.indexOf("#");
        if(fragmentIndex > -1) {
            builder.delete(fragmentIndex, builder.length());
        }

        // delete any internal dir up ../ if any are inside the URL, and not just in the start.
        int indexOfDirUp = builder.indexOf("../");
        while(indexOfDirUp > -1){
            int indexOfLastDirBeforeDirUp = builder.lastIndexOf("/", indexOfDirUp - 2);
            if(indexOfLastDirBeforeDirUp > -1 ) {
                builder.delete(indexOfLastDirBeforeDirUp +1, indexOfDirUp + 3);
            }
            indexOfDirUp = builder.indexOf("../");
        }

        return builder.toString();

    }

    /**
     * This method downloads a web page and extracts the hyperlinks
     * @param url - url of the page to be downloaded
     */

    private void downloadURL(String url){
        try{
            URL u = new URL(url);
            InputStream is = u.openStream();

            String[] buff = u.getPath().split("/");
            String fileName;

            StringBuilder path = new StringBuilder();
            path.append(rootDir + u.getHost()+ "\\");


            if(buff.length==0){
                fileName = "\\page.html";
                //path.append(fileName); // ?
            } else {
                if(!buff[buff.length-1].contains(".")){
                    fileName = "\\page.html";
                    path.append(u.getPath()+"\\");
                } else {
                    fileName = buff[buff.length-1];
                    //int index = fileName.lastIndexOf("/"); // "/"
                    int index;
                    if((index= fileName.lastIndexOf("/"))>=0) {
                        path.append(fileName.substring(0,index));
                    }
                    if((index= fileName.lastIndexOf("\\"))>=0) {
                        path.append(fileName.substring(0,index));
                    }
                }
            }
            path.append("\\");

            /*if(fileName.isEmpty()){
                fileName = "\\page.html";
            }
            if(!fileName.contains(".")){
                fileName += "\\page.html";
            }
            if(url.endsWith("/")){
                fileName = "\\page.html";
            }*/



            File dir = new File(path.toString());
            if(!dir.exists())
                dir.mkdirs();

            //System.out.println("filename " + fileName);
            //System.out.println("path "+ path);

            //path = path + fileName;

            File fout = new File(path+fileName);
            FileOutputStream fos = new FileOutputStream(fout);

            int length = -1;
            byte[] buffer = new byte[1024];
            while ((length = is.read(buffer)) > -1){
                fos.write(buffer,0,length);
            }

            is.close();
            fos.close();
            if(fileName.endsWith(".html") || fileName.endsWith(".htm")){
                extractLinks(fileName, path.toString(), url);
            }
            //System.out.println("###########################################################");
        }
        catch (MalformedURLException url_e){
            url_e.printStackTrace();
        }
        catch (IOException io_e) {
            io_e.printStackTrace();
        }
    }

    public void download(String url, int pageNb) throws IOException {
        if(pageNb == MAX_NB_OF_PAGES)
            return;

        downloadURL(url);
        visitedPages.add(url);
        //String fileName = getDirectoryName(url) + getFileName(url);
        //if(fileName.contains(".html"))
        //    extractLinks(fileName, getDirectoryName(url), url);
        for (Iterator<String> it=pagesToVisit.iterator(); it.hasNext();){
            String l = it.next();
            it.remove();
            download(l,pageNb+1);
        }
    }
}
