package ro.mta.commands;

import ro.mta.PostProcessor;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchCommand extends AbstractCommand {

    private static String filteredFilesRootDirectory = "filteredByCrawlerIlona";

    private String pathToDir;
    private Long maxFileSize;
    private List<String> keywords;

    private Boolean isValid = true;
    private String invalidReasonString;


    /*
        Valideaza calea data ca parametru si arunca exceptie cu mesaj daca nu e ok
     */
    private String validatePathToDir(String pathToDir) throws NotDirectoryException {
        if (Files.isDirectory(Path.of(pathToDir))){
            return pathToDir;
        }
        else {
            throw new NotDirectoryException("The path given to the directory is not good");
        }
    }

    /*
        Valideaza dimensiunea data ca parametru. functia parseLong() arunca exceptie daca formatul de nu bun
     */
    private Long validateMaxFileSize(String maxFileSize){
        return Long.parseLong(maxFileSize);
    }

    //Se valideaza dimensiunea listei de cuvinte cheie si se adauga in lista.
    private List<String> validateKeywords(String[] kw){

        List<String> kwList = new ArrayList<>(15);

        if (kw.length < 4){
            return kwList;
        }

        for (int i=3;i<kw.length;i++){
            kwList.add(kw[i]);
        }

        return kwList;

    }

    /*
        Goleste directorul dat ca parametru
     */
    private void emptyDirectory(File dir){

        for (File file : Objects.requireNonNull(dir.listFiles())){
            if (file.isDirectory())
            {
                emptyDirectory(file);
            }
            file.delete();
        }
    }


    /*Formatul comenzii de search este:
     *
     * search pathToDir maxFileSize keyword0 keyword1 keyword2 ...
     * maxFileSize poate fi 0 sau negativ pentru a fi ignorat
     * keyword-urile pot sa lipseasca.
     *
     * Daca nu sunt indeplinite cerintele se arunca exceptie si apoi se seteaza un flag-uri care marcheaza
     * comanda drept invalida.
     *
     */
    public SearchCommand(String[] args) {
        super(args);

        try{
            pathToDir = validatePathToDir(args[1]);
            maxFileSize = validateMaxFileSize(args[2]);
            keywords = validateKeywords(args);
        }
        catch (IndexOutOfBoundsException e){
            isValid = false;
            invalidReasonString = "The search command didn`t contain enough arguments";
        } catch (Exception nfe){
            isValid = false;
            invalidReasonString = nfe.getMessage();
        }
    }


    /*
        Executa comanda.
        Daca comanda e invalida se afiseaza mesajul de eroare si se inchide aplicatia.
        Daca comanda e valida se parcurge urmatorul algoritm:
        1)Se executa filtrarea
        2)Se goleste si sterge directorul standard de filtrare
        3)Se recreeaza directorul standard de filtrare
        4)Se copiaza fisierle dintr-o parte in alta
     */
    @Override
    public void execute() {

        if(!isValid){
            System.out.println(invalidReasonString);
            return;
        }

        List<Path> filteredFiles = PostProcessor.filterRootDirectory(pathToDir,maxFileSize,keywords);

        Path filteredFilesRoot = Path.of(pathToDir,filteredFilesRootDirectory);

        try {

            if (Files.exists(filteredFilesRoot) && Files.isDirectory(filteredFilesRoot)){
                emptyDirectory(filteredFilesRoot.toFile());
                Files.delete(filteredFilesRoot);
            } else if (Files.isRegularFile(filteredFilesRoot)){
                Files.delete(filteredFilesRoot);
            }

            Files.createDirectory(filteredFilesRoot);

            copyFiles(filteredFilesRoot,filteredFiles);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }


    private void copyFiles(Path directory,List<Path> filePaths){



        filePaths.forEach(filePath -> {
            String absPath = filePath.toAbsolutePath().toString();

            absPath = absPath.replace(directory.getParent().toString(),directory.toString());

            try {

                Path p = Paths.get(absPath);

                if (!Files.exists(p.getParent())){
                    new File(p.toString()).mkdirs();
                }

                Files.copy(filePath,p, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e){
                e.printStackTrace();
            }
        });

    }

}
