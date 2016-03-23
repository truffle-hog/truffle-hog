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
package edu.kit.trufflehog.model.network;

/**
 * \brief
 * \details
 * \date 22.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class TestAddress implements IAddress {

    private final Integer value;

    public TestAddress(int value) {

        this.value = value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object o) {

        return o instanceof TestAddress && value.equals(((TestAddress)o).value);

    }

    @Override
    public byte[] toByteArray() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public boolean isMulticast() {
        return false;
       // throw new UnsupportedOperationException("Operation not implemented yet");
    }
}
