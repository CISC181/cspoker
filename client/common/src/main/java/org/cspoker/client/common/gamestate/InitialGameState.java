/**
 * 
 * Copyright 2008 DAI-Labor, Deutsche Telekom Laboratories
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 */
package org.cspoker.client.common.gamestate;

import java.util.*;

import org.cspoker.common.api.lobby.holdemtable.event.HoldemTableTreeEvent;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.player.SeatedPlayer;
import org.cspoker.common.elements.table.DetailedHoldemTable;
import org.cspoker.common.elements.table.Round;
import org.cspoker.common.elements.table.SeatId;
import org.cspoker.common.elements.table.TableConfiguration;

/**
 * @author stephans
 */
public class InitialGameState
		extends AbstractGameState {
	
	private final DetailedHoldemTable table;
	
	/**
	 * @param tableConfiguration
	 */
	public InitialGameState(DetailedHoldemTable table) {
		this.table = table;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getAllSeatedPlayerIds()
	 */
	@Override
	public Set<PlayerId> getAllSeatedPlayerIds() {
		Set<PlayerId> players = new TreeSet<PlayerId>();
		for (SeatedPlayer player : table.getPlayers()) {
			players.add(player.getId());
		}
		return Collections.unmodifiableSet(players);
		
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getCommunityCards()
	 */
	@Override
	public EnumSet<Card> getCommunityCards() {
		return EnumSet.copyOf(table.getCommunityCards());
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getDealer()
	 */
	@Override
	public PlayerId getDealer() {
		return table.getDealer().getId();
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getLargestBet()
	 */
	@Override
	public int getLargestBet() {
		int result = 0;
		for (SeatedPlayer player : table.getPlayers()) {
			result = Math.max(result, player.getBetChipsValue());
		}
		return result;
	}
	
	/**
	 * TODO Horribly complicated hack
	 * 
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getLastBettor()
	 */
	@Override
	public PlayerId getLastBettor() {
		int maxBet = getLargestBet();
		int dealerSeatId = getPlayer(getDealer()).getSeatId().getId();
		List<SeatedPlayer> candidates = new ArrayList<SeatedPlayer>();
		for (SeatedPlayer player : table.getPlayers()) {
			if (player.getBetChipsValue() == maxBet) {
				candidates.add(player);
			}
		}
		for (int i = 0; i < table.getTableConfiguration().getMaxNbPlayers(); i++) {
			for (SeatedPlayer player : candidates) {
				if (player.getSeatId().getId() == dealerSeatId) {
					return player.getId();
				}
			}
			dealerSeatId--;
			if (dealerSeatId < 0) {
				dealerSeatId += table.getTableConfiguration().getMaxNbPlayers();
			}
		}
		
		return null;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getLastEvent()
	 */
	@Override
	public HoldemTableTreeEvent getLastEvent() {
		throw new UnsupportedOperationException("Implement this");
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getMinNextRaise()
	 */
	@Override
	public int getMinNextRaise() {
		throw new UnsupportedOperationException("Implement this");
	}
	
	/**
	 * @return Unknown after initialization, return 0
	 * @see org.cspoker.client.common.gamestate.GameState#getNbRaises()
	 */
	@Override
	public int getNbRaises() {
		return 0;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getNextToAct()
	 */
	@Override
	public PlayerId getNextToAct() {
		throw new UnsupportedOperationException("Implement this");
	}
	
	/**
	 * @param playerId
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getPlayer(org.cspoker.common.elements.player.PlayerId)
	 */
	@Override
	public PlayerState getPlayer(PlayerId playerId) {
		SeatedPlayer selected = null;
		for (SeatedPlayer seated : table.getPlayers()) {
			if (seated.getId().equals(playerId)) {
				selected = seated;
			}
		}
		if (selected == null) {
			return null;
		}
		final SeatedPlayer player = selected;
		return new AbstractPlayerState() {
			
			@Override
			public boolean sitsIn() {
				return player.isSittingIn();
			}
			
			@Override
			public boolean hasFolded() {
				return !player.hasCards();
			}
			
			@Override
			public int getStack() {
				return player.getStackValue();
			}
			
			@Override
			public SeatId getSeatId() {
				return player.getSeatId();
			}
			
			@Override
			public PlayerId getPlayerId() {
				return player.getId();
			}
			
			@Override
			public EnumSet<Card> getCards() {
				return EnumSet.noneOf(Card.class);
			}
			
			@Override
			public List<Integer> getBetProgression() {
				return Collections.singletonList(player.getBetChipsValue());
			}
			
			@Override
			public int getBet() {
				return player.getBetChipsValue();
			}
		};
	}
	
	/**
	 * @param seatId
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getPlayerId(org.cspoker.common.elements.table.SeatId)
	 */
	@Override
	public PlayerId getPlayerId(SeatId seatId) {
		for (SeatedPlayer player : table.getPlayers()) {
			if (player.getSeatId().equals(seatId)) {
				return player.getId();
			}
		}
		return null;
	}
	
	/**
	 * @return Point to yourself ...
	 * @see org.cspoker.client.common.gamestate.GameState#getPreviousGameState()
	 */
	@Override
	public GameState getPreviousGameState() {
		return this;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getPreviousRoundsPotSize()
	 */
	@Override
	public int getPreviousRoundsPotSize() {
		return table.getPots().getTotalValue();
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getRound()
	 */
	@Override
	public Round getRound() {
		return table.getRound();
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getRoundPotSize()
	 */
	@Override
	public int getRoundPotSize() {
		int result = 0;
		for (SeatedPlayer player : table.getPlayers()) {
			result += player.getBetChipsValue();
		}
		return result;
	}
	
	/**
	 * @return
	 * @see org.cspoker.client.common.gamestate.GameState#getTableConfiguration()
	 */
	@Override
	public TableConfiguration getTableConfiguration() {
		return table.getTableConfiguration();
	}
}
