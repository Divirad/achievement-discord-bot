package com.divirad.discordbot.achievement.achievements;

import com.divirad.discordbot.achievement.database.AchievementDTO;

/**
 * Class for achievements that are automatically given after a single event occurs
 * 
 * @author Cionco
 * @since 1.0
 *
 */
public class OneTimeAchievement extends Achievement {

	public OneTimeAchievement(String name, String description, AchievementDTO db_achievement) {
		super(name, description, db_achievement);
	}

}
