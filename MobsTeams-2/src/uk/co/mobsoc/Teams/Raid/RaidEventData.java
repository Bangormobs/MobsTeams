package uk.co.mobsoc.Teams.Raid;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import uk.co.mobsoc.Teams.Main;
import uk.co.mobsoc.Teams.Data.Government;

public class RaidEventData implements Runnable,RaidCollection{
	// This data is not made permanent.
	long timeElapsed=0;
	Government attacker, defender;
	int timerID=-1;
	ArrayList<RaidBlockData> blockList = new ArrayList<RaidBlockData>();
	
	public RaidEventData(Government attacker, Government defender){
		this.attacker = attacker; this.defender = defender;
	}
	
	public void start(){
		// TODO Check Repeating options are correct
		timeElapsed=0;
		timerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.main, this, 20, 0);
	}
	
	public void stop(){
		Bukkit.getScheduler().cancelTask(timerID);
	}

	@Override
	public void run() {
		timeElapsed ++;
	}
	// 1 Min = 60
	// 5 mins = 300
	// 30 mins = 1800
	enum State { INVALID, WARMUP, MAIN, COOLDOWN };
	public State getState(){
		if(timeElapsed==-1){
			return State.INVALID;
		}else if(timeElapsed < 300){
			return State.WARMUP;
		}else if(timeElapsed < 1800){
			return State.MAIN;
		}else if(timeElapsed < 2100){
			return State.COOLDOWN;
		}
		return State.INVALID;
	}
	
	/***
	 * For non-player block changes
	 * @param block
	 * @return
	 */
	@Override
	public boolean actionIsRaid(RaidBlockData block){
		if(defender.ownsBlock(block.getCurrectBlockAt())){
			return true;
		}
		return false;
	}
	
	/***
	 * For player actions
	 */
	@Override
	public boolean actionIsRaid(Player player, RaidBlockData block){
		if(attacker.hasMember(player)){
			if(defender.ownsBlock(block.getCurrectBlockAt())){
				return true;
			}
		}
		return false;
	}

	@Override
	public ArrayList<RaidBlockData> getAllAlteredBlocks() {
		return blockList;
	}

	@Override
	public void addBlock(RaidBlockData block) {
		for(RaidBlockData rbd : blockList){
			if(rbd.isEqualLocation(block)){
				return;
			}
		}
		blockList.add(block);
	}
}
