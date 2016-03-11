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

package edu.kit.trufflehog.presenter.nativebuild.osx;

import edu.kit.trufflehog.presenter.nativebuild.NativeBuilder;
import org.apache.logging.log4j.LogManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

/**
 * <p>
 * </p>
 *
 * @author Julian Brendl
 * @version 1.0
 */
public class OSXBuilder implements NativeBuilder {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger();

    @Override
    public void build() {
        // Set docking icon on mac
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("mac")) {
            try {
                URL iconURL = OSXBuilder.class.getResource(File.separator + "edu" + File.separator + "kit" + File.separator
                        + "trufflehog" + File.separator + "view" + File.separator +"icon.png");
                Image image = new ImageIcon(iconURL).getImage();
                com.apple.eawt.Application.getApplication().setDockIconImage(image);
            } catch (Exception e) {
                logger.error("Unable to set docking icon, probably not running on a mac", e);
            }
        }
    }
}
