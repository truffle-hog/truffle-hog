package edu.kit.ipd.trufflehog.model.filter;

import java.io.Serializable;
import java.util.LinkedList;

public abstract class Filter implements Serializable {

	private LinkedList ipRegexList;

	private LinkedList macRegexList;

	private LinkedList nameRegexList;

	public void addIpRegex(String ex) {

	}

	public void addMACRegex(String ex) {

	}

	public void addNameRegex(String ex) {

	}

}
