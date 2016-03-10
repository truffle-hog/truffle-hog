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
 * You should have received a copy of the GNU General Public License
 * along with TruffleHog.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.kit.trufflehog.view;

import javafx.scene.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class FilterMenuInputParser {
    private final Text errorText;

    public FilterMenuInputParser(Text errorText) {
        this.errorText = errorText;
    }

    public boolean checkName(String name) {
        // Make sure name is not null
        if (name == null || name.equals("")) {
            errorText.setText("Please enter a name.");
            return false;
        } else if (name.length() > 50) {
            errorText.setText("Name cannot be longer than 50 characters.");
            return false;
        }

        Pattern namePattern = Pattern.compile("[A-Za-z0-9. _-]{1,50}");
        Matcher nameMatcher = namePattern.matcher(name);

        return nameMatcher.matches();
    }
}
