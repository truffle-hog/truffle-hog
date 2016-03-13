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
package edu.kit.trufflehog.view.animation;

import edu.kit.trufflehog.model.network.graph.components.ViewComponent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.Collection;

/**
 * \brief Basic animation class for graph animations
 * \details
 * \date 12.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class GraphAnimator implements EventHandler<ActionEvent> {

/*    private final Collection<ViewComponent> backing = new ArrayList<>();

    private final Collection<ViewComponent> viewComponents = new ArrayList<>();*/

    private Collection<ViewComponent> active = new ArrayList<>();

    private Collection<ViewComponent> inactive = new ArrayList<>();

    public GraphAnimator() {

    }

    private Collection<ViewComponent> getActive() {

        return active;
    }

    private Collection<ViewComponent> getInactive() {

        return inactive;
    }

    private void switchActive() {

        final Collection<ViewComponent> tmp = active;
        active = inactive;
        inactive = tmp;
    }

    public void addView(ViewComponent viewComponent) {

        getInactive().add(viewComponent);
    }


    @Override
    public void handle(ActionEvent event) {

        getActive().stream().parallel().forEach(ViewComponent::animate);
        getActive().clear();
        switchActive();
    }

    public void animate(ViewComponent component) {

    }
}
