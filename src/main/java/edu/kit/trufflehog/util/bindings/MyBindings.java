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

import edu.kit.trufflehog.model.network.IPAddress;
import edu.kit.trufflehog.model.network.MacAddress;
import edu.kit.trufflehog.model.network.graph.components.node.NodeInfoComponent;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * \brief
 * \details
 * \date 22.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 *
 * //TODO rename?
 * //TODO document!
 */
public class MyBindings {

    private static final Logger logger = LogManager.getLogger(MyBindings.class);

    public static DoubleBinding divideIntToDouble(ObservableNumberValue divisor, ObservableNumberValue divider) {
        return new DivideIntToDoubleBinding(divisor, divider);
    }

    public static StringBinding nodeInfoStringBinding(NodeInfoComponent nic) {
        return new NodeInfoStringBinding(nic);
    }

    public static DoubleBinding sqrt(ObservableNumberValue value) {

        return new SqrtBinding(value);

    }

    public static DoubleBinding pow2(ObservableNumberValue value) {

        return new Pow2Binding(value);
    }

    public static void bindBidirectionalWithOffset(DoubleProperty doubleProperty, DoubleProperty doubleProperty1, DoubleBinding multiply) {

        new BidirectionalDoubleOffsetBinding(doubleProperty, doubleProperty1, multiply);

    }

    private static class BidirectionalDoubleOffsetBinding implements ChangeListener<Number> {

        private final DoubleBinding offset;
        private final DoubleProperty lhs;
        private final DoubleProperty rhs;

        private BidirectionalDoubleOffsetBinding(DoubleProperty property1, DoubleProperty property2, DoubleBinding offset) {


            this.offset = offset;
            this.lhs = property1;
            this.rhs = property2;

            lhs.set(rhs.get() + offset.get());

            rhs.addListener(this);
            lhs.addListener(this);
            offset.addListener(this);
        }

        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

            if (observable.equals(lhs)) {
                rhs.setValue(lhs.get() - offset.get());
            } else if (observable.equals(rhs)) {
                lhs.setValue(rhs.get() + offset.get());
            } else {
                lhs.setValue(rhs.get() + offset.get());
            }

        }
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

    private static class DivideIntToDoubleBinding extends DoubleBinding {
        private final ObservableNumberValue divisor;
        private final ObservableNumberValue divider;

        private DivideIntToDoubleBinding(ObservableNumberValue divisor, ObservableNumberValue divider) {
            super.bind(divisor, divider);
            this.divisor = divisor;
            this.divider = divider;
        }

        @Override
        protected double computeValue() {
            return divisor.getValue().doubleValue() / divider.getValue().doubleValue();
        }
    }

    private static class NodeInfoStringBinding extends StringBinding {
        private final NodeInfoComponent nodeInfoComponent;

        private NodeInfoStringBinding(NodeInfoComponent nic) {
            super.bind(nic.getDeviceNameProperty(), nic.getIpAddressProperty(), nic.getMacAddressProperty());
            this.nodeInfoComponent = nic;
        }

        @Override
        protected String computeValue() {
            final String deviceName = nodeInfoComponent.getDeviceName();
            final MacAddress macAddress = nodeInfoComponent.getMacAddress();
            final IPAddress ipAddress = nodeInfoComponent.getIPAddress();

            if (deviceName != null) {
                return deviceName + " (" + macAddress + ")";
            }

            if (ipAddress != null) {
                return ipAddress + " (" + macAddress + ")";
            }

            return macAddress.toString();
        }
    }
}
