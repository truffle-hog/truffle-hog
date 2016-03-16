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

import edu.kit.trufflehog.model.network.graph.NetworkConnection;
import edu.kit.trufflehog.model.network.graph.NetworkNode;
import edu.kit.trufflehog.model.network.graph.components.edge.EdgeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.FilterPropertiesComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;
import edu.kit.trufflehog.model.network.graph.components.node.NodeStatisticsComponent;
import edu.kit.trufflehog.model.network.graph.components.node.PacketDataLoggingComponent;
import edu.kit.trufflehog.viewmodel.StatisticsViewModel;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/**
 * \brief
 * \details
 * \date 15.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class ComponentInfoVisitor implements IComponentVisitor<Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>>> {

    @Override
    public Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> visit(ViewComponent viewComponent) {

        return Collections.emptyList();
    }

    @Override
    public Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> visit(EdgeStatisticsComponent edgeStatisticsComponent) {

        final Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> infoMap = new LinkedList<>();

        infoMap.add(new StatisticsViewModel.StringEntry<>("Connection Traffic", edgeStatisticsComponent.getTrafficProperty()));

        return infoMap;
    }

    @Override
    public Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> visit(NodeStatisticsComponent nodeStatisticsComponent) {

        final Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> infoMap = new LinkedList<>();

        infoMap.add(new StatisticsViewModel.StringEntry<>("Outgoing and ingoing Communication", nodeStatisticsComponent.getCommunicationCountProperty()));

        return infoMap;
    }

    @Override
    public Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> visit(NodeInfoComponent nodeInfoComponent) {

        final Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> infoMap = new LinkedList<>();

        infoMap.add(new StatisticsViewModel.StringEntry<>("Device name", nodeInfoComponent.getDeviceNameProperty()));
        infoMap.add(new StatisticsViewModel.StringEntry<>("IP Address", nodeInfoComponent.getIpAddressProperty()));
        infoMap.add(new StatisticsViewModel.StringEntry<>("Mac Address", new SimpleObjectProperty<>(nodeInfoComponent.getMacAddress())));

        return infoMap;
    }

    @Override
    public Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> visit(FilterPropertiesComponent filterPropertiesComponent) {

        final Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> infoMap = new LinkedList<>();
        infoMap.add(new StatisticsViewModel.StringEntry<>("Status", new SimpleObjectProperty<>(filterPropertiesComponent.getFilterColor())));
        return infoMap;
    }

    @Override
    public Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> visit(PacketDataLoggingComponent packetDataLoggingComponent) {

        final Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> infoMap = new LinkedList<>();
        infoMap.add(new StatisticsViewModel.StringEntry<>("Packets", packetDataLoggingComponent.getObservablePacketsProperty()));

        return infoMap;
    }

    @Override
    public Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> visit(NetworkNode iComponents) {
        return Collections.emptyList();
    }

    @Override
    public Collection<StatisticsViewModel.IEntry<StringProperty, ? extends Property>> visit(NetworkConnection iComponents) {
        return Collections.emptyList();
    }
}
