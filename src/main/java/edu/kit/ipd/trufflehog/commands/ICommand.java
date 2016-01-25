package edu.kit.ipd.trufflehog.commands;

import java.io.Serializable;

public interface ICommand extends Serializable {

	public abstract Void execute();

}
