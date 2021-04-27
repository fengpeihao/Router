package com.cfxc.router.compiler;

import com.cfxc.router.annotation.Constants;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

import static com.cfxc.router.annotation.utils.Utils.isNotEmpty;


/**
 * @description Simplify the message print.
 * @author peihao.feng
 * @date 4/2/21
 */
public class Logger {
    private Messager msg;

    public Logger(Messager messager) {
        msg = messager;
    }

    /**
     * Print info log.
     */
    public void info(CharSequence info) {
        if (isNotEmpty(info)) {
            msg.printMessage(Diagnostic.Kind.NOTE, Constants.PREFIX_OF_LOGGER + info);
        }
    }

    public void error(CharSequence error) {
        if (isNotEmpty(error)) {
            msg.printMessage(Diagnostic.Kind.ERROR, Constants.PREFIX_OF_LOGGER + "An exception is encountered, [" + error + "]");
        }
    }

    public void error(Throwable error) {
        if (null != error) {
            msg.printMessage(Diagnostic.Kind.ERROR, Constants.PREFIX_OF_LOGGER + "An exception is encountered, [" + error.getMessage() + "]" + "\n" + formatStackTrace(error.getStackTrace()));
        }
    }

    public void warning(CharSequence warning) {
        if (isNotEmpty(warning)) {
            msg.printMessage(Diagnostic.Kind.WARNING, Constants.PREFIX_OF_LOGGER + warning);
        }
    }

    private String formatStackTrace(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : stackTrace) {
            sb.append("    at ").append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
