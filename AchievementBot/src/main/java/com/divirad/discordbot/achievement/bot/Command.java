package com.divirad.discordbot.achievement.bot;

import com.divirad.discordbot.achievement.database.AchievementDTO;
import com.divirad.discordbot.achievement.database.AchievementDTO.AchievementDao;
import com.divirad.discordbot.achievement.database.AchievementRefStizzler.AchievementRefStizzlerDao;

import net.dv8tion.jda.api.entities.TextChannel;

public enum Command {
	
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
	
	HELP {
		public void execute(String[] params, TextChannel source) throws ArrayIndexOutOfBoundsException {
			if(params.length == 0) source.sendMessage(getCommandList()).queue();
			else if(params.length == 1) {
				try {
					Command c = Command.valueOf(params[0]);
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
