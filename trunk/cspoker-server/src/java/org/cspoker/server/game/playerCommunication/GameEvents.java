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
package org.cspoker.server.game.playerCommunication;

import java.util.Iterator;
import java.util.List;

import org.cspoker.server.game.events.gameEvents.GameEvent;

public class GameEvents implements Iterable<GameEvent>{
	
	private final List<GameEvent> events;
	
	private final int latestEventNumber; 
	
	public GameEvents(List<GameEvent> events, int latestEventNumber){
		this.events = events;
		this.latestEventNumber = latestEventNumber;
	}
	
	public List<GameEvent> getGameEvents(){
		return events;
	}
	
	public int getLastEventNumber(){
		return latestEventNumber;
	}
	
	@Override
	public String toString(){
		return "events until "+latestEventNumber+". "+events.toString();
	}
	public Iterator<GameEvent> iterator() {
	    return events.iterator();
	}

}
