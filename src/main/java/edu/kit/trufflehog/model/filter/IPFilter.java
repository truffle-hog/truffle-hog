package edu.kit.trufflehog.model.filter;

import com.google.common.collect.Range;
import edu.kit.trufflehog.model.network.graph.INode;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *     
 * </p>
 *
 * @author Mark Giraud
 * @version 0.1
 */
public class IPFilter implements IFilter {

    final List<Range> ipRanges = new LinkedList<>();

    @Override
    public void check(final INode node) {

    }

    public void addIPRule(final String rule) {

    }
}
