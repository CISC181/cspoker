package game.rules;

import game.rounds.Round;

/**
 * Class for the NoLimit betting game.rounds.rules used in Texas Hold'em.
 * @author Cedric
 */
public class NoLimit extends BettingRules {	
	/**********************************************************
	 * Constructors
	 **********************************************************/
	
	public NoLimit() {
	}
	/**********************************************************
	 * Raise
	 **********************************************************/
	@Override
	public boolean isValidRaise(int amount, Round round) {
		if(amount<getLastBetAmount()){
			setLastRaiseErrorMessage("ERROR : the betted amount must be at least the amount of the previous bet/raise" +
					" in the current round");
			return false;
		}
		return super.isValidRaise(amount, round);
	}
	@Override
	public String toString() {
		return "No Limit Poker";
	}
}