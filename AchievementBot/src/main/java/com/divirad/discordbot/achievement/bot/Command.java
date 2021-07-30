package com.divirad.discordbot.achievement.bot;

import java.util.ArrayList;

import com.divirad.discordbot.achievement.database.AchievementDTO;
import com.divirad.discordbot.achievement.database.AchievementDTO.AchievementDao;
import com.divirad.discordbot.achievement.database.AchievementRefStizzler;
import com.divirad.discordbot.achievement.database.AchievementRefStizzler.AchievementRefStizzlerDao;
import com.divirad.discordbot.achievement.database.Stizzler.StizzlerDao;
import com.divirad.discordbot.achievement.database.StizzlerRefGuild.StizzlerRefGuildDao;
import com.divirad.discordbot.achievement.lib.CommandAnnotations.AdminOnly;
import com.divirad.discordbot.achievement.lib.CommandAnnotations.StizzlerOnly;

import net.dv8tion.jda.api.entities.TextChannel;

public enum Command {
	
	@AdminOnly
	GRANT {
		public void execute(String[] params, TextChannel source) throws ArrayIndexOutOfBoundsException {
			if(params.length != 2) {
				source.sendMessage("Invalid argument count for command GRANT").queue();
				GRANT.help(source);
			}
			
			AchievementDTO a = AchievementDao.instance.get_by_name(params[1]);
			if(a.achievement_type_id != 1)
				throw new IllegalArgumentException("Can only manually award achievements with type \"manual\n");
			AchievementRefStizzlerDao.instance.award(params[0].substring(3, params[0].length() - 1), a.id);
			
		}
		
		public void help(TextChannel source) {
			source.sendMessage("Awards a user with an achievement"
					+ "\n\nSyntax:\n>GRANT <@DISCORDUSER> <ACHIEVEMENTNAME>").queue();
		}
	},
	@AdminOnly
	CREATE,
	HOWMANY {

		@Override
		public void execute(String[] params, TextChannel source) throws ArrayIndexOutOfBoundsException {
			if(params.length != 1) {
				source.sendMessage("Invalid argument count for command HOWMANY").queue();
				HOWMANY.help(source);
			}
			int all_stizzler_count = StizzlerRefGuildDao.instance.count_stizzler_on_server(source.getGuild().getId());
			AchievementDTO a = AchievementDao.instance.get_by_name(params[0]);
			int stizzler_with_ach_count = AchievementRefStizzlerDao.instance.how_many_have_achievement(a.id);
			
			double percent = ((double) stizzler_with_ach_count) / all_stizzler_count * 100;
			
			source.sendMessage(percent + "% of users on this server have been awarded the achievement " + a.name).queue();
		}

		@Override
		public void help(TextChannel source) {
			source.sendMessage("Shows how many percent of users have been awarded an achievement"
				+ "\n\nSyntax:\nHOWMANY <ACHIEVEMENTNAME>").queue();
		}
		
	},
	WHOHAS {

		@Override
		public void execute(String[] params, TextChannel source) throws ArrayIndexOutOfBoundsException {
			if(params.length != 1) {
				source.sendMessage("Invalid argument count for command WHOHAS").queue();
				WHOHAS.help(source);
			}
			
			AchievementDTO achievement = AchievementDao.instance.get_by_name(params[0]);
			ArrayList<AchievementRefStizzler> stizzler_ids = 
					AchievementRefStizzlerDao.instance.get_by_achievement_id(achievement.id);
			StringBuilder name_list = new StringBuilder();
			stizzler_ids.forEach(t -> name_list.append(StizzlerDao.instance.select_by_id(t.stizzler_id).discord_tag + "\n"));
			
			source.sendMessage("Following users have been awarded the " + params[0] + " achievement:\n\n" + name_list).queue();
		}

		@Override
		public void help(TextChannel source) {
			source.sendMessage("Shows a list of discord tags of all users that have been awarded an achievement"
					+ "\n\nWHOHAS <ACHIEVEMENTNAME>").queue();
		}
		
	},
	@StizzlerOnly
	MOI,
	@StizzlerOnly
	IMNOOB,
	THEYHAVE,
	
	HELP {
		public void execute(String[] params, TextChannel source) throws ArrayIndexOutOfBoundsException {
			if(params.length == 0) source.sendMessage(getCommandList()).queue();
			else if(params.length == 1) {
				try {
					Command c = Command.valueOf(params[0]);
					if(c.getClass().isAnnotationPresent(AdminOnly.class))
						source.sendMessage("!Admin Only!").queue();
					if(c.getClass().isAnnotationPresent(StizzlerOnly.class))
						source.sendMessage("!Stizzler Only!");
					c.help(source);
				} catch(IllegalArgumentException e) {
					source.sendMessage("Command " + params[0] + " does not exist. Use HELP for a list of all available commands").queue();
				}
			}
			else {
				source.sendMessage("Invalid argument count for command HELP").queue();
				HELP.help(source);
			}
		}
		
		public void help(TextChannel source) {
			source.sendMessage("Shows a short description of a command and it's syntax or a list of all commands when used without the commandname argument"
					+ "\n\nSyntax:\n>HELP [<COMMANDNAME>]").queue();
		}
		
		private String getCommandList() {
			String res = "";
			for(Command c : Command.values())
				res += "\n" + c.toString();
			return res;
		}
	};
	
	
	
	
	
	
	
	public void execute(String[] params, TextChannel source) throws ArrayIndexOutOfBoundsException {
		throw new AbstractMethodError();
	}
	
	public void help(TextChannel source) {
		throw new AbstractMethodError();
	}
}
