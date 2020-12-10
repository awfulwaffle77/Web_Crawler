package ro.mta;

import java.util.ArrayList;
import java.util.Map;

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

    public static void dimensionLimit(String siteName, Map<String,String> rules){


    }


}
