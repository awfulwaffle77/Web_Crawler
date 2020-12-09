package ro.mta;

public class WebCrawler {

    private static PostProcessor postProcessor;


    public static void main(String[] args) {

        interpretCommands(args);

    }
    
    public static void interpretConfig(String filename) {
        List<String> list = new ArrayList<String>();
        List<String> namesList = new ArrayList<String>();
        List<String> valsList = new ArrayList<String>();
        try(BufferedReader in = new BufferedReader(new FileReader("filename")))
        {
            String line;
            while((line = in.readLine())!=null){
                String[] pair = line.split("=");
               namesList.add(pair[0]);
                valsList.add(pair[1]);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }	

    private static void interpretCommands(String[] args) {

        if (args.length == 0) {

            System.out.println("Please enter one of the existing commands");

        } else {

            if (args[0].equals("search")) {

                if (args.length == 1) {

                    System.out.println("Please enter at least one keyword");

                } else {

//                    postProcessor.search();

                }
            } else if (args[0].equals("filterBySize")) {

                if (args.length == 1) {

                    System.out.println("Please enter the size you want");

                } else if (args.length == 2) {

//                    postProcessor.dimensionLimit();

                } else {

                    System.out.println("Please enter only one size");

                }
            } else {

                System.out.println("Please enter one of the existing commands");

            }
        }
    }
}
