package corepluginguiWS;

import java.util.ArrayList;
import java.util.List;

import com.degoos.wetsponge.command.WSCommand;
import com.degoos.wetsponge.command.WSCommandSource;
import com.degoos.wetsponge.entity.living.player.WSPlayer;
import com.degoos.wetsponge.item.WSItemStack;
import com.degoos.wetsponge.material.WSBlockTypes;

public class HatCommand extends WSCommand{

	public HatCommand() {
		super("hat","You Know");
	}

	@Override
	public void executeCommand(WSCommandSource commandSource, String command, String[] arguments) {
		if(!commandSource.hasPermission("coreplugingui.hat"))
		{
			commandSource.sendMessage("No Enough Permission");
			return;
		}
		if(!(commandSource instanceof WSPlayer))
			return;
		if(((WSPlayer)commandSource).getInventory().getItem(((WSPlayer)commandSource).getSelectedHotbarSlot()).isPresent())
		{
			WSItemStack temp=((WSPlayer)commandSource).getInventory().getItem(((WSPlayer)commandSource).getSelectedHotbarSlot()).get();
			if(((WSPlayer)commandSource).getInventory().getItem(39).isPresent())
			{
				((WSPlayer)commandSource).getInventory().setItem(((WSPlayer)commandSource).getInventory().getItem(39).get(), ((WSPlayer)commandSource).getSelectedHotbarSlot());	
			}else {
				((WSPlayer)commandSource).getInventory().setItem(WSItemStack.of(WSBlockTypes.AIR), ((WSPlayer)commandSource).getSelectedHotbarSlot());
			}
			((WSPlayer)commandSource).getInventory().setItem(temp, 39);
			
		}
	}

	@Override
	public List<String> sendTab(WSCommandSource commandSource, String command, String[] arguments) {
		return new ArrayList<String>();
	}

}
