package ro.mta;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
public class Logger {
    private static Logger logger = null;
    private String logFile;
    private PrintWriter writer;
    public Logger() {
        try {
            logFile=System.getProperty("user.dir")+"\\logs.txt";
            System.out.println(logFile);
            FileWriter fw = new FileWriter(logFile);
            writer = new PrintWriter(fw, true);
        }
        catch (IOException e)
        {
        }
    }
    public static synchronized Logger getInstance(){
        if(logger == null)
            logger = new Logger();
        return logger;
    }
    public void writeLog (String tip,String message) {
        String data_format;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM.yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        data_format = dateFormat.format(cal.getTime());
        if(tip=="INFO")
            writer.println("[ " +data_format + " ] " + tip + ":" + " Download in progress "+ message);
        if(tip=="WARN")
            writer.println("[ " +data_format + " ] " + tip + ":" + " Page "+ message + " not found (404)");
        if(tip=="ERR")
            writer.println("[ " +data_format + " ] " + tip + ":" + " Cannot resolve host "+ message);
    }
}