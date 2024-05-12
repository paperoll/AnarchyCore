package org.wksh.core.logger;

import com.diogonunes.jcolor.Attribute;
import org.wksh.core.AnarchyCore;

import java.util.HashMap;
import java.util.regex.Pattern;

import static com.diogonunes.jcolor.Ansi.colorize;

public class Logger
{
    private static HashMap<String, Logger> loggers = new HashMap<>();
    private String name;

    public static Logger getLogger(String loggerName)
    {
        if(loggers.containsKey(loggerName))
        {
            return loggers.get(loggerName);
        }

        Logger logger = new Logger(loggerName);
        loggers.put(loggerName, logger);
        return logger;
    }

    private Logger(String loggerName)
    {
        name = loggerName;
    }

    public void log(LogLevel level, Object message)
    {
        String text = colorize(String.format(" %s ", name), Attribute.CYAN_TEXT(), Attribute.BOLD(), Attribute.FRAMED()) + " ";

        switch (level)
        {
            case INFO:
                text += colorize(String.format("INFO: %s", message), Attribute.GREEN_TEXT(), Attribute.BOLD());
                break;
            case WARN:
                text += colorize(String.format("WARN: %s", message), Attribute.YELLOW_TEXT(), Attribute.BOLD());
                break;
            case ERROR:
                text += colorize(String.format("ERROR: %s", message), Attribute.RED_TEXT(), Attribute.BOLD());
                break;
            case DEBUG:
                text += colorize(String.format("DEBUG: %s", message), Attribute.MAGENTA_TEXT(), Attribute.BOLD());
                break;
        }

        System.out.println(text);
    }

    public void info(Object text, Object... arguments)
    {
        String stringMessage = text.toString();
        for (Object argument : arguments)
        {
            String regex = Pattern.quote("{}");
            stringMessage = stringMessage.replaceFirst(regex, argument.toString());
        }
        log(LogLevel.INFO, stringMessage);
    }

    public void warn(Object text, Object... arguments)
    {
        String stringMessage = text.toString();
        for (Object argument : arguments)
        {
            String regex = Pattern.quote("{}");
            stringMessage = stringMessage.replaceFirst(regex, argument.toString());
        }
        log(LogLevel.WARN, stringMessage);
    }

    public void error(Object text, Object... arguments)
    {
        String stringMessage = text.toString();
        for (Object argument : arguments)
        {
            String regex = Pattern.quote("{}");
            stringMessage = stringMessage.replaceFirst(regex, argument.toString());
        }
        log(LogLevel.ERROR, stringMessage);
    }

    public void debug(Object text, Object... arguments)
    {
        String stringMessage = text.toString();
        for (Object argument : arguments)
        {
            String regex = Pattern.quote("{}");
            stringMessage = stringMessage.replaceFirst(regex, argument.toString());
        }
        log(LogLevel.DEBUG, stringMessage);
    }

    public enum LogLevel
    {
        INFO,
        WARN,
        ERROR,
        DEBUG
    }
}