package uk.co.mobsoc.Teams.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.ContainerBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.inventory.InventoryHolder;

import uk.co.mobsoc.Teams.Main;
import uk.co.mobsoc.Teams.Raid.RaidBlockData;
import uk.co.mobsoc.Teams.Raid.RaidCollection;

/**
 * Listen for all events which may be a Raid, Unclaimed traversal, or other event where it should eventually revert to original state
 * @author triggerhapp
 *
 */
public class RaidListener implements Listener{
	private Main main;

	public RaidListener(Main main){
		this.main = main;
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
	public void onBlockBreak(BlockBreakEvent event){
		if(event.isCancelled()){ return; }
		RaidBlockData rbd = new RaidBlockData(event.getBlock());
		for(RaidCollection raid : main.getAllRaids()){
			if(raid.actionIsRaid(event.getPlayer(), rbd)){
				raid.addBlock(rbd);
			}
		}
	}
	
	public void onPlacePlace(BlockPlaceEvent event){
		if(event.isCancelled()){ return; }
		RaidBlockData rbd = new RaidBlockData(event.getBlock());
		for(RaidCollection raid : main.getAllRaids()){
			if(raid.actionIsRaid(event.getPlayer(), rbd)){
				raid.addBlock(rbd);
			}
		}
	}

	public void onExplosion(EntityExplodeEvent event){
		if(event.isCancelled()){ return; }
		for(Block b : event.blockList()){
			RaidBlockData rbd = new RaidBlockData(b);
			for(RaidCollection raid : main.getAllRaids()){
				if(raid.actionIsRaid(rbd)){
					raid.addBlock(rbd);
				}
			}
		}
	}

	public void onLiquidFlow(BlockFromToEvent event){
		if(event.isCancelled()){ return ; }
		RaidBlockData rbd = new RaidBlockData(event.getToBlock());
		for(RaidCollection raid : main.getAllRaids()){
			if(raid.actionIsRaid(rbd)){
				raid.addBlock(rbd);
			}
		}
	}
	
	public void onBucketFill(PlayerBucketFillEvent event){
		if(event.isCancelled()){ return; }
		RaidBlockData rbd = new RaidBlockData(event.getBlockClicked().getRelative(event.getBlockFace()));
		for(RaidCollection raid : main.getAllRaids()){
			if(raid.actionIsRaid(rbd)){
				raid.addBlock(rbd);
			}
		}
	}
	
	public void onBucketEmpty(PlayerBucketEmptyEvent event){
		if(event.isCancelled()){ return; }
		RaidBlockData rbd = new RaidBlockData(event.getBlockClicked().getRelative(event.getBlockFace()));
		for(RaidCollection raid : main.getAllRaids()){
			if(raid.actionIsRaid(rbd)){
				raid.addBlock(rbd);
			}
		}
	}
	
	public void onInteractBlock(PlayerInteractEvent event){
		if(event.isCancelled()){ return; }
		if(!event.hasBlock()){ return; }
		RaidBlockData rbd = new RaidBlockData(event.getClickedBlock());
		for(RaidCollection raid : main.getAllRaids()){
			if(raid.actionIsRaid(event.getPlayer(), rbd)){
				raid.addBlock(rbd);
			}
		}
	}
	
	public void onEntityBlockFormEvent(EntityBlockFormEvent event){
		if(event.isCancelled()){ return; }
		RaidBlockData rbd = new RaidBlockData(event.getBlock());
		for(RaidCollection raid : main.getAllRaids()){
			if(raid.actionIsRaid(rbd)){
				raid.addBlock(rbd);
			}
		}
	}
}
