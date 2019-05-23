package corepluginguiWS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import com.degoos.wetsponge.WetSponge;
import com.degoos.wetsponge.entity.living.player.WSPlayer;
import com.degoos.wetsponge.enums.EnumEventPriority;
import com.degoos.wetsponge.enums.EnumServerType;
import com.degoos.wetsponge.event.WSListener;
import com.degoos.wetsponge.event.inventory.WSInventoryClickEvent;
import com.degoos.wetsponge.event.inventory.WSInventoryCloseEvent;
import com.degoos.wetsponge.inventory.WSInventory;
import com.degoos.wetsponge.item.WSItemStack;
import com.degoos.wetsponge.plugin.WSPlugin;
import com.degoos.wetsponge.sound.WSSound;
import com.degoos.wetsponge.text.WSText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main extends WSPlugin{
	  public JsonUtil config=null;
	  public String ConfigFileLocation=".\\WetSpongePlugins\\CorePluginGUIWS\\config.json";
	  public ArrayList<String> ChildCommandList=new ArrayList<String>();
	  public TreeMap<WSPlayer,TreeMap<WSPlayer,Long>> TeleportList=new TreeMap<WSPlayer,TreeMap<WSPlayer,Long>>();
	  public HashMap<String,WSInventory> InventoryList=new HashMap<String,WSInventory>();
	  public HashMap<WSInventory,Long> StartList=new HashMap<WSInventory,Long>();
	  public ArrayList<String> CancelList=new ArrayList<String>();
	  public ArrayList<String> SpongeCompatible=new ArrayList<String>();
	  public HashMap<String,ArrayList<WSItemStack>> SpongeTempStorage=new HashMap<String,ArrayList<WSItemStack>>();
      public void onEnable()
      { 
    	  try {
    	      ChildCommandList.add("reload");
    	      WetSponge.getServer().getConsole().sendMessage(WSText.builder("CorePluginGUI_WetSponge Loaded").build());
    		  }catch(Throwable e) {e.printStackTrace();}
    	  try {
          new File(new File(ConfigFileLocation).getParent()).mkdirs();
    	  new File(ConfigFileLocation).createNewFile();
    	  config=parseJson(new String(readFile(new File(ConfigFileLocation)),"GBK"));
    	  if(config==null)
    		  config=new JsonUtil();
    	  WetSponge.getCommandManager().addCommand(this, new HatCommand());
    	  WetSponge.getCommandManager().addCommand(this, new SetHomeCommand(this));
    	  WetSponge.getCommandManager().addCommand(this, new HomeCommand(this));
    	  WetSponge.getCommandManager().addCommand(this, new TpaCommand(this));
    	  WetSponge.getCommandManager().addCommand(this,new HomeListCommand(this));
    	  WetSponge.getCommandManager().addCommand(this,new HomeInfoCommand(this));
    	  WetSponge.getCommandManager().addCommand(this,new DelHomeCommand(this));
    	  WetSponge.getEventManager().registerListener(this, this);
    	  WetSponge.getServer().getConsole().sendMessage(WSText.builder("CorePluginGUI_WetSponge Enabled").build());
    	  }catch(Throwable e) {e.printStackTrace();}
      }
      public void onDisable()
      {
    	  try {
    	  SaveConfig();
    	  WetSponge.getServer().getConsole().sendMessage(WSText.builder("CorePluginGUI_WetSponge Disabled").build());
    	  }catch(Throwable e) {e.printStackTrace();}
      }
	@SuppressWarnings("deprecation")
	@WSListener(priority=EnumEventPriority.VERY_HIGH,ignoreCancelled=true)
      public void onInventoryClick(WSInventoryClickEvent e)
      {
    	  try {
    	  if(CancelList.contains(e.getPlayer().getUniqueId().toString()))
    	  {
    		  CancelList.remove(e.getPlayer().getUniqueId().toString());
    		  e.setCancelled(true);
    	  }
    	  if(InventoryList.containsValue(e.getClickedInventory()) || (WetSponge.isSponge() && SpongeCompatible.contains(e.getPlayer().getUniqueId().toString())))
    	  {
    		  e.setCancelled(true);
    		  if(WetSponge.isSponge())
        	  {
    			e.getPlayer().closeInventory();
        		return;  
        	  }
    		  if(e.getTransaction().getOldData().get().getDisplayName().contains("Home - "))
    		  {
    			  WSPlayer.class.cast(e.getPlayer()).playSound(WSSound.ENTITY_PLAYER_LEVELUP,100,0);
    			  CancelList.add(e.getPlayer().getUniqueId().toString());
    			  e.getPlayer().closeInventory();
    			  if(isBasedOnSpigot())
    			  {
    				  e.getPlayer().performCommand("wetsponge:wetspongecmd home "+e.getTransaction().getOldData().get().getLore().get(0).getText());  
    			  }else {
    				  e.getPlayer().performCommand("home "+e.getTransaction().getOldData().get().getLore().get(0).getText());
    			  }
    		  }else if(e.getTransaction().getOldData().get().getDisplayName().getText().equals("下一页"))
    		  {
    			  WSPlayer.class.cast(e.getPlayer()).playSound(WSSound.BLOCK_ANVIL_LAND,100,0);
    			  StartList.replace(e.getClickedInventory(), StartList.get(e.getClickedInventory())+1);
    			  if(isBasedOnSpigot())
    			  {
    				  e.getPlayer().performCommand("wetsponge:wetspongecmd homelist "+StartList.get(e.getClickedInventory()));  
    			  }else {
    				  e.getPlayer().performCommand("homelist "+StartList.get(e.getClickedInventory()));
    			  }
    		  }else if(e.getTransaction().getOldData().get().getDisplayName().getText().equals("上一页"))
    		  {
    			  if(StartList.get(e.getClickedInventory())!=1)
    			  {
    				  WSPlayer.class.cast(e.getPlayer()).playSound(WSSound.BLOCK_ANVIL_LAND,100,0);
    				  StartList.replace(e.getClickedInventory(), StartList.get(e.getClickedInventory())-1);
        			  if(isBasedOnSpigot())
        			  {
        				  e.getPlayer().performCommand("wetsponge:wetspongecmd homelist "+StartList.get(e.getClickedInventory()));  
        			  }else {
        				  e.getPlayer().performCommand("homelist "+StartList.get(e.getClickedInventory()));
        			  }
    			  }
    		  }
    	  }
    	  }catch(Throwable e2) {}
      }
      @WSListener(priority=EnumEventPriority.VERY_HIGH,ignoreCancelled=true)
      public void onInventoryClose(WSInventoryCloseEvent e)
      {
    	  InventoryList.remove(e.getPlayer().getUniqueId().toString());
    	  SpongeCompatible.remove(e.getPlayer().getUniqueId().toString());
    	  StartList.remove(e.getInventory());
      }
    /*@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
      {
    	  try {
    	  if(command.getName().equalsIgnoreCase("hat"))
    	  {
    		  if(sender instanceof Player)
    			  if(Player.class.cast(sender).getItemInHand()!=null)
    			  {
    				  ItemStack temp=Player.class.cast(sender).getItemInHand();
    				  Player.class.cast(sender).setItemInHand(Player.class.cast(sender).getInventory().getHelmet());
    				  Player.class.cast(sender).getInventory().setHelmet(temp);  
    			  }
    	  return true;
    	  }else if(command.getName().equalsIgnoreCase("sethome"))
    	  {
    		  if(sender instanceof Player)
    		  {
    			  long index=0;
    			  if(args.length>0)
    				  index=Long.valueOf(args[0]);
    			  long PlayerIndex=getHomeIndex(Player.class.cast(sender));
    			  if(PlayerIndex!=-1)
    			  {
    				  JsonUtil.Home temphome=config.HomeList.get((int)PlayerIndex);
    				  if(isIndexAvailable(PlayerIndex, index))
    				  {
    					  ArrayList<Double> templ=new ArrayList<Double>();
        				  templ.add(Player.class.cast(sender).getLocation().getX());
        				  templ.add(Player.class.cast(sender).getLocation().getY());
        				  templ.add(Player.class.cast(sender).getLocation().getZ());
        				  templ.add((double) Player.class.cast(sender).getLocation().getYaw());
        				  templ.add((double) Player.class.cast(sender).getLocation().getPitch());
    					  JsonUtil.Home.HomeLocation temph=config.new Home(null,null).new HomeLocation(index,Player.class.cast(sender).getWorld().getName(),templ);
    					  long realindex=getHomeIndexIndex(Player.class.cast(sender),index);
    					  temphome.LocationList.remove((int)realindex);
    					  temphome.LocationList.add((int)realindex, temph);
    					  config.HomeList.remove((int)PlayerIndex);
    					  config.HomeList.add((int)PlayerIndex,temphome);
    					  SaveConfig();
    					  sender.sendMessage("已重设家: Index="+index+" RealIndex="+realindex+" !");
    				  }else {
        				  ArrayList<Double> templ=new ArrayList<Double>();
        				  templ.add(Player.class.cast(sender).getLocation().getX());
        				  templ.add(Player.class.cast(sender).getLocation().getY());
        				  templ.add(Player.class.cast(sender).getLocation().getZ());
        				  templ.add((double) Player.class.cast(sender).getLocation().getYaw());
        				  templ.add((double) Player.class.cast(sender).getLocation().getPitch());
        				  JsonUtil.Home.HomeLocation temph=config.new Home(null,null).new HomeLocation(index,Player.class.cast(sender).getWorld().getName(),templ);
        				  temphome.LocationList.add(temph);
        				  config.HomeList.remove((int)PlayerIndex);
    					  config.HomeList.add((int)PlayerIndex,temphome);
    					  SaveConfig();
    					  sender.sendMessage("已设置家: Index="+index+" !");
    				  }
    			  }else {
    				  ArrayList<JsonUtil.Home.HomeLocation> tempa=new ArrayList<JsonUtil.Home.HomeLocation>();
    				  ArrayList<Double> templ=new ArrayList<Double>();
    				  templ.add(Player.class.cast(sender).getLocation().getX());
    				  templ.add(Player.class.cast(sender).getLocation().getY());
    				  templ.add(Player.class.cast(sender).getLocation().getZ());
    				  templ.add((double) Player.class.cast(sender).getLocation().getYaw());
    				  templ.add((double) Player.class.cast(sender).getLocation().getPitch());
    				  tempa.add(config.new Home(null,null).new HomeLocation(index,Player.class.cast(sender).getWorld().getName(),templ));
    			      config.HomeList.add(config.new Home(Player.class.cast(sender).getUniqueId().toString(),tempa));
    			      SaveConfig();
    			  }
    		  }
    	  return true;
    	  }else if(command.getName().equalsIgnoreCase("home"))
    	  {
    		  if(sender instanceof Player)
    		  {
    			  long index=0;
    			  if(args.length>0)
    				  index=Long.valueOf(args[0]);
    			  long PlayerIndex=getHomeIndex(Player.class.cast(sender));
    			  if(PlayerIndex!=-1)
    			  {
    				  JsonUtil.Home temphome=config.HomeList.get((int)PlayerIndex);
    				  if(isIndexAvailable(PlayerIndex, index))
    				  {
    					  JsonUtil.Home.HomeLocation location=temphome.LocationList.get((int)getHomeIndexIndex(Player.class.cast(sender),index));
    					  Player.class.cast(sender).teleport(new Location(Bukkit.getWorld(location.World),location.Location.get(0),location.Location.get(1),location.Location.get(2),Float.valueOf(String.valueOf(location.Location.get(3))),Float.valueOf(String.valueOf(location.Location.get(4)))),TeleportCause.PLUGIN);
    					  sender.sendMessage("已传送至家: Index="+index+" RealIndex="+(int)getHomeIndexIndex(Player.class.cast(sender),index)+" !");
    				  }
    			  }
    		  }
    		  return true;
    	  }else if(command.getName().equalsIgnoreCase("CorePlugin"))
    	  {
    		  if(args.length>0)
    		  {
    			  if(args[0].equals("reload"))
    			  {
    				  ReloadConfig();  
    			  }else {
    				  sender.sendMessage("ChildCommandList:");
    				  for(int i=0;i<ChildCommandList.size();i++)
    					  sender.sendMessage(ChildCommandList.get(i));
    			  }
    			  return true;
    		  }
    	  }else if(command.getName().equalsIgnoreCase("tpa"))
    	  {
    		  if(sender instanceof Player)
    		  {
    			  if(args.length>0)
    			  {
    				  if(Bukkit.getPlayer(args[0])!=null)
    				  {
    					  TreeMap<Player,Long> temptree=null;
    					  if(TeleportList.containsKey(Player.class.cast(sender)) && TeleportList.get(Player.class.cast(sender)) != null)
    					  {
    						  temptree=TeleportList.get(Player.class.cast(sender));
    					  }else {
    						  temptree=new TreeMap<Player,Long>();  
    					  }
    					  temptree.put(Bukkit.getPlayer(args[0]), System.nanoTime()+10000000000L);
    					  if(TeleportList.containsKey(Player.class.cast(sender)))
    					  {
    						  TeleportList.remove(Player.class.cast(sender));
    						  TeleportList.put(Player.class.cast(sender),temptree);
    					  }else {
    						  TeleportList.put(Player.class.cast(sender),temptree);  
    					  }
    					  Bukkit.getPlayer(args[0]).sendMessage("Player "+Player.class.cast(sender).getName()+" is trying to teleport you. Please type /tpaccept or /tpadeny in 10 seconds to response it.");
    					  sender.sendMessage("Success to sending teleport response.");
    				  }
    			  }else {
    				  return false;
    			  }
    		  }
    		  return true;
    	  }else if(command.getName().equalsIgnoreCase("homelist"))
    	  {
    		  if(sender instanceof Player)
    		  {
    			  /*if(args.length>0)
    				Player.class.cast(sender).closeInventory(Reason.PLUGIN);*/
      
      /*
      
              //if(!InventoryList.containsKey(Player.class.cast(sender).getUniqueId().toString())){
    				    long startpage=1;
    				    if(args.length>0)
    				      startpage=Long.parseLong(args[0]);
    				    if(startpage<1)
    				    	return true;
    				    ArrayList<Long> homelist=new ArrayList<Long>();
    				    		try {
    				    homelist=getAvailableIndexes(Player.class.cast(sender));
    				    		}catch(Throwable e2) {}
    	    			ArrayList<JsonUtil.Home.HomeLocation> tempstorage=new ArrayList<JsonUtil.Home.HomeLocation>();
    	    			for(int i=0;i<homelist.size();i++)
    	    			{
    	    		    tempstorage.add(config.HomeList.get((int)getHomeIndex(Player.class.cast(sender))).LocationList.get(i));
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
    	    			Player.class.cast(sender).closeInventory(Reason.PLUGIN);
    	    			Inventory temp=Bukkit.createInventory((InventoryHolder) sender, 9, "HomeList ("+startpage+"/"+max_page+")");
    	    			InventoryList.put(Player.class.cast(sender).getUniqueId().toString(),temp);
        	    		StartList.put(temp, startpage);	
    	    			ItemStack pre=new ItemStack(Material.ANVIL);
    	    			ItemMeta pred=pre.getItemMeta();
    	    			pred.setDisplayName("上一页");
    	    			pre.setItemMeta(pred);
    	    			temp.addItem(pre);
    	    			if(pages.get(startpage)!=null)
    	    			{
    	    				for(int i=0;i<pages.get(startpage).size();i++)
        	    			{
        	    				ItemStack temp2=new ItemStack(Material.BED,1,(short) 14);
            	    			ItemMeta temp233=temp2.getItemMeta();
            	    			List<String> temp3=new ArrayList<String>();
            	    			temp3.add(String.valueOf(pages.get(startpage).get(i).Index));
            	    			temp233.setLore(temp3);
            	    		    temp233.setDisplayName("Home - "+pages.get(startpage).get(i).Index);
            	    		    temp2.setItemMeta(temp233);
            	    			temp.addItem(temp2);
        	    			}	
    	    			}
    	    			ItemStack next=new ItemStack(Material.ANVIL);
    	    			ItemMeta nextd=next.getItemMeta();
    	    			nextd.setDisplayName("下一页");
    	    			next.setItemMeta(nextd);
    	    			temp.setItem(8, next);
    	    			Player.class.cast(sender).openInventory(temp);
    	    		  //}  
    		  }
    		  return true;
    	  }else if(command.getName().equalsIgnoreCase("homeinfo"))
    	  {
    		  if(sender instanceof Player)
    		  {
    			  long index=0;
    			  if(args.length>0)
    				  index=Long.valueOf(args[0]);
    			  long HomeIndex=getHomeIndex(Player.class.cast(sender));
    			  if(HomeIndex!=-1)
    			  {
    				  if(isIndexAvailable(HomeIndex,index))
    				  {
    					 long realindex=getHomeIndexIndex(Player.class.cast(sender),index);
    					 JsonUtil.Home.HomeLocation temp=config.HomeList.get((int)HomeIndex).LocationList.get((int)realindex);
    					 sender.sendMessage("\n\n\n-------------\n"+"HomeIndex:"+temp.Index+"\n"+"RealIndex:"+realindex+"\n"+"World:"+temp.World+"\n"+"X:"+temp.Location.get(0)+"\n"+"Y:"+temp.Location.get(1)+"\n"+"Z:"+temp.Location.get(2)+"\n"+"Yaw:"+temp.Location.get(3)+"\n"+"Pitch:"+temp.Location.get(4)+"\n-------------\n");
    				  }
    			  }
    		  }
    		  return true;
    	  }else if(command.getName().equalsIgnoreCase("delhome"))
    	  {
    		  if(sender instanceof Player)
    		  {
    			  long index=0;
    			  if(args.length>0)
    				  index=Long.valueOf(args[0]);
    			  long HomeIndex=getHomeIndex(Player.class.cast(sender));
    			  if(HomeIndex!=-1)
    			  {
    				  if(isIndexAvailable(HomeIndex,index))
    				  {
    					  long realindex=getHomeIndexIndex(Player.class.cast(sender),index);
    					  config.HomeList.get((int)HomeIndex).LocationList.remove((int)realindex);
    					  SaveConfig();
    					  sender.sendMessage("已删除家: Index="+index+" RealIndex="+realindex+" !");
    				  }
    			  }
    		  }
    		  return true;
    	  }
    	  return false;
    	  }catch(Throwable e) {e.printStackTrace();return true;}
      }*/
      public ArrayList<Long> getAvailableIndexes(WSPlayer player)
      {
    	  ArrayList<Long> result=new ArrayList<Long>();
    	  for(int i=0;i<config.HomeList.size();i++)
    	  {
    		  if(config.HomeList.get(i).Player!=null && config.HomeList.get(i).Player.equals(player.getUniqueId().toString()))
    		  {
    			  for(int i2=0;i2<config.HomeList.get(i).LocationList.size();i2++)
    			  {
    				  result.add(config.HomeList.get(i).LocationList.get(i2).Index);
    			  }
    			  break;
    		  }
    	  }
    	  return result;
      }
      public static byte[] readFile(File file) throws Throwable
      {
    	 FileInputStream input=new FileInputStream(file);
    	 byte[] t_ret=new byte[input.available()];
    	 input.read(t_ret, 0, input.available());
    	 input.close();
    	 return t_ret;
      }
      public static boolean writeFile(File file,byte[] content) throws Throwable
      {
    	  FileOutputStream output=new FileOutputStream(file);
    	  output.write(content, 0, content.length);
    	  output.flush();
    	  output.close();
    	  return true;
      }
      public static JsonUtil parseJson(String json) throws Throwable
      {
    	  Gson parse=new GsonBuilder().setLenient().setPrettyPrinting().enableComplexMapKeySerialization().create();
    	  return parse.fromJson(json, JsonUtil.class);
      }
      public static String toJsonString(JsonUtil json) throws Throwable
      {
    	  Gson parse=new GsonBuilder().setLenient().setPrettyPrinting().enableComplexMapKeySerialization().create();
    	  return parse.toJson(json);
      }
      public boolean SaveConfig() throws Throwable
      {
    	  return writeFile(new File(ConfigFileLocation), toJsonString(config).getBytes());
      }
      public void ReloadConfig() throws Throwable
      {
    	  this.config=parseJson(new String(readFile(new File(ConfigFileLocation)),"GBK"));
      }
      public long getHomeIndex(WSPlayer player) throws Throwable
      {
    	  try {
    	  for(int i=0;i<config.HomeList.size();i++)
    	  {
    		  if(config.HomeList.get(i).Player.equals(player.getUniqueId().toString()))
    			  return i;
    	  }
    	  return -1;
    	  }catch(NullPointerException e) {return -1;}
      }
      public boolean isIndexAvailable(long PlayerIndex,long index)
      {
    	  for(int i=0;i<config.HomeList.get((int)PlayerIndex).LocationList.size();i++)
    		  if(config.HomeList.get((int)PlayerIndex).LocationList.get(i).Index==index)
    			  return true;
    	  return false;
      }
      public long getHomeIndexIndex(WSPlayer player,long Index) throws Throwable
      {
    	  try {
    	  for(int i=0;i<config.HomeList.get((int)getHomeIndex(player)).LocationList.size();i++)
    	  {
    		  if(config.HomeList.get((int)getHomeIndex(player)).LocationList.get(i).Index==Index)
    			  return i;
    	  }
    	  return -1;
    	  }catch(NullPointerException e) {return -1;}
      }
      public boolean containsPlayer(WSPlayer player)
      {
    	  return false;
      }
      public boolean isBasedOnSpigot()
      {
    	  return WetSponge.getServerType()==EnumServerType.SPIGOT || WetSponge.getServerType()==EnumServerType.PAPER_SPIGOT;
      }
}
