package com.divirad.discordbot.achievement.database;

@MysqlMarker.TableView(isWholeTable = true, tableName = "guild")
public final class Guild {

	@MysqlMarker.PrimaryKey
	public String guild_id;
	
	public String servername;
	
	public static class GuildDao extends Dao<Guild> {

		public static final GuildDao instance = new GuildDao();
		
		public GuildDao() {
			super(Guild.class);
		}
		
		public Guild getById(String id) {
			Guild g = new Guild();
			g.guild_id = id;
			return select(g);
		}
		
		public void updateServername(String guild_id, String servername) {
			Guild g = new Guild();
			g.guild_id = guild_id;
			g.servername = servername;
			update(g);
		}
		
		public void bot_joined_server(String guild_id, String servername) {
			Guild g = new Guild();
			g.guild_id = guild_id;
			g.servername = servername;
			insert(g);
		}
	}
}