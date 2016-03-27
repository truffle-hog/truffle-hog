/*
 * This file is part of TruffleHog.
 *
 * TruffleHog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TruffleHog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import edu.kit.trufflehog.model.network.graph.components.ComponentInfoCollector;
import edu.kit.trufflehog.model.network.graph.components.ComponentInfoVisitor;
import edu.kit.trufflehog.model.network.graph.components.IComponentVisitor;
import edu.kit.trufflehog.viewmodel.StatisticsViewModel;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;

/**
 * \brief
 * \details
 * \date 09.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class SelectionCommand implements IUserCommand<Pair<Set<INode>, Set<IConnection>>> {

    private static final Logger logger = LogManager.getLogger();

    // TODO put empty pickedState
    private Set<INode> pickedNodes;

    private Set<IConnection> pickedConnections;

    boolean lastTimeEmpty = true;

    private final StatisticsViewModel statisticsViewModel;

    private Pair<Set<INode>, Set<IConnection>> selected = new ImmutablePair<>(new HashSet<>(), new HashSet<>());

    private final IComponentVisitor<TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>> infoVisitor = new ComponentInfoVisitor();
    private final Collector<IComponent, TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>,
            TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>> collector = new ComponentInfoCollector(this.infoVisitor);

    public SelectionCommand(StatisticsViewModel statisticsViewModel) {

        this.statisticsViewModel = statisticsViewModel;
    }

    @Override
    synchronized
    public <S extends Pair<Set<INode>, Set<IConnection>>> void setSelection(S selection) {

        selected = selection;
        logger.debug(selected);
    }

    private void updateNodeStatistics(Set<INode> nodes) {

        //clearStatistics();

        if (nodes.size() == 1) {

            final TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> infos = nodes.iterator().next().stream().collect(collector);
            logger.debug(infos);
            infos.setExpanded(true);
            Platform.runLater(() -> statisticsViewModel.setSelectionValues(infos));

        } else {



        }


    }

    private void updateConnectionStatistics(Set<IConnection> connections) {

        //clearStatistics();

        if (connections.size() == 1) {

            final TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> infos = connections.iterator().next().stream().collect(collector);
            logger.debug(infos);
            infos.setExpanded(true);

            Platform.runLater(() -> statisticsViewModel.setSelectionValues(infos));

        } else {



        }


    }

    private void updateMixedStatistics(Set<INode> nodes, Set<IConnection> connections) {

        clearStatistics();

        if (nodes.size() == 1) {

        }

       // final NodeStatisticsComponent nsc =


    }

    private void clearStatistics() {

       statisticsViewModel.clearStatistics();
    }

    @Override
    synchronized
    public void execute() {

        if (selected.getLeft().isEmpty() && selected.getRight().isEmpty()) {

            logger.debug("nothing selected");
            clearStatistics();

        } else if (selected.getLeft().isEmpty() && !selected.getRight().isEmpty()) {

            logger.debug("only edges selected");
            updateConnectionStatistics(selected.getRight());

        } else if (!selected.getLeft().isEmpty() && selected.getRight().isEmpty()) {

            logger.debug("only nodes selected");
            updateNodeStatistics(selected.getLeft());

        } else if (!selected.getLeft().isEmpty() && !selected.getRight().isEmpty()) {

            logger.debug("nodes and edges selected");
            updateMixedStatistics(selected.getLeft(), selected.getRight());
        }

    }
}
