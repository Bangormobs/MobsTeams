package uk.co.mobsoc.Teams.Raid;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import uk.co.mobsoc.Teams.Main;

public class UnclaimedRaid implements RaidCollection, Runnable{
	
	private Main plugin;
	ArrayList<RaidBlockData> blockList = new ArrayList<RaidBlockData>();

	public UnclaimedRaid(Main main){
		this.plugin = main;
		setTimer();
	}

	@Override
	public void run() {
		setTimer();
		for(RaidBlockData rbd : blockList){
			rbd.timer++;
			if(rbd.timer > 60){
				rbd.replace();
			}
		}
		
	}

	private void setTimer() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, 20);
	}

	@Override
	public boolean actionIsRaid(Player player, RaidBlockData block) {
		// TODO Check if area is claimed
		return true;
	}

	@Override
	public ArrayList<RaidBlockData> getAllAlteredBlocks() {
		return blockList;
	}

	@Override
	public void addBlock(RaidBlockData block) {
		blockList.add(block);
	}

	@Override
	public boolean actionIsRaid(RaidBlockData block) {
		// TODO Check if area is claimed
		return true;
	}

}
