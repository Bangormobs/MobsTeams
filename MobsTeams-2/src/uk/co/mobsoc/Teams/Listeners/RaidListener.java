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

public class RaidListener implements Listener{
	public RaidListener(Main main){
		Bukkit.getPluginManager().registerEvents(this, main);
	}
	
	/**
	 * Adds broken blocks to the list of blocks that return to initial state
	 * @param event
	 */
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event){
		if(event.isCancelled()){ return; }
		AbstractGame game = MobsGames.getGame();
		if(game==null){ return; }
		if(game.isParticipant(event.getPlayer())){
			BlockData bd = new BlockData(event.getBlock());
			MobsGames.getGame().addRevert(bd);
		}
	}
	
	/**
	 * Adds Placed blocks to the list of blocks that return to initial state
	 * @param event
	 */
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlacePlace(BlockPlaceEvent event){
		if(event.isCancelled()){ return; }
		AbstractGame game = MobsGames.getGame();
		if(game==null){ return; }
		if(game.isParticipant(event.getPlayer())){
			BlockData bd = new BlockData(event.getBlock());
			bd.data = event.getBlockReplacedState().getRawData();
			bd.id = event.getBlockReplacedState().getTypeId();
			MobsGames.getGame().addRevert(bd);
		}
	}
	


	/**
	 * Add Exploded blocks to the list of blocks that return to initial state.
	 * This might be a bit buggy, since it does not check (Because it is not logically possible) if the explosion is related to the game or not.
	 * @param event
	 */
	@EventHandler(priority=EventPriority.MONITOR)
	public void onExplosion(EntityExplodeEvent event){
		AbstractGame game = MobsGames.getGame();
		if(game!=null){
			for(Block block : event.blockList()){
				game.addRevert(new BlockData(block));
			}
		}
	}

	/**
	 * Add Liquid that flows into the list of blocks that return to initial state.
	 * This might be a bit buggy, since it does not check (due to excessive CPU usage) if the liquid is related to the game or not.
	 * @param event
	 */
	@EventHandler(priority=EventPriority.MONITOR)
	public void onLiquidFlow(BlockFromToEvent event){
		AbstractGame game = MobsGames.getGame();
		if(game!=null){
			game.addRevert(new BlockData(event.getToBlock()));
		}
	}
	
	/**
	 * Add Liquid that is taken to the list of blocks to return to initial state
	 * @param event
	 */
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBucketFill(PlayerBucketFillEvent event){
		if(event.isCancelled()){ return; }
		AbstractGame game = MobsGames.getGame();
		if(game==null){ return; }
		if(game.isParticipant(event.getPlayer())){
			BlockData bd = new BlockData(event.getBlockClicked().getRelative(event.getBlockFace()));
			game.addRevert(bd);
		}
	}
	
	/**
	 * Add Liquid that is placed to the list of blocks to return to initial state
	 * @param event
	 */
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBucketEmpty(PlayerBucketEmptyEvent event){
		if(event.isCancelled()){ return; }
		AbstractGame game = MobsGames.getGame();
		if(game==null){ return; }
		if(game.isParticipant(event.getPlayer())){
			BlockData bd = new BlockData(event.getBlockClicked().getRelative(event.getBlockFace()));
			game.addRevert(bd);
		}
	}
	
	/**
	 * Add Interacted items to the list of blocks to return to initial state
	 * @param event
	 */
	@EventHandler(priority=EventPriority.MONITOR)
	public void onInteractBlock(PlayerInteractEvent event){
		AbstractGame game = MobsGames.getGame();
		if(game==null){ return; }
		if(event.isCancelled()){ return; }
		if(event.hasBlock()){
			BlockData bd = new BlockData(event.getClickedBlock());
			game.addRevert(bd);
			//bd = new BlockData(event.getClickedBlock().getRelative(event.getBlockFace()));
			//game.addRevert(bd);

		}
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onInventoryOpen(InventoryOpenEvent event){
		if(event.isCancelled()){ return; }
		AbstractGame game = MobsGames.getGame();
		if(game==null){ return; }
		if(game.isParticipant((Player) event.getPlayer())){
			InventoryHolder iH = event.getInventory().getHolder();
			// To Hell with depreciating a perfectly good class.
			if(iH instanceof ContainerBlock){
				game.addRevert(iH);
			}
			
		}
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockPhysicsEvent(BlockPhysicsEvent event){
		if(event.isCancelled()){ return; }
		AbstractGame game = MobsGames.getGame();
		if(game==null){ return; }
		game.addRevert(new BlockData(event.getBlock()));
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onEntityBlockFormEvent(EntityBlockFormEvent event){
		if(event.isCancelled()){ return; }
		AbstractGame game = MobsGames.getGame();
		if(game==null){ return; }
		BlockData bd = new BlockData(event.getBlock());
		game.addRevert(bd);	
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockFormEvent(BlockFormEvent event){
		if(event.isCancelled()){ return; }
		AbstractGame game = MobsGames.getGame();
		if(game==null){ return; }
		BlockData bd = new BlockData(event.getBlock());
		game.addRevert(bd);		
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockSpreadEvent(BlockSpreadEvent event){
		if(event.isCancelled()){ return; }
		AbstractGame game = MobsGames.getGame();
		if(game==null){ return; }
		BlockData bd = new BlockData(event.getBlock());
		game.addRevert(bd);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockFadeEvent(BlockFadeEvent event){
		if(event.isCancelled()){ return; }
		AbstractGame game = MobsGames.getGame();
		if(game==null){ return; }
		BlockData bd = new BlockData(event.getBlock());
		game.addRevert(bd);		
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onLeavesDecayEvent(LeavesDecayEvent event){
		if(event.isCancelled()){ return; }
		AbstractGame game = MobsGames.getGame();
		if(game==null){ return; }
		BlockData bd = new BlockData(event.getBlock());
		game.addRevert(bd);		
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent event){
		if(event.isCancelled()){ return; }
		AbstractGame game = MobsGames.getGame();
		if(game==null){ return; }
		BlockData bd = new BlockData(event.getBlock());
		game.addRevert(bd);
		//System.out.println("Block changed from "+event.getBlock().getTypeId()+" to "+event.getTo().getId()+" by "+event.getEntity());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onStructureGrowEvent(StructureGrowEvent event){
		if(event.isCancelled()){ return; }
		AbstractGame game = MobsGames.getGame();
		if(game==null){ return; }
		for(BlockState bs : event.getBlocks()){
			BlockData bd = new BlockData(bs.getBlock());
			game.addRevert(bd);
		}
	}

}
