package corepluginguiWS;

import java.util.ArrayList;
import java.util.List;

import com.degoos.wetsponge.command.WSCommand;
import com.degoos.wetsponge.command.WSCommandSource;
import com.degoos.wetsponge.entity.living.player.WSPlayer;

public class SetHomeCommand extends WSCommand{
	private Main plugin;

	public SetHomeCommand(Main plugin) {
		super("sethome","You Know");
		this.plugin=plugin;
	}

	@Override
	public void executeCommand(WSCommandSource commandSource, String command, String[] arguments) {
		try {
		if(!commandSource.hasPermission("coreplugingui.home.set"))
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
					  ArrayList<Double> templ=new ArrayList<Double>();
  				  templ.add(WSPlayer.class.cast(commandSource).getLocation().getX());
  				  templ.add(WSPlayer.class.cast(commandSource).getLocation().getY());
  				  templ.add(WSPlayer.class.cast(commandSource).getLocation().getZ());
  				  templ.add((double) WSPlayer.class.cast(commandSource).getLocation().getYaw());
  				  templ.add((double) WSPlayer.class.cast(commandSource).getLocation().getPitch());
					  JsonUtil.Home.HomeLocation temph=plugin.config.new Home(null,null).new HomeLocation(index,WSPlayer.class.cast(commandSource).getWorld().getName(),templ);
					  long realindex=plugin.getHomeIndexIndex(WSPlayer.class.cast(commandSource),index);
					  temphome.LocationList.remove((int)realindex);
					  temphome.LocationList.add((int)realindex, temph);
					  plugin.config.HomeList.remove((int)PlayerIndex);
					  plugin.config.HomeList.add((int)PlayerIndex,temphome);
					  plugin.SaveConfig();
					  commandSource.sendMessage("已重设家: Index="+index+" RealIndex="+realindex+" !");
				  }else {
  				  ArrayList<Double> templ=new ArrayList<Double>();
  				  templ.add(WSPlayer.class.cast(commandSource).getLocation().getX());
  				  templ.add(WSPlayer.class.cast(commandSource).getLocation().getY());
  				  templ.add(WSPlayer.class.cast(commandSource).getLocation().getZ());
  				  templ.add((double) WSPlayer.class.cast(commandSource).getLocation().getYaw());
  				  templ.add((double) WSPlayer.class.cast(commandSource).getLocation().getPitch());
  				  JsonUtil.Home.HomeLocation temph=plugin.config.new Home(null,null).new HomeLocation(index,WSPlayer.class.cast(commandSource).getWorld().getName(),templ);
  				  temphome.LocationList.add(temph);
  				  plugin.config.HomeList.remove((int)PlayerIndex);
					  plugin.config.HomeList.add((int)PlayerIndex,temphome);
					  plugin.SaveConfig();
					  commandSource.sendMessage("已设置家: Index="+index+" !");
				  }
			  }else {
				  ArrayList<JsonUtil.Home.HomeLocation> tempa=new ArrayList<JsonUtil.Home.HomeLocation>();
				  ArrayList<Double> templ=new ArrayList<Double>();
				  templ.add(WSPlayer.class.cast(commandSource).getLocation().getX());
				  templ.add(WSPlayer.class.cast(commandSource).getLocation().getY());
				  templ.add(WSPlayer.class.cast(commandSource).getLocation().getZ());
				  templ.add((double) WSPlayer.class.cast(commandSource).getLocation().getYaw());
				  templ.add((double) WSPlayer.class.cast(commandSource).getLocation().getPitch());
				  tempa.add(plugin.config.new Home(null,null).new HomeLocation(index,WSPlayer.class.cast(commandSource).getWorld().getName(),templ));
			      plugin.config.HomeList.add(plugin.config.new Home(WSPlayer.class.cast(commandSource).getUniqueId().toString(),tempa));
			      plugin.SaveConfig();
			  }
		  }
		}catch(Throwable e) {}
	}

	@Override
	public List<String> sendTab(WSCommandSource commandSource, String command, String[] arguments) {
		return new ArrayList<String>();
	}

}
