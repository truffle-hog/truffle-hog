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
package edu.kit.trufflehog.command.usercommand;

import edu.kit.trufflehog.model.network.graph.INode;
import edu.uci.ics.jung.visualization.picking.PickedState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * \brief
 * \details
 * \date 09.03.16
 * \copyright GNU Public License
 *
 * @author Jan Hermes
 * @version 0.0.1
 */
public class NodeSelectionCommand implements IUserCommand<PickedState<INode>> {

    private static final Logger logger = LogManager.getLogger();

    // TODO put empty pickedState
    private PickedState<INode> pickedState;

    boolean lastTimeEmpty = true;

    @Override
    public <S extends PickedState<INode>> void setSelection(S selection) {

        pickedState = selection;
        //throw new UnsupportedOperationException("Operation not implemented yet");
    }

    @Override
    public void execute() {

        if (lastTimeEmpty)

        logger.debug(pickedState.getPicked().toString());

/*        pickedState.getPicked().stream().forEach(node -> {
            //TODO: Do stuff on nodes
        });*/

        //throw new UnsupportedOperationException("Operation not implemented yet");
    }
}
