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

import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedList;
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
public class ComponentInfoCollector implements Collector<IComponent, Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>,
        Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>> {

    private final IComponentVisitor<Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>> infoExtractor;

    public ComponentInfoCollector(IComponentVisitor<Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>> infoExtractor) {

        this.infoExtractor = infoExtractor;
    }

    @Override
    public Supplier<Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>> supplier() {

        return LinkedList::new;
    }

    @Override
    public BiConsumer<Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>, IComponent> accumulator() {

        return (stringPairMap, iComponent) -> stringPairMap.addAll(iComponent.accept(infoExtractor));
    }

    @Override
    public BinaryOperator<Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>> combiner() {

        return (lhs, rhs) -> {
            lhs.addAll(rhs);
            return lhs;
        };
    }

    @Override
    public Function<Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>, Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>> finisher() {

        return stringObjectMap -> stringObjectMap;
    }

    @Override
    public Set<Characteristics> characteristics() {

        return EnumSet.of(Characteristics.UNORDERED);
    }
}
