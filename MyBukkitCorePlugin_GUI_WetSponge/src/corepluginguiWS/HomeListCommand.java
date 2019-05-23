package corepluginguiWS;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.meta.ItemMeta;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.text.Text;

import com.degoos.wetsponge.WetSponge;
import com.degoos.wetsponge.command.WSCommand;
import com.degoos.wetsponge.command.WSCommandSource;
import com.degoos.wetsponge.entity.living.player.WSPlayer;
import com.degoos.wetsponge.inventory.WSInventory;
import com.degoos.wetsponge.item.WSItemStack;
import com.degoos.wetsponge.material.WSBlockTypes;
import com.degoos.wetsponge.text.WSText;
import com.degoos.wetsponge.text.action.click.WSRunCommandAction;
import com.degoos.wetsponge.text.action.hover.WSShowTextAction;

public class HomeListCommand extends WSCommand{
	private Main plugin;

	public HomeListCommand(Main plugin) {
		super("homelist", "You know");
		this.plugin=plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void executeCommand(WSCommandSource commandSource, String command, String[] arguments) {
		try {
			if(!commandSource.hasPermission("coreplugingui.homelist"))
			{
				commandSource.sendMessage("No Enough Permission");
				return;
			}
			if(commandSource instanceof WSPlayer)
  		    {
  			  /*if(args.length>0)
  				Player.class.cast(sender).closeInventory(Reason.PLUGIN);*/
            //if(!InventoryList.containsKey(Player.class.cast(sender).getUniqueId().toString())){
  				    long startpage=1;
  				    if(arguments.length>0 && !arguments[0].equals(""))
  				      startpage=Long.parseLong(arguments[0]);
  				    if(startpage<1)
  				    	return;
  				    ArrayList<Long> homelist=new ArrayList<Long>();
  				    		try {
  				    homelist=plugin.getAvailableIndexes(WSPlayer.class.cast(commandSource));
  				    		}catch(Throwable e2) {}
  	    			ArrayList<JsonUtil.Home.HomeLocation> tempstorage=new ArrayList<JsonUtil.Home.HomeLocation>();
  	    			for(int i=0;i<homelist.size();i++)
  	    			{
  	    		    tempstorage.add(plugin.config.HomeList.get((int)plugin.getHomeIndex(WSPlayer.class.cast(commandSource))).LocationList.get(i));
  	    			}
  	    			Comparator<JsonUtil.Home.HomeLocation> tempcom=(JsonUtil.Home.HomeLocation o1,JsonUtil.Home.HomeLocation o2)->{
  	    				if(o1.Index>o2.Index)
  	    				{
  	    					return 1;
  	    				}else if(o1.Index==o2.Index)
  	    				{
  	    					return 0;
  	    				}else {
  	    					return -1;
  	    				}
  	    			};
  	    			tempstorage.sort(tempcom);
  	    			HashMap<Long,ArrayList<JsonUtil.Home.HomeLocation>> pages=new HashMap<Long,ArrayList<JsonUtil.Home.HomeLocation>>();
  	    			long page_temp=1;
  	    			long tempv=tempstorage.size()/7;
  	    			if(tempstorage.size()%7!=0)
  	    				tempv++;
  	    			for(int i=0;i<tempv;i++)
  	    			{
  	    				ArrayList<JsonUtil.Home.HomeLocation> temp_s=new ArrayList<JsonUtil.Home.HomeLocation>();
  	    				for(int i2=0;i2<7;i2++)
  	    				{
  	    					if(((page_temp-1)*7+i2)>=tempstorage.size())
  	    						break;
  	    					temp_s.add(tempstorage.get((int)((page_temp-1)*7+i2)));
  	    				}
  	    				pages.put(page_temp,temp_s);
  	    				page_temp++;
  	    			}
  	    			long max_page=pages.size();
  	    			if(max_page==0)
  	    				max_page=1;
  	    			if(startpage>max_page)
  	    				startpage=max_page;
  	    			if(plugin.isBasedOnSpigot())
  	    			{
  	    				WSPlayer.class.cast(commandSource).closeInventory();
  	  	    			WSInventory temp=WSInventory.of(9, "HomeList ("+startpage+"/"+max_page+")");
  	  	    			plugin.InventoryList.put(WSPlayer.class.cast(commandSource).getUniqueId().toString(),temp);
  	      	    		plugin.StartList.put(temp, startpage);	
  	  	    			WSItemStack pre=WSItemStack.of(WSBlockTypes.ANVIL);
  	  	    			if(plugin.isBasedOnSpigot())
  	  	    			{
  	  	    				ItemMeta tempmeta=((org.bukkit.inventory.ItemStack)pre.getHandled()).getItemMeta();
  	  	    				tempmeta.setDisplayName("上一页");
  	  	    				((org.bukkit.inventory.ItemStack)pre.getHandled()).setItemMeta(tempmeta);
  	  	    			}
  	  	    			if(WetSponge.isSponge())
  	  	    			{
  	  	    				((org.spongepowered.api.item.inventory.ItemStack)pre.getHandled()).offer(Keys.DISPLAY_NAME,Text.builder("上一页").build());
  	  	    			}
  	  	    			pre=pre.setDisplayName(WSText.builder("上一页").build());
  	  	    			temp.addItem(pre);
  	  	    			if(pages.get(startpage)!=null)
  	  	    			{
  	  	    				for(int i=0;i<pages.get(startpage).size();i++)
  	      	    			{
  	      	    				WSItemStack temp2=WSItemStack.of(WSBlockTypes.RED_BED);
  	          	    			List<WSText> temp3=new ArrayList<WSText>();
  	          	    			temp3.add(WSText.builder(String.valueOf(pages.get(startpage).get(i).Index)).build());
  	          	    			if(plugin.isBasedOnSpigot())
  	          	    			{
  	          	    				List<String> temp4=new ArrayList<String>();
  	              	    			temp4.add(String.valueOf(pages.get(startpage).get(i).Index));
  	              	    			ItemMeta tempmeta=((org.bukkit.inventory.ItemStack)temp2.getHandled()).getItemMeta();
  	              	    			tempmeta.setLore(temp4);
  	              	    			tempmeta.setDisplayName("Home - "+pages.get(startpage).get(i).Index);
  	              	    			((org.bukkit.inventory.ItemStack)temp2.getHandled()).setItemMeta(tempmeta);	
  	          	    			}
  	          	    			if(WetSponge.isSponge())
  	          	    			{
  	          	    				List<Text> templore=new ArrayList<Text>();
  	          	    				templore.add(Text.builder(String.valueOf(pages.get(startpage).get(i).Index)).build());
  	          	    				((org.spongepowered.api.item.inventory.ItemStack)temp2.getHandled()).offer(Keys.ITEM_LORE,templore);
  	          	    				((org.spongepowered.api.item.inventory.ItemStack)temp2.getHandled()).offer(Keys.DISPLAY_NAME,Text.builder("Home - "+pages.get(startpage).get(i).Index).build());
  	          	    			}
  	          	    			temp2=temp2.setLore(temp3);
  	          	    			temp2=temp2.setDisplayName(WSText.builder("Home - "+pages.get(startpage).get(i).Index).build());
  	          	    			temp.addItem(temp2);
  	      	    			}	
  	  	    			}
  	  	    			WSItemStack next=WSItemStack.of(WSBlockTypes.ANVIL);
  	  	    			if(plugin.isBasedOnSpigot())
  	  	    			{
  	  	    				ItemMeta tempmeta=((org.bukkit.inventory.ItemStack)next.getHandled()).getItemMeta();
  	  	    				tempmeta.setDisplayName("下一页");
  	  	    				((org.bukkit.inventory.ItemStack)next.getHandled()).setItemMeta(tempmeta);
  	  	    			}
  	  	    			if(WetSponge.isSponge())
  	  	    			{
  	  	    				((org.spongepowered.api.item.inventory.ItemStack)next.getHandled()).offer(Keys.DISPLAY_NAME,Text.builder("下一页").build());
  	  	    			}
  	  	    			next=next.setDisplayName(WSText.builder("下一页").build());
  	  	    			temp.setItem(next, 8);
  	  	    			if(WetSponge.isSponge())
  	  	    			{
  	  	    				plugin.SpongeCompatible.add(WSPlayer.class.cast(commandSource).getUniqueId().toString());
  	  	    			}
  	  	    			WSPlayer.class.cast(commandSource).openInventory(temp);	
  	    			    }else {
  	    				WSPlayer.class.cast(commandSource).sendMessage("\n\n\nHomeList ("+startpage+"/"+max_page+") :\n-------------\n");
  	    				for(int i=0;i<pages.get(startpage).size();i++)
  	    				{
  	    					WSPlayer.class.cast(commandSource).sendMessage(WSText.builder("Home - "+pages.get(startpage).get(i).Index).hoverAction(WSShowTextAction.of(WSText.builder("Click To Get More Information").build())).clickAction(WSRunCommandAction.of("/homeinfo "+pages.get(startpage).get(i).Index)).build());
  	    				}
  	    				WSPlayer.class.cast(commandSource).sendMessage("-------------");
  	    				WSPlayer.class.cast(commandSource).sendMessage(WSText.builder("上一页").hoverAction(WSShowTextAction.of(WSText.builder("上一页").build())).clickAction(WSRunCommandAction.of("/homelist "+(startpage-1))).build());
  	    				WSPlayer.class.cast(commandSource).sendMessage(WSText.builder("下一页").hoverAction(WSShowTextAction.of(WSText.builder("下一页").build())).clickAction(WSRunCommandAction.of("/homelist "+(startpage+1))).build());
  	    			}
  	    		  //}  
  		  }
		}catch(Throwable e) {}
	}
	
	@Override
	public List<String> sendTab(WSCommandSource commandSource, String command, String[] arguments) {
		return new ArrayList<String>();
	}
}
