package ro.mta.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommand {
    protected List<String> args;
    public abstract void execute();

    public AbstractCommand(List<String> args){
        this.args = args;
    }
    public AbstractCommand(String[] args){
        this.args = Arrays.asList(args);
    }
}
