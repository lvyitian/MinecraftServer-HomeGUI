package corepluginguiWS;

import java.util.ArrayList;
import java.util.List;

import com.degoos.wetsponge.command.WSCommand;
import com.degoos.wetsponge.command.WSCommandSource;

public class CorePluginCommand extends WSCommand{
	private Main plugin;

	public CorePluginCommand(Main plugin) {
		super("coreplugin", "You know");
		this.plugin=plugin;
	}

	@Override
	public void executeCommand(WSCommandSource commandSource, String command, String[] arguments) {
		try {
			if(!commandSource.hasPermission("coreplugingui.admin"))
			{
				commandSource.sendMessage("No Enough Permission");
				return;
			}
			if(arguments.length>0 && !arguments[0].equals(""))
			{
				if(arguments[0].equals("reload"))
				{
					plugin.ReloadConfig();
				}else {
				  commandSource.sendMessage("ChildCommandList:");
  				  for(int i=0;i<plugin.ChildCommandList.size();i++)
  					  commandSource.sendMessage(plugin.ChildCommandList.get(i));
				}
			}else {
				commandSource.sendMessage("/CorePlugin <ChildCommand>");
			}
		}catch(Throwable e) {}
	}
	
	@Override
	public List<String> sendTab(WSCommandSource commandSource, String command, String[] arguments) {
		return new ArrayList<String>();
	}
}
