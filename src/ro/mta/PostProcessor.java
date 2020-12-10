package ro.mta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PostProcessor {


     /*
        Filtrul de cuvinte cheie
        Daca nu sunt cuvinte cheie fisierul este acceptat
        Daca sunt cuvinte cheie se citeste fisierul in memorie si apoi se cauta in fiecare linie cuvintele cheie
        Daca fisierul contine cel putin un cuvant cheie din lista, acesta este acceptat
        Daca se intampina o eroare fisierul este respins.
     */
    private static boolean keywordFilter(Path file, List<String> keywords) {

        if (keywords.size() == 0){
            return true;
        }

        try {

            List<String> fileContent = Files.readAllLines(file);

            Stream<String> kwStream = keywords.stream();

            return fileContent.stream().anyMatch(line->kwStream.anyMatch(line::contains));

        } catch (IOException e) {
            return false;
        }

    }

      /*
        Filtrul de dimensiune.
        Daca dimensiunea data ca parametru este <=0 fisierul este acceptat
        Altfel se compara valorile intre ele.
        Daca apare vreo exceptie fisierul este respins
     */
    private static boolean sizeFilter(Path file,Long size){

        if (size <=0){
            return true;
        }

        try {
            return Files.size(file) < size;
        } catch (IOException e) {
           return false;
        }
    }


    /*
        Functia de filtrare efectiva.
        1)Selecteaza toate fisierele ce nu sunt directoare din directorul radacina
        2)Le filtreaza pe toate dupa dimensiunea maxima
        3)Le filtreaza pe toate dupa cuvintele cheie
        4)Intoarce rezultatul
     */
    public static List<Path> filterRootDirectory(String root, Long maxSize, List<String> keywords){

        Path rootPath = Path.of(root);

        try {
            Stream<Path> files = Files.walk(rootPath).filter(Files::isRegularFile);

            files = files.filter(path -> sizeFilter(path,maxSize));

            files = files.filter(path->keywordFilter(path,keywords));

            return files.collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

}
