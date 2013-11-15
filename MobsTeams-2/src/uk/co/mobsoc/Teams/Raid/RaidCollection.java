package uk.co.mobsoc.Teams.Raid;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;


public interface RaidCollection {
	public boolean actionIsRaid(Player player, RaidBlockData block);
	public boolean actionIsRaid(RaidBlockData block);

	public ArrayList<RaidBlockData> getAllAlteredBlocks();
	public void addBlock(RaidBlockData block);

}
