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
package org.cspoker.client.commands;

import java.net.MalformedURLException;

import org.cspoker.client.request.GameEventsAckRequest;
import org.cspoker.client.request.GameEventsRequest;

public class GameEventsCommand implements CommandExecutor {

    private GameEventsRequest noack;
    private GameEventsAckRequest ack;

    public GameEventsCommand(String address) throws MalformedURLException{
	this.noack = new GameEventsRequest(address);
	this.ack = new GameEventsAckRequest(address);
    }
    
    public String execute(String... args) throws Exception {
	if(args.length==0){
	    return noack.execute(args);
	}else{
	    return ack.execute(args);
	}
    }

}
