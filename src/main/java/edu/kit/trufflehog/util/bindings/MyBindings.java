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

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableNumberValue;

/**
 * \brief
 * \details
 * \date 22.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class MyBindings {



    public static DoubleBinding sqrt(ObservableNumberValue value) {

        return new SqrtBinding(value);

    }

    public static DoubleBinding pow2(ObservableNumberValue value) {

        return new Pow2Binding(value);
    }

    private static class SqrtBinding extends DoubleBinding {

        final ObservableNumberValue value;

        private SqrtBinding(ObservableNumberValue value) {
            super.bind(value);
            this.value = value;
        }

        @Override
        protected double computeValue() {

            return Math.sqrt(value.getValue().doubleValue());
        }
    }

    private static class Pow2Binding extends DoubleBinding {
        private final ObservableNumberValue value;

        private Pow2Binding(ObservableNumberValue value) {
            super.bind(value);
            this.value = value;
        }

        @Override
        protected double computeValue() {
            return value.getValue().doubleValue() * value.getValue().doubleValue();
        }
    }
}
