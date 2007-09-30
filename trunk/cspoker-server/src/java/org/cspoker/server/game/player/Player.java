/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.cspoker.server.game.player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.cspoker.server.game.PlayerId;
import org.cspoker.server.game.elements.cards.deck.Deck.Card;
import org.cspoker.server.game.elements.chips.Chips;
import org.cspoker.server.game.elements.chips.IllegalValueException;

/**
 * A class to represent players: bots or humans.
 * 
 * @author Kenzo
 * 
 */
public class Player {

    /***************************************************************************
     * Variables
     **************************************************************************/

    /**
     * The variable containing the id of the player.
     */
    private final PlayerId id;

    /**
     * The name of the player.
     */
    private final String name;

    /**
     * The stack of this player.
     */
    private final Chips chips;

    /**
     * The chips the player has bet in this round.
     * 
     */
    private final Chips betChips;

    /**
     * The hidden cards.
     */
    private final List<Card> pocketCards;

    /***************************************************************************
     * Constructor
     **************************************************************************/

    /**
     * Construct a new player with given id, name and initial number of chips.
     * 
     * @throws IllegalValueException
     *                 [must] The given initial value is not valid.
     * 
     * @post The chips pile is effective and the value of chips is the same as
     *       the given initial value. |new.getBetChips()!=null &&
     *       new.getChips.getValue()==initialNbChips
     * @post The bet chips pile is effective and There are no chips on this
     *       pile. |new.getBetChips()!=null && new.getBetChips().getValue()==0
     */
    Player(PlayerId id, String name, int initialNbChips)
	    throws IllegalValueException {
	this.id = id;
	this.name = name;
	chips = new Chips(initialNbChips);
	betChips = new Chips();
	pocketCards = new CopyOnWriteArrayList<Card>();
    }

    /***************************************************************************
     * Name
     **************************************************************************/

    /**
     * Returns the name of this player.
     * 
     * @return The name of this player.
     */
    public String getName() {
	return name;
    }

    /***************************************************************************
     * Id
     **************************************************************************/

    /**
     * Returns the id of this player.
     * 
     * @return The id of this player.
     */
    public PlayerId getId() {
	return id;
    }

    /***************************************************************************
     * Chips
     **************************************************************************/

    public Chips getStack() {
	return chips;
    }

    public Chips getBetChips() {
	return betChips;
    }

    public void transferAmountToBetPile(int amount)
	    throws IllegalValueException {
	getStack().transferAmountTo(amount, getBetChips());
    }

    public void transferAllChipsToBetPile() throws IllegalValueException {
	getStack().transferAllChipsTo(getBetChips());
    }

    /***************************************************************************
     * Cards
     **************************************************************************/

    /**
     * Deal a pocket card to this player.
     * 
     */
    public void dealPocketCard(Card card) {
	pocketCards.add(card);
    }

    /**
     * Returns the pocket cards of this player.
     * 
     * A change in the returned list, does not change the internal
     * representation.
     * 
     * @return The pocket cards of this player.
     */
    public List<Card> getPocketCards() {
	return new ArrayList<Card>(pocketCards);
    }

    public void clearPocketCards() {
	pocketCards.clear();
    }

    @Override
    public String toString() {
	return getId() + ": " + getName() + " ($" + getStack() + " in chips)";
    }

    /**
     * Returns a hash code value for this player.
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	return result;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	final Player other = (Player) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }

    public SavedPlayer getSavedPlayer() {
	return new SavedPlayer(this);
    }
}