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
package org.cspoker.client.bots.bot.search.node.finalround;

import java.util.HashSet;
import java.util.Set;

import org.cspoker.client.bots.bot.search.node.GameTreeNode;
import org.cspoker.client.common.gamestate.GameState;
import org.cspoker.client.common.gamestate.PlayerState;
import org.cspoker.common.elements.cards.Card;
import org.cspoker.common.elements.cards.Deck;
import org.cspoker.common.elements.hand.Hand;
import org.cspoker.common.elements.player.PlayerId;

import com.google.common.base.Predicate;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.google.common.collect.Multiset.Entry;

public class ShowdownNode implements GameTreeNode{

	private PlayerId botId;
	private GameState gameState;
	private Multiset<Integer> EVs = new HashMultiset<Integer>();
	
	public ShowdownNode(PlayerId botId, GameState gameState) {
		this.botId = botId;
		this.gameState = gameState;
	}

	@Override
	public void expand() {
		int nbSamples = 20;
		PlayerState botState = gameState.getPlayer(botId);
		Set<PlayerState> activeOpponents = Sets.filter(gameState.getAllSeatedPlayers(),new Predicate<PlayerState>(){
			@Override
			public boolean apply(PlayerState state) {
				return state.sitsIn() && !state.getPlayerId().equals(botId);
			}
		});
		
		Set<Card> botCards = botState.getCards();
		
		Set<Card> usedCards = new HashSet<Card>(7);
		usedCards.addAll(botCards);
		
		Set<Card> communityCards = sampleCommunityCards(usedCards,gameState.getCommunityCards());
		usedCards.addAll(communityCards);

		int winEV = gameState.getGamePotSize();

		int botRank =  new Hand(Sets.union(communityCards, botCards)).getBestFiveRank();

		for(int i=0;i<nbSamples;i++){
			if(winsSample(activeOpponents, usedCards, communityCards,
					botRank)){
				EVs.add(winEV);
			}else{
				EVs.add(0);
			}
		}
	}

	private boolean winsSample(Set<PlayerState> activeOpponents,
			Set<Card> usedCards, Set<Card> communityCards, int botRank) {
		Deck deck = Deck.createWeaklyRandomDeck();
		for(PlayerState opponent:activeOpponents){
			Set<Card> opponentCards = sampleOpponentCards(opponent,deck,usedCards);
			int opponentRank = new Hand(Sets.union(communityCards, opponentCards)).getBestFiveRank();
			if(opponentRank<botRank){
				return false;
			}
		}
		return true;
	}

	private Set<Card> sampleCommunityCards(Set<Card> usedCards, Set<Card> dealtCommunityCards){
		int nbDealt = dealtCommunityCards.size();
		if(nbDealt==5){
			return dealtCommunityCards;
		}
		Deck deck = Deck.createWeaklyRandomDeck();
		Card[] cards = new Card[5-nbDealt];
		for(int i=0;i<cards.length;i++){
			do{
				cards[i] = deck.drawCard();
			}while(usedCards.contains(cards[i]));
		}
		return Sets.union(dealtCommunityCards, ImmutableSet.of(cards));
	}

	private Set<Card> sampleOpponentCards(PlayerState player, Deck deck, Set<Card> usedCards){
		Card one=null, two=null;
		do{
			one = deck.drawCard();
		}while(usedCards.contains(one));
		do{
			two = deck.drawCard();
		}while(usedCards.contains(two));
		return ImmutableSet.of(one,two);
	}

	@Override
	public double getEV() {
		int EV = 0;
		int size = 0;
		for(Entry<Integer> entry:EVs.entrySet()){
			EV+=entry.getCount()*entry.getElement();
			size+=entry.getCount();
		}
		return ((double)EV)/size;
	}

}