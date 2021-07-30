package com.divirad.discordbot.achievement.database;

import java.util.ArrayList;
import java.util.List;

@MysqlMarker.TableView(isWholeTable = true, tableName = "stizzler")
public final class Stizzler {

	@MysqlMarker.PrimaryKey
	public String uid;
	
	public String discord_tag;
	
//	@MysqlMarker.AutomaticValue
//	public Date stizzler_since;
	
	public Stizzler() {}
	
	public static class StizzlerDao extends Dao<Stizzler> {
		public static final StizzlerDao instance = new StizzlerDao();
		
		public StizzlerDao() {
			super(Stizzler.class);
		}
		
		public void insert(String uid, String discord_tag) {
			Stizzler s = new Stizzler();
			s.uid = uid;
			s.discord_tag = discord_tag;
			insert(s);
		}
		
		public Stizzler select_by_id(String uid) {
			Stizzler s = new Stizzler();
			s.uid = uid;
			return select(s);
		}
		
		public List<Stizzler> selectAll() {
			List<Stizzler> result = Database.query("Select * FROM " + 
									((MysqlMarker.TableView) Stizzler.class.getAnnotations()[0]).tableName()
								, this::convAllInResultSet);
			fireRowSelected(new DaoEvent<Stizzler>((ArrayList) result, System.currentTimeMillis(), DaoEvent.SELECT, result.size(), cls));
			return result;
		}

		public void updateTag(String uid, String discord_tag) {
			Stizzler s = new Stizzler();
			s.uid = uid;
			s.discord_tag = discord_tag;
			update(s);
		}
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Stizzler)) return false;
		
		Stizzler s = (Stizzler) o;
		return s.uid.equals(this.uid);
	}
}
