package uk.co.mobsoc.Teams.Raid;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.ContainerBlock;

public class RaidBlockData {
	World world;
	int x,y,z;
	/** The Block ID that was or will be in the location this class has stored */
	public int id;
	/** The Block Metadata that was or will be in the location this class has stored */
	public int data;
	/** Unclaimed land timer */
	public int timer=0;
	
	public RaidBlockData(){
		
	}

	public RaidBlockData(Block block){
		world = block.getWorld();
		x = block.getX();
		y = block.getY();
		z = block.getZ();
		id = block.getTypeId();
		data = block.getData();
	}

	/**
	 * Set the block at this Location to the ID/data defined in the fields
	 */
	public void replace() {
		Block b = world.getBlockAt(x, y, z);
		if(b.getState() instanceof ContainerBlock){
			((ContainerBlock)b.getState()).getInventory().clear();
		}
		b.setType(Material.AIR);
		b.setTypeIdAndData(id, (byte) data, false);
		
	}
	
	/**
	 * Compare with a Bukkit block
	 * @param b
	 * @return true if the location and world are exactly the same, false else
	 */
	public boolean isEqualLocation(Block b){
		if(b==null){ return false; }
		return b.getX() == x &&
			   b.getY() == y &&
			   b.getZ() == z &&
			   b.getWorld().getName().equalsIgnoreCase(world.getName());
	}

	/**
	 * 
	 * @param Another BlockData to compare to
	 * @return true is both BlockData are the same World and location, false otherwise
	 */
	public boolean isEqualLocation(RaidBlockData bd){
		return bd.x == x &&
		       bd.y == y && 
		       bd.z == z &&
		       bd.world.getName().equalsIgnoreCase(world.getName());
	}

	/**
	 * 
	 * @return a BlockData instance where the location is the same as this one, but the ID and data are derived from the block currently present
	 */
	public RaidBlockData getCurrentBlockDataAt(){
		return new RaidBlockData(world.getBlockAt(x,y,z));
	}
	
	/**
	 * 
	 * @return a Block instance which has the data of the block in the world specified in the location specified
	 */
	public Block getCurrectBlockAt(){
		return world.getBlockAt(x,y,z);
	}
}
