package com.divirad.discordbot.achievement.achievements;

import com.divirad.discordbot.achievement.database.AchievementDTO;

/**
 * Class for achievements that have to be given to members manually
 * 
 * @author Cionco
 * @since 1.0
 * 
 */
public class ManualAchievement extends Achievement {

	public ManualAchievement(String name, String description, AchievementDTO db_achievement) {
		super(name, description, db_achievement);
	}

}
