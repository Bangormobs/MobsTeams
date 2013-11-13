package uk.co.mobsoc.Teams;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import uk.co.mobsoc.Teams.Data.CommandData;
import uk.co.mobsoc.Teams.Data.PlayerData;

public class Util {
	private static PreparedStatement sqlGetPlayerFromName, sqlGetPlayerFromKey, sqlGetPlayersForGovernment, sqlGetAllPlayers,
									 sqlUpdatePlayer, sqlInsertPlayer,
	                                 sqlGetGovernmentFromKey, sqlGetAllGovernments,
	                                 sqlUpdateGovernment, sqlInsertGovernment,
	                                 sqlCommandsGetPending, sqlCommandReply;

	public static void init(String userName, String passWord, String dataBase, String IP){
		Statement stat;
		try {
		// 	I hate try catching but fuck I love MySQL
			Main.conn = DriverManager.getConnection("jdbc:mysql://"+IP+"/"+dataBase+"?user="+userName+"&password="+passWord);
			stat = Main.conn.createStatement();
			stat.execute("drop procedure if exists AddColumnUnlessExists;");
			String s = "create procedure AddColumnUnlessExists( ";
		       	s+= " IN dbName tinytext, ";
		       	s+= " IN tableName tinytext, ";
		       	s+= " IN fieldName tinytext, ";
		       	s+= " IN fieldDef text ) ";
		       	s+= "begin ";
		       	s+= " IF NOT EXISTS ( ";
		       	s+= "  SELECT * FROM information_schema.COLUMNS ";
		       	s+= "  WHERE column_name=fieldName ";
		       	s+= "  and table_name = tableName ";
		       	s+= "  and table_schema = dbName ";
		       	s+= "  ) ";
		       	s+= " THEN ";
		       	s+= "  set @ddl=CONCAT('ALTER TABLE ', dbName, '.', tableName, ' ADD COLUMN ',fieldName,' ',fieldDef); ";
		       	s+= "  prepare stmt from @ddl; ";
		       	s+= "  execute stmt; ";
		       	s+= " END IF; END; ";
		    stat.executeUpdate(s);
		    stat.execute("CREATE TABLE IF NOT EXISTS `PlayerData` (`key` int(11) NOT NULL AUTO_INCREMENT, `government` int(11) NOT NULL DEFAULT -1, `name` text NOT NULL, `chunks` text NOT NULL DEFAULT '', `stats` text NOT NULL DEFAULT '', PRIMARY KEY (`key`), UNIQUE(`key`));");
		    stat.execute("CREATE TABLE IF NOT EXISTS `GovernmentData` (`key` int(11) NOT NULL AUTO_INCREMENT, `name` text NOT NULL DEFAULT '', `type` text NOT NULL DEFAULT 'feudal', `meta` text NOT NULL DEFAULT '', `owner` int(11) NOT NULL DEFAULT -1, `monarchtimer` int(11) NOT NULL DEFAULT 0, `lastonline` DATETIME NOT NULL DEFAULT NOW(), PRIMARY KEY(`key`), UNIQUE(`key`));");
		    stat.execute("CREATE TABLE IF NOT EXISTS `CommandData` (`key` int(11) NOT NULL AUTO_INCREMENT, `name` text NOT NULL DEFAULT 'Unknown action', `player` text NOT NULL DEFAULT '', `command` text NOT NULL DEFAULT '', `reply` text NOT NULL DEFAULT 'pending', PRIMARY KEY(`key`), UNIQUE(`key`));"); 
		    
		    sqlGetPlayerFromName = Main.conn.prepareStatement("SELECT `key`, `government`, `name`, `chunks`, `stats` FROM `PlayerData` WHERE `name` = ?");
		    sqlGetPlayerFromKey = Main.conn.prepareStatement("SELECT `key`, `government`, `name`, `chunks`, `stats` FROM `PlayerData` WHERE `key` = ?");
		    sqlGetPlayersForGovernment = Main.conn.prepareStatement("SELECT `key`, `government`, `name`, `chunks`, `stats` FROM `PlayerData` WHERE `government` = ? ");
		    sqlGetAllPlayers = Main.conn.prepareStatement("SELECT `key`, `government`, `name`, `chunks`, `stats` FROM `PlayerData`");
		    
		    sqlUpdatePlayer = Main.conn.prepareStatement("UPDATE `PlayerData` SET `government` = ?, `name` = ?, `chunks` = ?, `stats` = ? WHERE `key` = ? ");
		    sqlInsertPlayer = Main.conn.prepareStatement("INSERT INTO `PlayerData` (`government`, `name`, `chunks`, `stats`) VALUES ( ? , ? , ? , ? );");
		    
		    sqlGetGovernmentFromKey = Main.conn.prepareStatement("SELECT `key`, `name`, `type`, `meta`, `owner`, `monarchtimer`, `lastonline` FROM `GovernmentData` WHERE `key` = ?");
		    sqlGetAllGovernments = Main.conn.prepareStatement("SELECT `key`, `name`, `type`, `meta`, `owner`, `monarchtimer`, `lastonline` FROM `GovernmentData`");

		    sqlUpdateGovernment = Main.conn.prepareStatement("UPDATE `GovernmentData` SET `name` = ?, `type` = ?, `meta` = ?, `owner` = ?, `monarchtimer` = ?, `lastonline` = ? WHERE `key` = ? ");
		    sqlInsertGovernment = Main.conn.prepareStatement("INSERT INTO `GovernmentData` (`name`, `type`, `meta`, `owner`, `monarchtimer`, `lastonline`) VALUES ( ? , ? , ? , ? , ? , ?);");
		    
		    sqlCommandsGetPending = Main.conn.prepareStatement("SELECT `key`, `name`, `player`, `command`, `reply` FROM `CommandData` WHERE `reply` = 'pending'");
		    sqlCommandReply = Main.conn.prepareStatement("UPDATE `CommandData` SET `reply` = ? WHERE `key` = ?");
		    
	       	//stat.execute("CALL AddColumnUnlessExists('"+ dataBase +"' , 'TeamData', 'beaconworld', 'text');");
	       	//stat.execute("CALL AddColumnUnlessExists('"+ dataBase +"' , 'TeamData', 'beaconx', 'int(11)');");
	        //	sqlGetTeams = Main.conn.prepareStatement("SELECT `key`, `teamname`, `teamdesc`, `chunks`, `canraid`, `canberaid`, `canpvp`,`ranks` FROM TeamData");
		    //	sqlGetPlayers = Main.conn.prepareStatement("SELECT `name`, `skills`, `stats`, `team`, `rank` FROM PlayerData");
		    //	sqlNewTeam = Main.conn.prepareStatement("INSERT INTO `TeamData` (`key`, `teamname`, `teamdesc`, `chunks`, `canraid`, `canberaid`, `canpvp`,`ranks`) VALUES (?,?,?,?,?,?,?,'Unnamed,Unnamed,Unnamed,Unnamed,Unnamed,Unnamed,Unnamed,Unnamed,Unnamed,Unnamed,SupremeLeader')");
		    //	sqlNewPlayer = Main.conn.prepareStatement("INSERT INTO `PlayerData` (`name`, `skills`, `stats`, `team`,`rank`, `created`) VALUES (?,?,?,?,0,NOW())");
		    //	sqlUpdateTeam = Main.conn.prepareStatement("UPDATE `TeamData` SET `teamname` = ?, `teamdesc` = ?, `chunks` = ?, `canraid` = ?, `canberaid` = ?, `canpvp` = ?, `ranks`=? WHERE `key` = ?");
		    //	sqlUpdatePlayer = Main.conn.prepareStatement("UPDATE `PlayerData` SET `skills` = ?, `stats` = ?, `team` = ?, `rank` = ? , `nameC` = ? WHERE LOWER(`name`)=LOWER(?)");
		    //	sqlGetPlayer = Main.conn.prepareStatement("SELECT `name`, `skills`, `stats`, `team`,`rank` FROM PlayerData WHERE `name` = ?");
		    //	sqlGetTeam = Main.conn.prepareStatement("SELECT `key`, `teamname`, `teamdesc`, `chunks`, `canraid`, `canberaid`, `canpvp`,`ranks` FROM TeamData WHERE `key` = ?");
		    //	sqlGetAlliance = Main.conn.prepareStatement("SELECT `team1`, `team2`, `state`,`wantsWar`, `wantsNeutral`, `wantsAlly` FROM AllianceData WHERE `team1` = ? AND `team2` = ? ");
		    //	sqlNewAlliance = Main.conn.prepareStatement("INSERT INTO `AllianceData` (`team1`, `team2`, `state`,`wantsWar`,`wantsNeutral`,`wantsAlly`) VALUES (?,?,?,?,?,?)");
		    //	sqlUpdateAlliance = Main.conn.prepareStatement("UPDATE `AllianceData` SET `state` = ?, `wantsWar` = ?, `wantsNeutral` = ?, `wantsAlly` = ? WHERE `team1` = ? AND `team2` = ?");
		    //	sqlDeleteAlliance = Main.conn.prepareStatement("DELETE FROM `AllianceData` WHERE `team1` = ? AND `team2` = ?");
		    //	sqlTeamPlayerData = Main.conn.prepareStatement("SELECT `name`, `skills`, `stats`, `team`,`rank` FROM PlayerData WHERE `team` = ? ");
		    //	sqlNewRSS = Main.conn.prepareStatement("INSERT INTO `webref_rss_items` (`title`, `description`, `link`, `stamp`) VALUES ( ? , ? , 'http://mobsoco.co.uk/', NOW() )");
		    //	sqlMarkBlock = Main.conn.prepareStatement("INSERT INTO `BlockData` (`x`, `y`, `z`, `world`, `timestamp`) VALUES ( ? , ? , ? , ?,  NOW())"); 
		    //	sqlUnMarkBlock = Main.conn.prepareStatement("DELETE FROM `BlockData` WHERE x = ? AND y = ? AND z = ? AND world = ?");
		    //	sqlCheckMarkBlock = Main.conn.prepareStatement("SELECT `x`, `y`, `z`, `world` FROM `BlockData` WHERE x = ? AND y = ? AND z = ? AND world = ?");
		    //	sqlPurgeBlocks = Main.conn.prepareStatement("DELETE FROM `BlockData` WHERE `timestamp` < DATE_SUB(NOW(), INTERVAL 30 MINUTE);");
		    //	sqlDeleteTeam = Main.conn.prepareStatement("DELETE FROM `TeamData` WHERE `key` = ?");
		    //	sqlPlayerCanPvP = Main.conn.prepareStatement("SELECT `created` < DATE_SUB(NOW(), INTERVAL 2 DAY) From PlayerData where name = ? ");
		    //	sqlPlayerForfeit = Main.conn.prepareStatement("UPDATE `PlayerData` SET `created` = 0 WHERE `name` = ?");
		    //	sqlNewWeight = Main.conn.prepareStatement("INSERT INTO `Weight` (`stat`, `weight`, `hidden`) VALUES ( ? , ? , 1 )");
		    //	sqlSetWeight = Main.conn.prepareStatement("UPDATE `Weight` SET `weight` = ? WHERE `stat` = ?");
		    //	sqlSetStatHidden = Main.conn.prepareStatement("UPDATE `Weight` SET `hidden` = ? WHERE `stat` = ?");
		    //	sqlGetWeight = Main.conn.prepareStatement("SELECT `weight` FROM `Weight` WHERE `stat` = ?");
		    //	sqlWarTimer = Main.conn.prepareStatement("SELECT (`warTimer`>0 and `warTimer`<NOW()) from `AllianceData` where `team1` = ? and `team2` = ?");
		    //	sqlStartWarTimer = Main.conn.prepareStatement("UPDATE `AllianceData` SET `warTimer` = DATE_ADD(NOW(), INTERVAL 2 DAY) WHERE `team1` = ? AND `team2` = ?");
		    //	sqlUnsetWarTimer = Main.conn.prepareStatement("UPDATE `AllianceData` SET `warTimer` = 0 WHERE `team1` = ? AND `team2` = ?");
		    //	sqlSetTeamBeacon = Main.conn.prepareStatement("UPDATE `TeamData` SET `beaconx` = ?, `beacony` = ?, `beaconz` = ?, `beaconworld` = ? WHERE `key` = ?");
		    //	sqlGetTeamBeacon = Main.conn.prepareStatement("SELECT `beaconx`, `beacony`, `beaconz`, `beaconworld` FROM `TeamData` WHERE `key` = ?");
		    //	sqlGetPlayerNameC = Main.conn.prepareStatement("SELECT `nameC` FROM `PlayerData` WHERE `name` = ?");
		    //	sqlGetResources = Main.conn.prepareStatement("SELECT `stickCount`, `plankCount`, `cobbleCount`, `ironCount`, `goldCount`, `diamondCount`, `leatherCount`, `stringCount` FROM `PlayerData` WHERE `name` = ?");
		    //	sqlSetResources = Main.conn.prepareStatement("UPDATE `PlayerData` SET `stickCount` = ? , `plankCount` = ?, `cobbleCount` = ?, `ironCount` = ?, `goldCount` = ?, `diamondCount` = ?, `leatherCount` = ?, `stringCount` = ? WHERE `name` = ?");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static PlayerData getPlayerData(String name){
		try{
			sqlGetPlayerFromName.setString(0, name);
			sqlGetPlayerFromName.execute();
			ResultSet rs = sqlGetPlayerFromName.getResultSet();
			if(rs.next()){
				PlayerData pd = new PlayerData();
				pd.setFromMySQL(rs);
				return pd;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		PlayerData pd = new PlayerData();
		pd.playerName = name;
		return pd;
	}
	
	public static void setPlayerData(PlayerData pd){
		if(pd.key == -1){
			// New Data
			try {
				sqlInsertPlayer.setInt(0, pd.government);
				sqlInsertPlayer.setString(1,pd.playerName);
				sqlInsertPlayer.setString(2, pd.getChunksString());
				sqlInsertPlayer.setString(3, pd.getStatsString());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try {
				sqlUpdatePlayer.setInt(0, pd.government);
				sqlUpdatePlayer.setString(1,pd.playerName);
				sqlUpdatePlayer.setString(2, pd.getChunksString());
				sqlUpdatePlayer.setString(3, pd.getStatsString());
				sqlUpdatePlayer.setInt(4, pd.key);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void updateCommandReply(CommandData cd){
		try {
			sqlCommandReply.setString(1, cd.reply);
			sqlCommandReply.setInt(2, cd.key);
			sqlCommandReply.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
