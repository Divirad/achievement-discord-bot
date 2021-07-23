package com.divirad.discordbot.achievement.database;

import com.divirad.discordbot.achievement.achievements.Achievement;
import com.divirad.discordbot.achievement.achievements.ManualAchievement;

@MysqlMarker.TableView(isWholeTable = true, tableName = "achievement")
public final class AchievementDTO {

	@MysqlMarker.PrimaryKey
	public int id;
	public String name;
	public String description;
	public int achievement_type_id;
	
	public AchievementDTO() {}
	
	public static class AchievementDao extends Dao<AchievementDTO> {

		public static final AchievementDao instance = new AchievementDao();
		
		public AchievementDao() {
			super(AchievementDTO.class);
		}
		
		public AchievementDTO get_by_name(String name) {
			AchievementDTO a = Database.query("SELECT * FROM " + this.tableName + " WHERE name = ?;", 
					ps -> ps.setString(1, name),
					this::convFirstInResultSet);
			fireRowSelected(new DaoEvent<>(a, System.currentTimeMillis(), DaoEvent.SELECT, 1, this.cls));
			return a;
		}
	}
	
	public enum AchievementType {
		MANUAL {
			private Achievement createObject(AchievementDTO a) {
				return new ManualAchievement(a.name, a.description, a);
			}
		},
		ONETIME,
		TRACKING;
		
		private Achievement createObject(AchievementDTO a) {
			throw new AbstractMethodError();
		}
	}
	
	public Achievement createAchievementObject() {
		return AchievementType.values()[achievement_type_id - 1].createObject(this);
	}

}
