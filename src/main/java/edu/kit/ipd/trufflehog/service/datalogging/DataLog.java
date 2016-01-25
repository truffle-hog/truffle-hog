package edu.kit.ipd.trufflehog.service.datalogging;

import java.io.Serializable;

import edu.kit.ipd.trufflehog.model.graph.NetworkGraph;

import java.util.List;
import java.util.Date;

public class DataLog implements Serializable {

	private NetworkGraph snapshotLog;

	private List<ICommand> commandLog;

	private Date startDate;

	private Date endDate;

}
