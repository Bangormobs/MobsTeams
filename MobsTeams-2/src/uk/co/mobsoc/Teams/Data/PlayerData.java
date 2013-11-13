package uk.co.mobsoc.Teams.Data;

import java.sql.ResultSet;

import org.bukkit.block.Block;

public class PlayerData {
	public int key=-1, government=-1;
	public String playerName="";
	
	@Override
	public boolean equals(Object o){
		if(o instanceof PlayerData){
			return playerName.equalsIgnoreCase(((PlayerData) o).playerName);
		}
		if(o instanceof String){
			return playerName.equalsIgnoreCase((String)o);
		}
		return false;
	}

	public void setFromMySQL(ResultSet rs) {
		
	}

	public String getStatsString() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getChunksString() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean ownsBlock(Block block) {
		// TODO Auto-generated method stub
		return false;
	}
}
