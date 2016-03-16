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
package edu.kit.trufflehog.model.network.graph.components;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.viewmodel.StatisticsViewModel;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * \brief
 * \details
 * \date 15.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class ComponentInfoCollector implements Collector<IComponent, TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>,
        TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>> {

    private final IComponentVisitor<TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>> infoExtractor;

    public ComponentInfoCollector(IComponentVisitor<TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>> infoExtractor) {

        this.infoExtractor = infoExtractor;
    }

    @Override
    public Supplier<TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>> supplier() {

        //return TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>::new;

        return new Supplier<TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>>() {
            @Override
            public TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> get() {
                return new TreeItem<>(new StatisticsViewModel.StringEntry<>("Node", ""));
            }
        };

        //return new TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>();
    }

    @Override
    public BiConsumer<TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>, IComponent> accumulator() {

        return (treeItem, iComponent) -> treeItem.getChildren().add(iComponent.accept(infoExtractor));
    }

    @Override
    public BinaryOperator<TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>> combiner() {

        return (lhs, rhs) -> {

            if (lhs.getParent() == null && rhs.getParent() == null) {
                final TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> item = new TreeItem<>();
                item.getChildren().addAll(lhs, rhs);
                return item;
            } else if (lhs.getParent() == null) {
                rhs.getParent().getChildren().add(lhs);
                return rhs;
            } else {
                lhs.getParent().getChildren().add(rhs);
                return lhs;
            }
        };
    }

    @Override
    public Function<TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>, TreeItem<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>> finisher() {

        return stringObjectMap -> stringObjectMap;
    }

    @Override
    public Set<Characteristics> characteristics() {

        return EnumSet.of(Characteristics.UNORDERED);
    }
}
