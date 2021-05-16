package com.divirad.discordbot.achievement.database;

import com.divirad.discordbot.achievement.achievements.Achievement;
import com.divirad.discordbot.achievement.achievements.ManualAchievement;

@MysqlMarker.TableView(isWholeTable = true, tableName = "achievement")
public class AchievementDTO {

	@MysqlMarker.PrimaryKey
	public int id;
	public String name;
	public String description;
	public int achievement_type_id;
	
	public static class AchievementDao extends Dao<AchievementDTO> {

		public AchievementDao() {
			super(AchievementDTO.class);
		}
		
	}
	
	public enum AchievementType {
		MANUAL {
			private Achievement createObject(AchievementDTO a) {
				return new ManualAchievement(a.name, a.description);
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
