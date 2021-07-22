package com.divirad.discordbot.achievement.bot.log;

import java.util.List;

import com.divirad.discordbot.achievement.database.DaoEvent;
import com.divirad.discordbot.achievement.database.DaoListener;
import com.divirad.discordbot.achievement.database.MysqlMarker;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

public class Logger implements DaoListener {

	private Guild guild;
	private TextChannel log_channel;
	
	public Logger(Guild guild) {
		this.guild = guild;
		List<TextChannel> channels = guild.getTextChannelsByName("bot-logs", true);
		if(channels.size() == 0)
			throw new IllegalArgumentException("Can't initialize logging for server since no channel named \"bot-logs\" could be found");
		if(channels.size() > 1)
			throw new IllegalArgumentException("Can't initialize logging for server since there are more than one channels with the name \"bot-logs\"");
		log_channel = channels.get(0);
	}

	@Override
	public void rowInserted(DaoEvent<?> e) {
		log_channel.sendMessage(e.rows_affected + " rows inserted into table " + ((MysqlMarker.TableView) e.cls.getAnnotations()[0]).tableName());
	}

	@Override
	public void rowDeleted(DaoEvent<?> e) {
		log_channel.sendMessage(e.rows_affected + " rows deleted from table " + ((MysqlMarker.TableView) e.cls.getAnnotations()[0]).tableName());
	}

	@Override
	public void rowUpdated(DaoEvent<?> e) {
		log_channel.sendMessage(e.rows_affected + " rows updated in table " + ((MysqlMarker.TableView) e.cls.getAnnotations()[0]).tableName());
	}

	@Override
	public void rowSelected(DaoEvent<?> e) {}

	@Override
	public void rowReplaced(DaoEvent<?> e) {
		log_channel.sendMessage(e.rows_affected + " rows replaced in table " + ((MysqlMarker.TableView) e.cls.getAnnotations()[0]).tableName());
	}
}
