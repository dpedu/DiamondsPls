package me.dpedu.diamondpls;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.dthielke.herochat.Channel;
import com.dthielke.herochat.Chatter;
import com.dthielke.herochat.ChatterManager;
import com.dthielke.herochat.Herochat;

public class DiamondPls extends JavaPlugin implements Listener {
	
	public HashMap<String,Boolean> playersEnabled = new HashMap<String,Boolean>();
	public Herochat _herochat;
	
	public void onEnable() {
		Plugin p = this.getServer().getPluginManager().getPlugin( "Herochat" );
		if ( p != null && p instanceof Herochat )
		{
			System.out.println( "[DiamondPls] Found Herochat" );
			this._herochat = ( Herochat ) p;
		}
		
		this.getServer().getPluginManager().registerEvents( this, this );
	}

	@EventHandler( priority = EventPriority.HIGHEST )
	public void onBlockBreakEvent(BlockBreakEvent bbe) {
		if( bbe.getBlock().getType()==Material.DIAMOND_ORE ) {
			if(!playersEnabled.containsKey(bbe.getPlayer().getName())) {
				bbe.setCancelled(true);
				nono(bbe.getPlayer());
			} else if(!playersEnabled.get(bbe.getPlayer().getName())) {
				bbe.setCancelled(true);
				nono(bbe.getPlayer());
			} else {
				playersEnabled.put(bbe.getPlayer().getName(), false);
			}
		}
	}
	@EventHandler( priority = EventPriority.HIGHEST )
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent apce) {
		String message = apce.getMessage();
		
		if ( this._herochat != null )
		{
			try {
				ChatterManager chatterManager = Herochat.getChatterManager();
				Chatter sender = chatterManager.getChatter( apce.getPlayer() );
				Channel channel = sender.getActiveChannel();
				if ( !channel.getName().toLowerCase().equals( "global" ) ) return;
			} catch( Exception ex ) {
				System.out.println( "ERROR" );
				return; // Ignore it
			}
		}
		
		if(apce.getMessage().indexOf("diamonds please")==0) {
			playersEnabled.put(apce.getPlayer().getName(), true);
		}
	}
	public void nono(Player p) {
		p.sendMessage("Please say \"diamonds please\" before mining this!" );
	}
}
