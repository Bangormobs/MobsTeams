package uk.co.mobsoc.Teams;

import java.sql.Connection;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

import uk.co.mobsoc.Teams.Listeners.RaidListener;
import uk.co.mobsoc.Teams.Raid.RaidCollection;
import uk.co.mobsoc.Teams.Raid.UnclaimedRaid;

public class Main extends JavaPlugin{
	public static Main main;
	public static Connection conn;
	
	public void onEnable(){
		main = this;
		readMySqlData();
		new RaidListener(this);
		raids.add(new UnclaimedRaid(this));
	}
	
	public void onDisable(){
	}

	private void readMySqlData() {
		saveDefaultConfig();
		String userName = getConfig().getString("sql-username");
		String passWord = getConfig().getString("sql-password");
		String dataBase = getConfig().getString("sql-database");
		String IP = getConfig().getString("sql-ip");
		Util.init(userName, passWord, dataBase, IP);
	}

	ArrayList<RaidCollection> raids = new ArrayList<RaidCollection>();
	public ArrayList<RaidCollection> getAllRaids() {
		return raids;
	}
}
