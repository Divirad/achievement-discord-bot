package com.divirad.discordbot.achievement.database;

import java.sql.Date;
import java.util.ArrayList;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

@MysqlMarker.TableView(isWholeTable = true, tableName = "ref_achievement_stizzler")
public final class AchievementRefStizzler {

	@MysqlMarker.PrimaryKey
	public int achievement_id;
	
	@MysqlMarker.PrimaryKey
	public String stizzler_id;
	
	@MysqlMarker.AutomaticValue
	public Date achieved_date;
	
	public AchievementRefStizzler() {}
	
	public static class AchievementRefStizzlerDao extends Dao<AchievementRefStizzler> {

		public static final AchievementRefStizzlerDao instance = new AchievementRefStizzlerDao();
		
		public AchievementRefStizzlerDao() {
			super(AchievementRefStizzler.class);
		}
		
		public void award(String stizzler_id, int achievement_id) {
			AchievementRefStizzler a = new AchievementRefStizzler();
			a.stizzler_id = stizzler_id;
			a.achievement_id = achievement_id;
			insert(a);
		}
		
		public int how_many_have_achievement(int achievement_id) {
			int res = Database.query("SELECT COUNT(*) FROM " + tableName + " WHERE achievement_id = ?",
					ps -> ps.setInt(1, achievement_id),
					rs -> { rs.next(); return rs.getInt(1); });
			fireRowSelected(new DaoEvent<>((AchievementRefStizzler) null, System.currentTimeMillis(), DaoEvent.SELECT, 1, this.cls));
			return res;
		}
		
		public ArrayList<AchievementRefStizzler> get_by_achievement_id(int achievement_id) {
			 ArrayList<AchievementRefStizzler> res = Database.query("SELECT * FROM " + tableName + " WHERE achievement_id = ?",
					 ps -> ps.setInt(1, achievement_id),
					 this::convAllInResultSet);
			 fireRowSelected(new DaoEvent<>(res, System.currentTimeMillis(), DaoEvent.SELECT, res.size(), this.cls));
			 return res;
		}
		
		public ArrayList<AchievementRefStizzler> get_by_stizzler_id(String uid) {
			ArrayList<AchievementRefStizzler> res = Database.query("SELECT * FROM " + tableName + " WHERE stizzler_id = ?", 
					ps -> ps.setString(1, uid),
					this::convAllInResultSet);
			fireRowSelected(new DaoEvent<>(res, System.currentTimeMillis(), DaoEvent.SELECT, res.size(), this.cls));
			return res;
		}
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof AchievementDTO) {
			AchievementDTO a = (AchievementDTO) o;
			return this.achievement_id == a.id;
		} else if(o instanceof Stizzler) {
			Stizzler s = (Stizzler) o;
			return this.stizzler_id == s.uid;
		} else if(o instanceof AchievementRefStizzler) {
			AchievementRefStizzler ars = (AchievementRefStizzler) o;
			return this.achievement_id == ars.achievement_id &&
					this.stizzler_id == ars.stizzler_id &&
					this.achieved_date == ars.achieved_date;
		} else return false;
	}
	
}
