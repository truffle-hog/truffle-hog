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

import javafx.beans.property.ListProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * \brief
 * \details
 * \date 14.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 *
 * //TODO document!
 */
public class StatisticsViewModel {

    private static final Logger logger = LogManager.getLogger(StatisticsViewModel.class);

    private final TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> rootItem = new TreeItem<>(new StringEntry<>("Info", ""));

    private final ListProperty<TreeItem<
                    StatisticsViewModel.IEntry<
                            StringProperty, ? extends  Property>>> infoListProperty = new SimpleListProperty<>(rootItem.getChildren());

    public TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> getRootItem() {
        return rootItem;
    }

    public ListProperty<TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends  Property>>> getInfoListProperty() {
        return infoListProperty;
    }

    public void setSelectionValues(TreeItem<IEntry<StringProperty, ? extends Property>> selectionValues) {

        rootItem.getChildren().clear();
        rootItem.getChildren().add(selectionValues);
    }

    public void clearStatistics() {

        rootItem.getChildren().clear();
    }

    public static class StringEntry<T extends Property> implements IEntry<StringProperty, T> {

        private final StringProperty keyProperty;
        private final T valueProperty;

        public StringEntry(String label, T property) {

            keyProperty = new SimpleStringProperty(label);
            valueProperty = property;
        }

        @SuppressWarnings("unchecked")
        public StringEntry(String name, String value) {

            keyProperty = new SimpleStringProperty(name);
            valueProperty = (T) new SimpleStringProperty(value);
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
