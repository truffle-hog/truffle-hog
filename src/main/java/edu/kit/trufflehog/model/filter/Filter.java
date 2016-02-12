package edu.kit.trufflehog.model.filter;

import edu.kit.trufflehog.command.ICommand;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * <p>
 *     Contains regex lists with ip, mac and name rules. These will be used with {@link ICommand} to distinguish specific
 *     nodes in the graph.
 * </p>
 */
public class Filter implements Serializable {
    private LinkedList ipRegexList;
    private LinkedList macRegexList;
    private LinkedList nameRegexList;

    /**
     * <p>
     *     Adds an IP-rule to the regex list.
     * </p>
     *
     * @param ex Expression to add
     */
    public void addIPRegex(String ex) {
    }

    /**
     * <p>
     *     Adds an MAC-rule to the regex list.
     * </p>
     *
     * @param ex Expression to add
     */
    public void addMACRegex(String ex) {
    }

    /**
     * <p>
     *     Adds an Name-rule to the regex list.
     * </p>
     *
     * @param ex Expression to add
     */
    public void addNameRegex(String ex) {
    }

    /**
     * <p>
     *     Checks whether a word matches one of the expressions from the regex lists
     * </p>
     *
     * @param s String to check
     * @return true if it does match, otherwise false
     */
    public boolean contains(String s) {
        return false; //requires implementation
    }
}
