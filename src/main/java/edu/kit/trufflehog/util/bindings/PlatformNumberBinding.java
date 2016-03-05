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
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Binding;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.NumberExpression;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

/**
 * \brief
 * \details
 * \date 04.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class PlatformNumberBinding implements NumberExpression, Binding<Number>, Observable, ObservableIntegerValue, ObservableNumberValue, ObservableValue<Number>, NumberBinding, ChangeListener<Number> {

    private static final Logger logger = LogManager.getLogger(PlatformNumberBinding.class);

    private int value;

    //private final IntegerProperty property;

    public PlatformNumberBinding(IntegerProperty integerProperty) {
     //   property = integerProperty;
        value = integerProperty.get();
       // super.bind(integerProperty);
        integerProperty.addListener(this);
    }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

        Platform.runLater(() -> value = newValue.intValue());
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void invalidate() {

    }

    @Override
    public ObservableList<?> getDependencies() {
        return null;
    }

    @Override
    public void dispose() {

    }

    @Override
    public NumberBinding negate() {
        return null;
    }

    @Override
    public NumberBinding add(ObservableNumberValue other) {
        return null;
    }

    @Override
    public NumberBinding add(double other) {
        return null;
    }

    @Override
    public NumberBinding add(float other) {
        return null;
    }

    @Override
    public NumberBinding add(long other) {
        return null;
    }

    @Override
    public NumberBinding add(int other) {
        return null;
    }

    @Override
    public NumberBinding subtract(ObservableNumberValue other) {
        return null;
    }

    @Override
    public NumberBinding subtract(double other) {
        return null;
    }

    @Override
    public NumberBinding subtract(float other) {
        return null;
    }

    @Override
    public NumberBinding subtract(long other) {
        return null;
    }

    @Override
    public NumberBinding subtract(int other) {
        return null;
    }

    @Override
    public NumberBinding multiply(ObservableNumberValue other) {
        return null;
    }

    @Override
    public NumberBinding multiply(double other) {
        return null;
    }

    @Override
    public NumberBinding multiply(float other) {
        return null;
    }

    @Override
    public NumberBinding multiply(long other) {
        return null;
    }

    @Override
    public NumberBinding multiply(int other) {
        return null;
    }

    @Override
    public NumberBinding divide(ObservableNumberValue other) {
        return null;
    }

    @Override
    public NumberBinding divide(double other) {
        return null;
    }

    @Override
    public NumberBinding divide(float other) {
        return null;
    }

    @Override
    public NumberBinding divide(long other) {
        return null;
    }

    @Override
    public NumberBinding divide(int other) {
        return null;
    }

    @Override
    public BooleanBinding isEqualTo(ObservableNumberValue other) {
        return null;
    }

    @Override
    public BooleanBinding isEqualTo(ObservableNumberValue other, double epsilon) {
        return null;
    }

    @Override
    public BooleanBinding isEqualTo(double other, double epsilon) {
        return null;
    }

    @Override
    public BooleanBinding isEqualTo(float other, double epsilon) {
        return null;
    }

    @Override
    public BooleanBinding isEqualTo(long other) {
        return null;
    }

    @Override
    public BooleanBinding isEqualTo(long other, double epsilon) {
        return null;
    }

    @Override
    public BooleanBinding isEqualTo(int other) {
        return null;
    }

    @Override
    public BooleanBinding isEqualTo(int other, double epsilon) {
        return null;
    }

    @Override
    public BooleanBinding isNotEqualTo(ObservableNumberValue other) {
        return null;
    }

    @Override
    public BooleanBinding isNotEqualTo(ObservableNumberValue other, double epsilon) {
        return null;
    }

    @Override
    public BooleanBinding isNotEqualTo(double other, double epsilon) {
        return null;
    }

    @Override
    public BooleanBinding isNotEqualTo(float other, double epsilon) {
        return null;
    }

    @Override
    public BooleanBinding isNotEqualTo(long other) {
        return null;
    }

    @Override
    public BooleanBinding isNotEqualTo(long other, double epsilon) {
        return null;
    }

    @Override
    public BooleanBinding isNotEqualTo(int other) {
        return null;
    }

    @Override
    public BooleanBinding isNotEqualTo(int other, double epsilon) {
        return null;
    }

    @Override
    public BooleanBinding greaterThan(ObservableNumberValue other) {
        return null;
    }

    @Override
    public BooleanBinding greaterThan(double other) {
        return null;
    }

    @Override
    public BooleanBinding greaterThan(float other) {
        return null;
    }

    @Override
    public BooleanBinding greaterThan(long other) {
        return null;
    }

    @Override
    public BooleanBinding greaterThan(int other) {
        return null;
    }

    @Override
    public BooleanBinding lessThan(ObservableNumberValue other) {
        return null;
    }

    @Override
    public BooleanBinding lessThan(double other) {
        return null;
    }

    @Override
    public BooleanBinding lessThan(float other) {
        return null;
    }

    @Override
    public BooleanBinding lessThan(long other) {
        return null;
    }

    @Override
    public BooleanBinding lessThan(int other) {
        return null;
    }

    @Override
    public BooleanBinding greaterThanOrEqualTo(ObservableNumberValue other) {
        return null;
    }

    @Override
    public BooleanBinding greaterThanOrEqualTo(double other) {
        return null;
    }

    @Override
    public BooleanBinding greaterThanOrEqualTo(float other) {
        return null;
    }

    @Override
    public BooleanBinding greaterThanOrEqualTo(long other) {
        return null;
    }

    @Override
    public BooleanBinding greaterThanOrEqualTo(int other) {
        return null;
    }

    @Override
    public BooleanBinding lessThanOrEqualTo(ObservableNumberValue other) {
        return null;
    }

    @Override
    public BooleanBinding lessThanOrEqualTo(double other) {
        return null;
    }

    @Override
    public BooleanBinding lessThanOrEqualTo(float other) {
        return null;
    }

    @Override
    public BooleanBinding lessThanOrEqualTo(long other) {
        return null;
    }

    @Override
    public BooleanBinding lessThanOrEqualTo(int other) {
        return null;
    }

    @Override
    public StringBinding asString() {
        return null;
    }

    @Override
    public StringBinding asString(String format) {
        return null;
    }

    @Override
    public StringBinding asString(Locale locale, String format) {
        return null;
    }

    @Override
    public int intValue() {
        return 0;
    }

    @Override
    public long longValue() {
        return 0;
    }

    @Override
    public float floatValue() {
        return 0;
    }

    @Override
    public double doubleValue() {
        return 0;
    }

    @Override
    public void addListener(ChangeListener<? super Number> listener) {

    }

    @Override
    public void removeListener(ChangeListener<? super Number> listener) {

    }

    @Override
    public Number getValue() {
        return null;
    }

    @Override
    public void addListener(InvalidationListener listener) {

    }

    @Override
    public void removeListener(InvalidationListener listener) {

    }

    @Override
    public int get() {
        return 0;
    }
}
