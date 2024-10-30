package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.heyner.common.Parameter;


import java.util.Arrays;

public class ArgsChecker {
    private static final Logger logger = LogManager.getLogger(ArgsChecker.class);
    private final boolean valid;
    
    public ArgsChecker(String [] args, Parameter param) {

        if (logger.isInfoEnabled())
            logger.info("Arguments : {}", Arrays.toString(args));

        //Check argument present :
        if (args.length == 0) {
            logger.error("No argument, end of program.");
            System.exit(-1);
        }

        // Check function (first argument) : 
        if (!isFunctionValid(args[0])) {
            logger.error("Invalid function : {}", args[0]);
            System.exit(-1);
        }

        //Check number of argument for thr function :
        controlNumberOfArgument(args,param);

        valid=true;
    }

    private void controlNumberOfArgument(String [] args, Parameter param) {
        int expected = 0;
        try {
            expected = Integer.parseInt(param.getProperty(args[0]+".numberOfArgument"));
        } catch (NumberFormatException e) {
            logger.error("Unable to parse number of arguments of : {}", args[0]+".numberOfArgument");
            System.exit(-1);
        }

        if ( args.length != expected) {
            logger.error("Invalid number of arguments, expected : {}, actual : {}", expected, args.length);
            System.exit(-1);
        }

    }

    private boolean isFunctionValid(String arg) {
        for (AvailableFunctions val : AvailableFunctions.values()) {
            if (val.name().equals(arg)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValid() {return this.valid;}
}
