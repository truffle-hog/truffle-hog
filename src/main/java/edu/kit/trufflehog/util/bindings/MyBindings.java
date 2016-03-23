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
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.StringProperty;
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
