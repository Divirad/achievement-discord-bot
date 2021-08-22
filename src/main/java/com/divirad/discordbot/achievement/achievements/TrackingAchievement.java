package com.divirad.discordbot.achievement.achievements;

import com.divirad.discordbot.achievement.database.AchievementDTO;

/**
 * Class for achievements that are automatically given after a measure that's
 * been tracked over time reaches a certain threshhold.
 * 
 * @author Cionco
 * @since 1.0
 *
 */
public class TrackingAchievement extends Achievement {

	public TrackingAchievement(String name, String description, AchievementDTO db_achievement) {
		super(name, description, db_achievement);
	}

}
