package com.massivecraft.massivetickets.engine;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.mixin.MixinActual;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivetickets.MassiveTickets;
import com.massivecraft.massivetickets.Perm;
import com.massivecraft.massivetickets.entity.MConf;
import com.massivecraft.massivetickets.entity.MPlayer;
import com.massivecraft.massivetickets.entity.MPlayerColl;
import com.massivecraft.massivetickets.predicate.PredicateIsModerator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;

public class EngineJoin extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineJoin i = new EngineJoin();
	public static EngineJoin get() { return i; }
	
	// -------------------------------------------- //
	// VERIFY WORKING ON JOIN
	// -------------------------------------------- //
	// Why do we do this? In order to keep the database clean!
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void verifyWorkingOnJoin(PlayerJoinEvent event)
	{
		// If a player is joining this server ...
		final Player player = event.getPlayer();
		if (MUtil.isntPlayer(player)) return;
		MPlayer mplayer = MPlayer.get(player);
		
		// ... and that player has "working" toggled on ...
		if (!mplayer.isWorking()) return;
		
		// ... and the player lacks the permission to work ...
		if (Perm.WORKING.has(player)) return;
		
		// ... then toggle working off.
		mplayer.setWorking(false);
	}
	
	// -------------------------------------------- //
	// BUMP ON JOIN
	// -------------------------------------------- //
	
	public static void bumpOnJoin(PlayerJoinEvent event, EventPriority priority)
	{
		// If a player is joining the server ...
		final Player player = event.getPlayer();
		
		// ... and this player is a moderator ...
		if (!PredicateIsModerator.get().apply(player)) return;
		
		// If the bump on join is activated ...
		if (!MConf.get().isBumpOnJoinActive()) return;
		
		// ... and this is the right priority ...
		if (MConf.get().getBumpOnJoinPriority() != priority) return;
		
		// ... and this is an actual join ...
		if (!MixinActual.get().isActualJoin(event)) return;
		
		// ... and if there are any tickets ...
		if (MPlayerColl.get().getAllTickets().size() == 0) return;
		
		// ... then bump the player.
		MassiveTickets.alertOneMessage(player, MassiveTickets.createBumpMessage());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void bumpOnJoinLowest(PlayerJoinEvent event)
	{
		bumpOnJoin(event, EventPriority.LOWEST);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void bumpOnJoinLow(PlayerJoinEvent event)
	{
		bumpOnJoin(event, EventPriority.LOW);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void bumpOnJoinNormal(PlayerJoinEvent event)
	{
		bumpOnJoin(event, EventPriority.NORMAL);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void bumpOnJoinHigh(PlayerJoinEvent event)
	{
		bumpOnJoin(event, EventPriority.HIGH);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void bumpOnJoinHighest(PlayerJoinEvent event)
	{
		bumpOnJoin(event, EventPriority.HIGHEST);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void bumpOnJoinMonitor(PlayerJoinEvent event)
	{
		bumpOnJoin(event, EventPriority.MONITOR);
	}
	
}
