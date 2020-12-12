package ro.mta.commands;


import javax.naming.directory.InvalidSearchFilterException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;//folosesc regex ca sa identific sfarsitul fisierului


public class ListCommand extends AbstractCommand {

    private String argument;
    private String pathToDir;
    private Boolean fileFound;

    private String validatePathToDir(String pathToDir) throws NotDirectoryException {
        if (Files.isDirectory(Path.of(pathToDir))){
            return pathToDir;
        }
        else {
            throw new NotDirectoryException("The path given to the directory is not good");
        }
    }

    private String checkListingArg(String listingArg) throws InvalidSearchFilterException {
        Pattern pattern = Pattern.compile("[a-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(listingArg);
        if (matcher.find()){
            return listingArg;
        }
        else {
            throw new InvalidSearchFilterException("The listing extension is not valid.");
        }
    }

    public ListCommand(String[] args) {
        super(args);
        try{
            fileFound = false;
            pathToDir = validatePathToDir(args[1]);
            argument = checkListingArg(args[2]);
        } catch (NotDirectoryException | InvalidSearchFilterException e) {
            e.printStackTrace();
        }
    }

    private void searchAndList( String path ) {

        Pattern pattern = Pattern.compile("(.*?)" + "\\." + argument+"$", Pattern.CASE_INSENSITIVE);

        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                searchAndList( f.getAbsolutePath() ); //aici incepe recursivitatea
            }
            else {
                Matcher matcher = pattern.matcher(f.getName());
                boolean matchFound = matcher.find();
                if(matchFound) {
                    System.out.println(f.getAbsoluteFile());
                    fileFound = Boolean.TRUE;
                }
            }
        }
        if(!fileFound)
            System.out.println("Nu s-a gasit niciun fisier cu extensia data.");
    }

    @Override
    public void execute() {
        searchAndList(pathToDir);
    }
}
