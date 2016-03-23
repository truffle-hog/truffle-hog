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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * \brief
 * \details
 * \date 04.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class MyBinding extends IntegerBinding implements ChangeListener<Number>, ListChangeListener<IntegerProperty> {

    private static final Logger logger = LogManager.getLogger(MyBinding.class);

    private int max = 0;

    public MyBinding() {
    }

    public void bindProperty(IntegerProperty property) {

        property.addListener(this);
        super.bind(property);
        //boundProperties.add(property);
    }

    @Override
    protected int computeValue() {
        return max;
    }

    @Override
    public void onChanged(Change<? extends IntegerProperty> c) {

        logger.debug("there was changed on the list");

        if (c.next()) {

            c.getAddedSubList().stream().forEach(p -> {
                if (p.get() > max) {
                    max = p.get();
                }
            });
        }
    }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

        logger.debug("there was change on a value");

        if (newValue.intValue() > max) {
            max = newValue.intValue();
        }
    }
}
