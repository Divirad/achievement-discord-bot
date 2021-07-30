package com.divirad.discordbot.achievement.database;

import java.sql.Date;

@MysqlMarker.TableView(isWholeTable = true, tableName = "ref_stizzler_guild")
public final class StizzlerRefGuild {

	@MysqlMarker.PrimaryKey
	String stizzler_id;
	
	@MysqlMarker.PrimaryKey
	String guild_id;
	
	@MysqlMarker.AutomaticValue
	Date stizzler_since;
	
	public static class StizzlerRefGuildDao extends Dao<StizzlerRefGuild> {

		public static StizzlerRefGuildDao instance = new StizzlerRefGuildDao();
		
		public StizzlerRefGuildDao() {
			super(StizzlerRefGuild.class);
		}
		
		public StizzlerRefGuild select(String uid, String guild_id) {
			StizzlerRefGuild srg = new StizzlerRefGuild();
			srg.stizzler_id = uid;
			srg.guild_id = guild_id;
			return select(srg);
		}
		
		public void joinsServer(String uid, String guild_id) {
			StizzlerRefGuild srg = new StizzlerRefGuild();
			srg.stizzler_id = uid;
			srg.guild_id = guild_id;
			insert(srg);
		}
		
		public void leavesServer(String uid, String guild_id) {
			StizzlerRefGuild srg = new StizzlerRefGuild();
			srg.stizzler_id = uid;
			srg.guild_id = guild_id;
			delete(srg);
		}
		
		public int count_stizzler_on_server(String guild_id) {
			int res = Database.query("SELECT COUNT(*) FROM " + tableName + " WHERE guild_id = ?", 
					ps -> ps.setString(1, guild_id),
					rs -> { rs.next(); return rs.getInt(1); });
			fireRowSelected(new DaoEvent<>((StizzlerRefGuild) null, System.currentTimeMillis(), DaoEvent.SELECT, 1, this.cls));
			return res;
		}
	}
}
