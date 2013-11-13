package uk.co.mobsoc.Teams.Data;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

public class ChunkData {
	int x,z;
	public ChunkData(Location l){
		x = l.getChunk().getX();
		z = l.getChunk().getZ();
	}
	
	public boolean isInside(Location l){
		Chunk c = l.getChunk();
		if(x == c.getX() && z == c.getZ()){
			return true;
		}
		return false;
	}
	
	public boolean isInside(Block b){
		return isInside(b.getLocation());
	}
	
	public boolean isInside(Entity e){
		return isInside(e.getLocation());
	}
}
