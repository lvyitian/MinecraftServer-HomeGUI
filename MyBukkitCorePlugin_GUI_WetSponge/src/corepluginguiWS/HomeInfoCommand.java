package corepluginguiWS;

import java.util.ArrayList;
import java.util.List;

import com.degoos.wetsponge.command.WSCommand;
import com.degoos.wetsponge.command.WSCommandSource;
import com.degoos.wetsponge.entity.living.player.WSPlayer;

public class HomeInfoCommand extends WSCommand{
	private Main plugin;

	public HomeInfoCommand(Main plugin) {
		super("homeinfo", "You know");
		this.plugin=plugin;
	}

	@Override
	public void executeCommand(WSCommandSource commandSource, String command, String[] arguments) {
		try {
			if(!commandSource.hasPermission("coreplugingui.homeinfo"))
			{
				commandSource.sendMessage("No Enough Permission");
				return;
			}
			if(commandSource instanceof WSPlayer)
  		  {
  			  long index=0;
  			  if(arguments.length>0 && !arguments[0].equals(""))
  				  index=Long.valueOf(arguments[0]);
  			  long HomeIndex=plugin.getHomeIndex(WSPlayer.class.cast(commandSource));
  			  if(HomeIndex!=-1)
  			  {
  				  if(plugin.isIndexAvailable(HomeIndex,index))
  				  {
  					 long realindex=plugin.getHomeIndexIndex(WSPlayer.class.cast(commandSource),index);
  					 JsonUtil.Home.HomeLocation temp=plugin.config.HomeList.get((int)HomeIndex).LocationList.get((int)realindex);
  					 commandSource.sendMessage("\n\n\n-------------\n"+"HomeIndex:"+temp.Index+"\n"+"RealIndex:"+realindex+"\n"+"World:"+temp.World+"\n"+"X:"+temp.Location.get(0)+"\n"+"Y:"+temp.Location.get(1)+"\n"+"Z:"+temp.Location.get(2)+"\n"+"Yaw:"+temp.Location.get(3)+"\n"+"Pitch:"+temp.Location.get(4)+"\n-------------\n");
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
