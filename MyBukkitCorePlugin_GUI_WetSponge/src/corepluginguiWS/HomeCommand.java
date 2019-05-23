package corepluginguiWS;

import java.util.ArrayList;
import java.util.List;

import com.degoos.wetsponge.WetSponge;
import com.degoos.wetsponge.command.WSCommand;
import com.degoos.wetsponge.command.WSCommandSource;
import com.degoos.wetsponge.entity.living.player.WSPlayer;
import com.degoos.wetsponge.world.WSLocation;

public class HomeCommand extends WSCommand{
	private Main plugin;

	public HomeCommand(Main plugin) {
		super("home", "You know");
		this.plugin=plugin;
	}

	@Override
	public void executeCommand(WSCommandSource commandSource, String command, String[] arguments) {
		try {
			if(!commandSource.hasPermission("coreplugingui.home.teleport"))
			{
				commandSource.sendMessage("No Enough Permission");
				return;
			}
			if(commandSource instanceof WSPlayer)
			{
				long index=0;
  			  if(arguments.length>0 && !arguments[0].equals(""))
  				  index=Long.valueOf(arguments[0]);
  			  long PlayerIndex=plugin.getHomeIndex(WSPlayer.class.cast(commandSource));
  			  if(PlayerIndex!=-1)
  			  {
  				  JsonUtil.Home temphome=plugin.config.HomeList.get((int)PlayerIndex);
  				  if(plugin.isIndexAvailable(PlayerIndex, index))
  				  {
  					  JsonUtil.Home.HomeLocation location=temphome.LocationList.get((int)plugin.getHomeIndexIndex(WSPlayer.class.cast(commandSource),index));
  					  WSPlayer.class.cast(commandSource).setLocation(WSLocation.of(WetSponge.getServer().getWorld(location.World).get(),location.Location.get(0),location.Location.get(1),location.Location.get(2),Float.valueOf(String.valueOf(location.Location.get(3))),Float.valueOf(String.valueOf(location.Location.get(4)))));
  					  commandSource.sendMessage("已传送至家: Index="+index+" RealIndex="+(int)plugin.getHomeIndexIndex(WSPlayer.class.cast(commandSource),index)+" !");
  				  }
  			  }
			}
		}catch(Throwable e) {}
	}
	
	@Override
	public List<String> sendTab(WSCommandSource commandSource, String command, String[] arguments) {
		return new ArrayList<String>();
	}
}
