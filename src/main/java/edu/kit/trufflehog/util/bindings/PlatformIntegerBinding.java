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

import javafx.application.Platform;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
public class PlatformIntegerBinding extends IntegerBinding implements ChangeListener<Number> {

    private static final Logger logger = LogManager.getLogger(PlatformIntegerBinding.class);

    private int value;

    //private final IntegerProperty property;

    public PlatformIntegerBinding(IntegerProperty integerProperty) {
     //   property = integerProperty;
        value = integerProperty.get();
        super.bind(integerProperty);
        integerProperty.addListener(this);
    }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

        Platform.runLater(() -> value = newValue.intValue());
    }

    @Override
    protected int computeValue() {
        return value;
    }
}
