package ro.mta;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Sitemap {

    public static void Create_File(String filename) {
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                System.out.println("Fisierul a fost creat: " + myObj.getName() + "\n");
            } else {
                System.out.println("Fisierul a fost inlocuit: " + myObj.getName() + "\n");
                myObj.delete();
                myObj.createNewFile(); //daca exista, il distrugem si cream altul
            }
        } catch (IOException e) {
            System.out.println("S-a produs o eroare.\n");
            e.printStackTrace();
        }
    }

    public static void usingFileOutputStream(String textToAppend, String fileToAppend) throws IOException
    {
        FileOutputStream outputStream = new FileOutputStream(fileToAppend, true);
        byte[] strToBytes = textToAppend.getBytes();
        outputStream.write(strToBytes);

        outputStream.close();
    }

    static void createSitemap(File[] files, int contor, int nivel, String fileToAppend)
    {
        if(contor == files.length)
            return;
        try {
            if (files[contor].isDirectory()) {
                for (int i = 0; i < nivel; i++)
                    usingFileOutputStream("\t", fileToAppend);
                String toPrint = files[contor].getName() + "/" + "\r\n";
                usingFileOutputStream(toPrint, fileToAppend);
                // recursivitate pentru subdirectoare
                createSitemap(files[contor].listFiles(), 0, nivel + 1, fileToAppend);

            }

            // recursivitate pentru directorul principal
            createSitemap(files, ++contor, nivel, fileToAppend);

            if (files[contor - 1].isFile())
            {
                for (int i = 0; i < nivel; i++)
                    usingFileOutputStream("\t", fileToAppend);
                String toPrint = files[contor - 1].getName() + "\r\n";
                usingFileOutputStream(toPrint, fileToAppend);
            }
        }   catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        String fileToAppend = "C:\\Users\\Bogdan\\Documents\\GitHub\\sitemap.txt";
        Create_File(fileToAppend); //fisierul de output

        String firstdirpath = "C:\\Users\\Bogdan\\Documents\\GitHub\\"; //Calea pentru directorul principal
        File firstdir = new File(firstdirpath);
        try {
            if(firstdir.exists() && firstdir.isDirectory())
            {
                File files[] = firstdir.listFiles();
                usingFileOutputStream(firstdirpath, fileToAppend);
                usingFileOutputStream("\r\n", fileToAppend);
                createSitemap(files,0,0, fileToAppend);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
