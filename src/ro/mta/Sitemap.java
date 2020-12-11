package ro.mta;
import java.io.File;

public class Sitemap {
    static void createSitemap(File[] files, int contor, int nivel)
    {
        if(contor == files.length)
            return;

        if(files[contor].isDirectory())
        {
            for (int i = 0; i < nivel; i++)
                System.out.print("\t");
            System.out.println(files[contor].getName() + "/");
            // recursivitate pentru subdirectoare
            createSitemap(files[contor].listFiles(), 0, nivel + 1);

        }
        // recursivitate pentru directorul principal
        createSitemap(files,++contor, nivel);

        if(files[contor-1].isFile())
        {
            for (int i = 0; i < nivel; i++)
                System.out.print("\t");
            System.out.println(files[contor-1].getName());
        }
    }

    public static void main(String[] args)
    {
        //Calea pentru directorul principal
        String firstdirpath = " ";

        // Obiect de tip File
        File firstdir = new File(firstdirpath);

        if(firstdir.exists() && firstdir.isDirectory())
        {
            File files[] = firstdir.listFiles();
            System.out.println("Sitemap pentru directorul cu calea: " + firstdirpath);
            createSitemap(files,0,0);
        }
    }
}
