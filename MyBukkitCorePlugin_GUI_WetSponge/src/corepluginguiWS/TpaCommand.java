package corepluginguiWS;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.degoos.wetsponge.WetSponge;
import com.degoos.wetsponge.command.WSCommand;
import com.degoos.wetsponge.command.WSCommandSource;
import com.degoos.wetsponge.entity.living.player.WSPlayer;

public class TpaCommand extends WSCommand{
	private Main plugin;

	public TpaCommand(Main plugin) {
		super("tpa", "You know");
		this.plugin=plugin;
	}

	@Override
	public void executeCommand(WSCommandSource commandSource, String command, String[] arguments) {
		try {
			if(!commandSource.hasPermission("coreplugingui.tpa.use"))
			{
				commandSource.sendMessage("No Enough Permission");
				return;
			}
			if(commandSource instanceof WSPlayer)
			{
				if(arguments.length>0 && !arguments[0].equals(""))
  			  {
  				  if(WetSponge.getServer().getPlayer(arguments[0]).isPresent())
  				  {
  					  TreeMap<WSPlayer,Long> temptree=null;
  					  if(plugin.TeleportList.containsKey(WSPlayer.class.cast(commandSource)) && plugin.TeleportList.get(WSPlayer.class.cast(commandSource)) != null)
  					  {
  						  temptree=plugin.TeleportList.get(WSPlayer.class.cast(commandSource));
  					  }else {
  						  temptree=new TreeMap<WSPlayer,Long>();  
  					  }
  					  temptree.put(WetSponge.getServer().getPlayer(arguments[0]).get(), System.nanoTime()+10000000000L);
  					  if(plugin.TeleportList.containsKey(WSPlayer.class.cast(commandSource)))
  					  {
  						  plugin.TeleportList.remove(WSPlayer.class.cast(commandSource));
  						  plugin.TeleportList.put(WSPlayer.class.cast(commandSource),temptree);
  					  }else {
  						  plugin.TeleportList.put(WSPlayer.class.cast(commandSource),temptree);  
  					  }
  					  WetSponge.getServer().getPlayer(arguments[0]).get().sendMessage("Player "+WSPlayer.class.cast(commandSource).getName()+" is trying to teleport you. Please type /tpaccept or /tpadeny in 10 seconds to response it.");
  					  commandSource.sendMessage("Success to sending teleport response.");
  				  }
  			  }else {
  				  commandSource.sendMessage("/tpa <Player>");
  			  }
			}
		}catch(Throwable e) {}
	}
	
	@Override
	public List<String> sendTab(WSCommandSource commandSource, String command, String[] arguments) {
		return new ArrayList<String>();
	}
}
