package ro.mta.commands;

import java.util.ArrayList;

public class InvalidCommand extends AbstractCommand{

    @Override
    public void execute() {
        System.out.println(args.get(0));
    }

    public InvalidCommand(String[] message){
        super(message);
    }

}
