package org.iceanarchy.core.logger;

import com.diogonunes.jcolor.Attribute;

import java.util.HashMap;

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

    public void info(Object text)
    {
        log(LogLevel.INFO, text);
    }

    public void warn(Object text)
    {
        log(LogLevel.WARN, text);
    }

    public void error(Object text)
    {
        log(LogLevel.ERROR, text);
    }

    public void debug(Object text)
    {
        log(LogLevel.DEBUG, text);
    }

    public enum LogLevel
    {
        INFO,
        WARN,
        ERROR,
        DEBUG
    }
}