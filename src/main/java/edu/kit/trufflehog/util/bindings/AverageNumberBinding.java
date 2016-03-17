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
package edu.kit.trufflehog.util.bindings;

import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collection;

/**
 * \brief Calculates the average of all the binded IntegerProperties
 * \details
 * \date 14.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class AverageNumberBinding extends IntegerBinding {

    private static final Logger logger = LogManager.getLogger(AverageNumberBinding.class);

    private final ObservableSet<IntegerProperty> boundProperties = FXCollections.observableSet();

    public AverageNumberBinding() {

        super.bind(boundProperties);
    }

    public void unbindAll() {

        this.unbind(boundProperties);
        boundProperties.clear();
        this.invalidate();
    }

    public void unbind(IntegerProperty ...properties) {

        super.unbind(properties);
        boundProperties.removeAll(Arrays.asList(properties));
        this.invalidate();
    }

    public void unbind(Collection<IntegerProperty> properties) {

        super.unbind(properties.toArray(new IntegerProperty[properties.size()]));
        boundProperties.removeAll(properties);
        this.invalidate();
    }

    public void unbind(IntegerProperty property) {

        super.unbind(property);
        boundProperties.remove(property);
        this.invalidate();
    }

    public void bind(IntegerProperty property) {

        if (boundProperties.contains(property)) {
            return;
        }
        boundProperties.add(property);
        super.bind(property);
    }

    public void bindAll(Collection<IntegerProperty> properties) {

        properties.stream().forEach(this::bind);
    }

    public void bindAll(IntegerProperty property, IntegerProperty ...properties) {

        bind(property);
        bindAll(Arrays.asList(properties));
    }

    @Override
    protected int computeValue() {

        if (boundProperties.isEmpty()) {
            return 0;
        }
        return boundProperties.parallelStream().map(IntegerProperty::get).reduce(0, Math::addExact) / boundProperties.size();
    }

}
