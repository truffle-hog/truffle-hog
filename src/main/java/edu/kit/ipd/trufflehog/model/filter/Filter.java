package edu.kit.ipd.trufflehog.model.filter;

import java.io.Serializable;
import java.util.LinkedList;

/**<p>
 * This class is used to store filtering options such as white- and blacklists.
 * </p>
 */

public class Filter implements Serializable {
	private LinkedList ipRegexList;
	private LinkedList macRegexList;
	private LinkedList nameRegexList;

    /**<p>
     * Adds an IP-rule to the regex list.
     * </p>
     * @param ex Expression to add
     */
	public void addIPRegex(String ex) {

	}

    /**<p>
     * Adds an MAC-rule to the regex list.
     * </p>
     * @param ex Expression to add
     */
	public void addMACRegex(String ex) {

	}

    /**<p>
     * Adds an Name-rule to the regex list.
     * </p>
     * @param ex Expression to add
     */
	public void addNameRegex(String ex) {

	}

}
