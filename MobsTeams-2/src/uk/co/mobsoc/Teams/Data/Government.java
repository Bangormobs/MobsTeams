package uk.co.mobsoc.Teams.Data;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class Government {
	int key=-1;
	private HashMap<String, String> metaData = new HashMap<String, String>();
	private ArrayList<String> playerNames = new ArrayList<String>();
	Government ownedBy=null;
	String type="";
	public abstract boolean isLord(PlayerData playerData);
	
	public boolean hasMember(PlayerData pd){
		for(String pName : playerNames){
			if(pd.equals(pName)){
				return true;
			}
		}
		return false;
	}
	public boolean hasMember(Player player) {
		for(String pName : playerNames){
			if(player.getName().equalsIgnoreCase(pName)){
				return true;
			}
		}
		return false;
	}
	
	public String getType(){
		return type;
	}
	public void loadFromString(String data){
		String[] pairs = data.split(";");
		for(String s : pairs){
			String[] pair = s.split("=");
			if(pair.length!=2){ continue; }
			metaData.put(pair[0], pair[1]);
		}
	}
	public String saveToString(){
		StringBuilder sb = new StringBuilder();
		for(String key : metaData.keySet()){
			sb.append(key);
			sb.append("=");
			sb.append(metaData.get(key));
			sb.append(";");
		}
		return sb.toString();
	}
	public String getName(){
		return getValue("Name");
	}
	
	public String getValue(String key){
		return metaData.get(key.toLowerCase());
	}
	
	public void setValue(String key, String value){
		if(key.contains(";") || key.contains("=") || value.contains(";") || value.contains(";")){
			System.out.println("ERROR : cannot store key:value pair");
			System.out.println(key = " -- "+value);
		}
		metaData.put(key.toLowerCase(), value);
	}
	public abstract ArrayList<PlayerData> getLordCandidates();
	public ArrayList<PlayerData> getMembers(){
		return null;
		
	}

	public boolean ownsBlock(Block block) {
		for(PlayerData pd : getMembers()){
			if(pd.ownsBlock(block)){
				return true;
			}
		}
		return false;
	}
}
