package com.divirad.discordbot.achievement.database;

public class StizzlerRefGuild {

	@MysqlMarker.PrimaryKey
	String uid;
	
	@MysqlMarker.PrimaryKey
	String guild_id;
	
	public static class StizzlerRefGuildDao extends Dao<StizzlerRefGuild> {

		public static StizzlerRefGuildDao instance = new StizzlerRefGuildDao();
		
		public StizzlerRefGuildDao() {
			super(StizzlerRefGuild.class);
		}
		
		public StizzlerRefGuild select(String uid, String guild_id) {
			StizzlerRefGuild srg = new StizzlerRefGuild();
			srg.uid = uid;
			srg.guild_id = guild_id;
			return select(srg);
		}
		
		public void joinsServer(String uid, String guild_id) {
			StizzlerRefGuild srg = new StizzlerRefGuild();
			srg.uid = uid;
			srg.guild_id = guild_id;
			insert(srg);
		}
		
		public void leavesServer(String uid, String guild_id) {
			StizzlerRefGuild srg = new StizzlerRefGuild();
			srg.uid = uid;
			srg.guild_id = guild_id;
			delete(srg);
		}
	}
}
