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
package edu.kit.trufflehog.model.network;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;

/**
 * \brief
 * \details
 * \date 10.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class SelectionStatistic implements ChangeListener<IComponent> {

    private final ObservableList<INode> selectedNodes = FXCollections.emptyObservableList();

    private final ObservableList<IConnection> selectedEdges = FXCollections.emptyObservableList();

    private final BooleanProperty isSelectedProperty = new SimpleBooleanProperty(false);

    private void update() {

        if (selectedNodes.isEmpty() && selectedEdges.isEmpty()) {
            isSelectedProperty.set(false);
            return;
        }

        computeStatisticValues();
    }

    private void computeStatisticValues() {



    }

    void setNodeSelection(Collection<INode> selectedNodes) {



    }

    void setConnectionSelection(Collection<IConnection> selectedConnections) {

    }

    public BooleanProperty getIsSelectedProperty() {
        return isSelectedProperty;

    }

    public boolean isSelected() {
        return isSelected();
    }


    @Override
    public void changed(ObservableValue<? extends IComponent> observable, IComponent oldValue, IComponent newValue) {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }
}
