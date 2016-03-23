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
package edu.kit.trufflehog.model.network.graph.components;

import edu.kit.trufflehog.model.network.graph.IComponent;
import edu.kit.trufflehog.model.network.graph.IComposition;
import edu.kit.trufflehog.model.network.graph.IUpdater;

/**
 * \brief
 * \details
 * \date 05.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class ViewComponent extends AbstractComponent implements IComponent {

    private final IRenderer renderer;
    private IComposition parent;

    public ViewComponent(IRenderer renderer) {
        this.renderer = renderer;
    }

    public void animate() {
        renderer.animate();
    }

    public IRenderer getRenderer() {
        return renderer;
    }

    @Override
    public String name() {
        return "View component";
    }

    @Override
    public <T> T accept(IComponentVisitor<T> visitor) {

        return visitor.visit(this);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public void setParent(IComposition parent) {
        this.parent = parent;
    }

    @Override
    public IComposition getParent() {
        return parent;
    }

    @Override
    public boolean update(IComponent instance, IUpdater updater) {

        if (instance == null) throw new NullPointerException("instance must not be null!");
        if (updater == null) throw new NullPointerException("updater must not be null!");
        return updater.update(this, instance);
    }
}
