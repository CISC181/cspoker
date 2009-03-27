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
package org.cspoker.client.bots.bot.search.opponentmodel.prolog.cafe;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import jp.ac.kobe_u.cs.prolog.lang.PrologControl;
import net.jcip.annotations.ThreadSafe;

import org.apache.log4j.Logger;
import org.cspoker.client.bots.bot.Bot;
import org.cspoker.client.bots.bot.BotFactory;
import org.cspoker.client.bots.bot.search.SearchBot;
import org.cspoker.client.bots.bot.search.SearchConfiguration;
import org.cspoker.client.bots.bot.search.node.expander.SamplingExpander;
import org.cspoker.client.bots.bot.search.node.leaf.ShowdownNode;
import org.cspoker.client.bots.bot.search.node.leaf.UniformShowdownNode;
import org.cspoker.client.bots.bot.search.node.visitor.Log4JOutputVisitor;
import org.cspoker.client.bots.bot.search.node.visitor.NodeVisitor;
import org.cspoker.client.bots.bot.search.node.visitor.NodeVisitor.Factory;
import org.cspoker.client.bots.bot.search.opponentmodel.OpponentModels;
import org.cspoker.client.bots.listener.BotListener;
import org.cspoker.client.common.SmartLobbyContext;
import org.cspoker.common.elements.player.PlayerId;
import org.cspoker.common.elements.table.TableId;

@ThreadSafe
public class PrologCafeBotFactory implements BotFactory {
	
	private final static Logger logger = Logger.getLogger(PrologCafeBotFactory.class);
	private static int copies = 0;
	
	private final int copy;

	private final Map<PlayerId, OpponentModels> opponentModels = new ConcurrentHashMap<PlayerId, OpponentModels>();
	private final org.cspoker.client.bots.bot.search.node.leaf.ShowdownNode.Factory showdownNodeFactory;
	private final Factory[] nodeVisitorFactories;

	public PrologCafeBotFactory() {
		this(new UniformShowdownNode.Factory(), new NodeVisitor.Factory[]{new Log4JOutputVisitor.Factory(2)});
	}
	
	public PrologCafeBotFactory(ShowdownNode.Factory showdownNodeFactory, NodeVisitor.Factory... nodeVisitorFactories) {
		this.copy = ++copies;
		this.showdownNodeFactory = showdownNodeFactory;
		this.nodeVisitorFactories = nodeVisitorFactories;
	}

	/**
	 * @see org.cspoker.client.bots.bot.BotFactory#createBot(org.cspoker.common.elements.player.PlayerId, org.cspoker.common.elements.table.TableId, org.cspoker.client.common.SmartLobbyContext, java.util.concurrent.ExecutorService, org.cspoker.client.bots.listener.BotListener[])
	 */
	public synchronized Bot createBot(final PlayerId botId, TableId tableId,
			SmartLobbyContext lobby, int buyIn,ExecutorService executor,
			BotListener... botListeners) {
		copies++;
		if(opponentModels.get(botId)==null){
			PrologControl prolog = new PrologControl();
			PrologCafeModel model = new PrologCafeModel(prolog,botId);
			opponentModels.put(botId, model);
		}
		SearchConfiguration config = new SearchConfiguration(
				opponentModels.get(botId), 
				showdownNodeFactory,
				new SamplingExpander.Factory(),
				50,100,250,250,0.25);
		return new SearchBot(botId, tableId, lobby, executor, config, buyIn, nodeVisitorFactories ,botListeners);
	}

	@Override
	public String toString() {
		return "PrologCafeSearchBotv1-"+copy;
	}
}
