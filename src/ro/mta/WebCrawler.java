package ro.mta;

public class WebCrawler {

    private static PostProcessor postProcessor;


    public static void main(String[] args) {

        interpretCommands(args);

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
