package com.divirad.discordbot.achievement.achievements;

import com.divirad.discordbot.achievement.database.AchievementDTO;

/**
 * Base class for all achievements.
 * 
 * @author Cionco
 * @since 1.0
 */
public abstract class Achievement {

	/**
	 *  Name that will be shown
	 */
	private String name;
	
	/**
	 *  How to get the achievement
	 */
	private String description;
	
	/**
	 * 	Link to the database object for this achievemnent
	 */
	private AchievementDTO db_achievement;
	
	public Achievement(String name, String description, AchievementDTO db_ach) {
		this.name = name;
		this.description = description;
		this.db_achievement = db_ach;
	}
}
