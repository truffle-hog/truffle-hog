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
package edu.kit.trufflehog.viewmodel;

import edu.kit.trufflehog.model.network.graph.IConnection;
import edu.kit.trufflehog.model.network.graph.INode;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Set;

/**
 * \brief
 * \details
 * \date 14.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class StatisticsViewModel {

    private static final Logger logger = LogManager.getLogger(StatisticsViewModel.class);


    private final ObjectProperty<SelectionStatus> selectionStatus = new SimpleObjectProperty<>(SelectionStatus.NONE);


    private final TableColumn keyColumn = new TableColumn("Key");
    private final TableColumn valueColumn = new TableColumn("Value");

    private final TableView<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> table = new TableView<>();

    private final ObservableList<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> infoList = FXCollections.observableArrayList();

    private final ListProperty<StatisticsViewModel.IEntry<StringProperty, ? extends  Property>> infoListProperty = new SimpleListProperty<>(infoList);

    public StatisticsViewModel() {

        keyColumn.setMinWidth(100);
        keyColumn.setCellValueFactory(
                new PropertyValueFactory<IEntry<StringProperty, ? extends Property>, String>("key")
        );

        valueColumn.setMinWidth(100);
        valueColumn.setCellValueFactory(
                new PropertyValueFactory<IEntry<StringProperty, ? extends Property>, String>("value")
        );
        table.setItems(infoList);

    }

    public ListProperty<StatisticsViewModel.IEntry<StringProperty, ? extends  Property>> getInfoListProperty() {
        return infoListProperty;
    }

    synchronized
    public void updateSelection(Pair<Set<INode>, Set<IConnection>> selected) {
    }


    public SelectionStatus getSelectionStatus() {
        return selectionStatus.get();
    }

    public ObjectProperty<SelectionStatus> selectionStatusProperty() {
        return selectionStatus;
    }

    private void recompute() {


    }

    synchronized
    public void updateNodeSelection(Collection<INode> selectedNodes) {

        recompute();
    }

    synchronized
    public void updateEdgeSelection(Collection<IConnection> selectedConnections) {

        recompute();
    }

    public void setSelectionValues(Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> selectionValues) {

        infoList.clear();
        infoList.addAll(selectionValues);
    }

    public enum SelectionStatus {
        NONE, ONE_VERTEX, ONE_EDGE, ONE_EDGE_ONE_VERTEX, MULTI_VERTEX, MULTI_EDGE, MIX
    }


    public static class StringEntry<T extends Property> implements IEntry<StringProperty, T> {

        private final StringProperty keyProperty;
        private final T valueProperty;

        public StringEntry(String label, T property) {

            keyProperty = new SimpleStringProperty(label);
            valueProperty = property;
        }


        @Override
        public StringProperty getKeyProperty() {
            return keyProperty;
        }

        @Override
        public T getValueProperty() {
            return valueProperty;
        }

        @Override
        public String toString() {
            return "key=" + keyProperty.get() + ", value=" + valueProperty.getValue();
        }
    }

    public interface IEntry<K extends Property, V extends Property> {

        K getKeyProperty();

        V getValueProperty();
    }
}
