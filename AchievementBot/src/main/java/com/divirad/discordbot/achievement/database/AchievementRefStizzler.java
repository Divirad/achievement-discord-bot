package com.divirad.discordbot.achievement.database;

import java.sql.Date;

@MysqlMarker.TableView(isWholeTable = true, tableName = "ref_achievement_stizzler")
public final class AchievementRefStizzler {

	@MysqlMarker.PrimaryKey
	int achievement_id;
	
	@MysqlMarker.PrimaryKey
	String stizzler_id;
	
	@MysqlMarker.AutomaticValue
	Date achieved_date;
	
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
	}
	
}
