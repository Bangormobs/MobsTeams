package uk.co.mobsoc.Teams;

import java.sql.Connection;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	public static Main main;
	public static Connection conn;
	
	public void onEnable(){
		main = this;
		readMySqlData();
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
}
