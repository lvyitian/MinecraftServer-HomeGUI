package corepluginguiWS;

import java.util.ArrayList;
import java.util.List;

import com.degoos.wetsponge.command.WSCommand;
import com.degoos.wetsponge.command.WSCommandSource;
import com.degoos.wetsponge.entity.living.player.WSPlayer;

public class DelHomeCommand extends WSCommand{
	private Main plugin;

	public DelHomeCommand(Main plugin) {
		super("delhome", "You know");
		this.plugin=plugin;
	}

	@Override
	public void executeCommand(WSCommandSource commandSource, String command, String[] arguments) {
		try {
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
  					  plugin.config.HomeList.get((int)HomeIndex).LocationList.remove((int)realindex);
  					  plugin.SaveConfig();
  					  commandSource.sendMessage("ÒÑÉ¾³ý¼Ò: Index="+index+" RealIndex="+realindex+" !");
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
