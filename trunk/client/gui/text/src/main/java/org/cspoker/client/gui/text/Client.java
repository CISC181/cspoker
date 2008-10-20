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
package org.cspoker.client.gui.text;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.security.auth.login.LoginException;

import org.cspoker.client.gui.text.commands.BetOrRaseCommand;
import org.cspoker.client.gui.text.commands.CardsCommand;
import org.cspoker.client.gui.text.commands.CheckOrCallCommand;
import org.cspoker.client.gui.text.commands.Command;
import org.cspoker.client.gui.text.commands.CreateTableCommand;
import org.cspoker.client.gui.text.commands.FoldCommand;
import org.cspoker.client.gui.text.commands.GetTableCommand;
import org.cspoker.client.gui.text.commands.GetTablesCommand;
import org.cspoker.client.gui.text.commands.HelpCommand;
import org.cspoker.client.gui.text.commands.JoinTableCommand;
import org.cspoker.client.gui.text.commands.LeaveTableCommand;
import org.cspoker.client.gui.text.commands.PotCommand;
import org.cspoker.client.gui.text.commands.ServerChatCommand;
import org.cspoker.client.gui.text.commands.SitInCommand;
import org.cspoker.client.gui.text.commands.TableChatCommand;
import org.cspoker.client.gui.text.eventlistener.PrintListener;
import org.cspoker.client.gui.text.savedstate.Cards;
import org.cspoker.client.gui.text.savedstate.Pot;
import org.cspoker.common.RemoteCSPokerServer;
import org.cspoker.common.api.chat.context.RemoteChatContext;
import org.cspoker.common.api.lobby.context.RemoteLobbyContext;
import org.cspoker.common.api.lobby.holdemtable.context.RemoteHoldemTableContext;
import org.cspoker.common.api.lobby.holdemtable.holdemplayer.context.RemoteHoldemPlayerContext;
import org.cspoker.common.api.shared.context.RemoteServerContext;
import org.cspoker.common.api.shared.exception.IllegalActionException;
import org.cspoker.common.api.shared.listener.UniversalServerListener;

/**
 * Connect to the given server and passes on user commands.
 */
public class Client {

	private HashMap<String, Command> commands = new HashMap<String, Command>();
	
	private Console console;

	private RemoteServerContext serverContext;

	private UniversalServerListener serverlistener;

	private final RemoteChatContext serverChatContext;

	private final RemoteLobbyContext lobbyContext;

	private RemoteHoldemPlayerContext currentPlayerContext;

	private RemoteHoldemTableContext currentTableContext;

	private long currentTableID;

	private RemoteChatContext currentTableChatContext;

	public Client(final String username, final String password,
			Console console, RemoteCSPokerServer server) throws RemoteException, LoginException, IllegalActionException {
		this.console = console;
		serverContext = server.login(username, password);
		serverlistener = new UniversalServerListener(new PrintListener(console));
		serverChatContext = serverContext.getServerChatContext(serverlistener);
		lobbyContext = serverContext.getLobbyContext(serverlistener);
		registerCommands();
	}
	
	private void registerCommands() {
		//Lobby
		commands.put("CREATETABLE", new CreateTableCommand(this, console));
		commands.put("JOINTABLE", new JoinTableCommand(this, console));
		commands.put("GETTABLE", new GetTableCommand(this, console));
		commands.put("GETTABLES", new GetTablesCommand(this, console));

		//Table
		commands.put("LEAVETABLE", new LeaveTableCommand(this, console));
		commands.put("SITIN", new SitInCommand(this,console));
		
		//Game
		commands.put("CHECK", new CheckOrCallCommand(this, console));
		commands.put("CALL", new CheckOrCallCommand(this, console));
		commands.put("BET", new BetOrRaseCommand(this, console));
		commands.put("RAISE", new BetOrRaseCommand(this, console));
		commands.put("FOLD", new FoldCommand(this, console));

		//Chat
		commands.put("SERVERCHAT", new ServerChatCommand(this, console));
		commands.put("TABLECHAT", new TableChatCommand(this, console));

		//Local
		Cards cards = new Cards();
		Pot pot = new Pot();
		CardsCommand cardsCommand = new CardsCommand(console, cards);
		commands.put("CARDS", cardsCommand);
		
		PotCommand potCommand = new PotCommand(console, pot);
		commands.put("POT", potCommand);

		HelpCommand help = new HelpCommand(console);
		commands.put("HELP", help);
	}

	private Command getCommand(String name) {
		return commands.get(name.toUpperCase());
	}

	public void execute(String command, String... args) throws Exception {
		Command c = getCommand(command);
		if (c == null) {
			throw new IllegalArgumentException("Not a valid command.");
		}
		c.execute(args);
	}

	public void close() {
		// no op
	}

	public RemoteServerContext getServerContext() {
		return this.serverContext;
	}
	
	public RemoteLobbyContext getLobbyContext() {
		return this.lobbyContext;
	}
	
	public RemoteChatContext getServerChatContext() {
		return this.serverChatContext;
	}

	public RemoteHoldemPlayerContext getCurrentPlayerContext() {
		return currentPlayerContext;
	}
	
	public void setCurrentPlayerContext(
			RemoteHoldemPlayerContext currentPlayerContext) {
		this.currentPlayerContext = currentPlayerContext;
	}

	public RemoteHoldemTableContext getCurrentTableContext() {
		return currentTableContext;
	}

	public void setCurrentTableContext(
			RemoteHoldemTableContext currentTableContext) {
		this.currentTableContext = currentTableContext;
	}

	public long getCurrentTableID() {
		return currentTableID;
	}
	
	public void setCurrentTableID(long currentTableID) {
		this.currentTableID = currentTableID;
	}

	public void setCurrentTableChatContext(RemoteChatContext tableChatContext) {
		this.currentTableChatContext = tableChatContext;
	}
	
	public RemoteChatContext getCurrentTableChatContext() {
		return this.currentTableChatContext;
	}
	
}