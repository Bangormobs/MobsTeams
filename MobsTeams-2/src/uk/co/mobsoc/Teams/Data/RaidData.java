package uk.co.mobsoc.Teams.Data;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import uk.co.mobsoc.Teams.Main;

public class RaidData implements Runnable{
	// This data is not made permanent.
	long timeElapsed=0;
	Government attacker, defender;
	int timerID=-1;
	
	public RaidData(Government attacker, Government defender){
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
	
	public boolean actionIsRaid(Player player, Block block){
		if(attacker.hasMember(player)){
			if(defender.ownsBlock(block)){
				return true;
			}
		}
		return false;
	}
}
