package com.divirad.discordbot.achievement.bot;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.divirad.discordbot.achievement.database.AchievementDTO;
import com.divirad.discordbot.achievement.database.AchievementDTO.AchievementDao;
import com.divirad.discordbot.achievement.database.AchievementRefStizzler;
import com.divirad.discordbot.achievement.database.AchievementRefStizzler.AchievementRefStizzlerDao;
import com.divirad.discordbot.achievement.database.Stizzler.StizzlerDao;
import com.divirad.discordbot.achievement.database.StizzlerRefGuild.StizzlerRefGuildDao;
import com.divirad.discordbot.achievement.lib.CommandAnnotations.AdminOnly;
import com.divirad.discordbot.achievement.lib.CommandAnnotations.StizzlerOnly;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public enum Command {
	
	@AdminOnly
	GRANT(2) {
		public void execute(String[] params, GuildMessageReceivedEvent event) throws ArrayIndexOutOfBoundsException {
			super.execute(params, event);
			TextChannel sourceChannel = event.getChannel();
			
			AchievementDTO a = AchievementDao.instance.get_by_name(params[1]);
			if(a.achievement_type_id != 1)
				throw new IllegalArgumentException("Can only manually award achievements with type \"manual\n");
			AchievementRefStizzlerDao.instance.award(params[0].substring(3, params[0].length() - 1), a.id);
			
		}
		
		public void help(GuildMessageReceivedEvent event) {
			event.getChannel().sendMessage("Awards a user with an achievement"
					+ "\n\nSyntax:\n>" + this.name() + " <@DISCORDUSER> <ACHIEVEMENTNAME>").queue();
		}
	},
	@AdminOnly
	CREATE(3) {

		@Override
		public void execute(String[] params, GuildMessageReceivedEvent event) throws ArrayIndexOutOfBoundsException {
			super.execute(params, event);
			TextChannel sourceChannel = event.getChannel();
										
			switch(Integer.parseInt(params[0])) {
			case 1:
				AchievementDao.instance.create(params[1], params[2], Integer.parseInt(params[0]));
				break;
			default: 
				sourceChannel.sendMessage("Achievement Type " + params[0] + " not implemented in this version");
			}
			
		}

		@Override
		public void help(GuildMessageReceivedEvent event) {
			event.getChannel().sendMessage("Creates a new achievement. Only manual implemented in this version"
					+ "\n\nSyntax: >" + this.name() + " <ACHIEVEMENTTYPE (1 - Manual, 2 - Onetime, 3 - Tracking)> <ACHIEVEMENTNAME> <ACHIEVEMENTDESCRIPTION>");
		}
		
	},
	HOWMANY(1) {

		@Override
		public void execute(String[] params, GuildMessageReceivedEvent event) throws ArrayIndexOutOfBoundsException {
			super.execute(params, event);
			TextChannel sourceChannel = event.getChannel();

			int all_stizzler_count = StizzlerRefGuildDao.instance.count_stizzler_on_server(sourceChannel.getGuild().getId());
			AchievementDTO a = AchievementDao.instance.get_by_name(params[0]);
			int stizzler_with_ach_count = AchievementRefStizzlerDao.instance.how_many_have_achievement(a.id);
			
			double percent = ((double) stizzler_with_ach_count) / all_stizzler_count * 100;
			
			sourceChannel.sendMessage(percent + "% of users on this server have been awarded the achievement " + a.name).queue();
		}

		@Override
		public void help(GuildMessageReceivedEvent event) {
			event.getChannel().sendMessage("Shows how many percent of users have been awarded an achievement"
				+ "\n\nSyntax:\n>" + this.name() + " <ACHIEVEMENTNAME>").queue();
		}
		
	},
	WHOHAS(1) {

		@Override
		public void execute(String[] params, GuildMessageReceivedEvent event) throws ArrayIndexOutOfBoundsException {
			super.execute(params, event);
			TextChannel sourceChannel = event.getChannel();
			
			AchievementDTO achievement = AchievementDao.instance.get_by_name(params[0]);
			ArrayList<AchievementRefStizzler> stizzler_ids = 
					AchievementRefStizzlerDao.instance.get_by_achievement_id(achievement.id);
			StringBuilder name_list = new StringBuilder();
			stizzler_ids.forEach(t -> name_list.append(StizzlerDao.instance.select_by_id(t.stizzler_id).discord_tag + "\n"));
			
			sourceChannel.sendMessage("Following users have been awarded the " + params[0] + " achievement:\n\n" + name_list).queue();
		}

		@Override
		public void help(GuildMessageReceivedEvent event) {
			event.getChannel().sendMessage("Shows a list of discord tags of all users that have been awarded an achievement"
					+ "\n\n>" + this.name() + " <ACHIEVEMENTNAME>").queue();
		}
		
	},
	@StizzlerOnly
	MOI(0) {

		@Override
		public void execute(String[] params, GuildMessageReceivedEvent event) throws ArrayIndexOutOfBoundsException {
			super.execute(params, event);
			TextChannel sourceChannel = event.getChannel();
			
			ArrayList<AchievementRefStizzler> achievement_ids =
					AchievementRefStizzlerDao.instance.get_by_stizzler_id(event.getAuthor().getId());
			StringBuilder achievement_list = new StringBuilder();
			achievement_ids.forEach(t -> achievement_list.append(AchievementDao.instance.get_by_id(t.achievement_id)));
			
			sourceChannel.sendMessage(event.getAuthor().getAsMention() + ", you have been awarded the following achievements:\n\n" + achievement_list).queue();
		}

		@Override
		public void help(GuildMessageReceivedEvent event) {
			event.getChannel().sendMessage("Lists all achievements that you have been awarded"
					+ "\n\nSyntax: >" + this.name());
		}
		
	},
	@StizzlerOnly
	IMNOOB(0) {

		@Override
		public void execute(String[] params, GuildMessageReceivedEvent event) throws ArrayIndexOutOfBoundsException {
			super.execute(params, event);
			TextChannel sourceChannel = event.getChannel();
			
			ArrayList<AchievementRefStizzler> my_achievements = 
					AchievementRefStizzlerDao.instance.get_by_stizzler_id(event.getAuthor().getId());
			
			StringBuilder result_list = new StringBuilder();
			((ArrayList<AchievementDTO>) AchievementDao.instance.get_all()
				.stream()
				.filter(p -> !my_achievements.contains(p))
				.collect(Collectors.toList()))
				.forEach(t -> result_list.append(t.name + "\n"));
			
			sourceChannel.sendMessage(event.getAuthor().getAsMention() + ", you're missing the following achievements\n\n" + result_list);			
		}

		@Override
		public void help(GuildMessageReceivedEvent event) {
			event.getChannel().sendMessage("Lists all achievements that you don't have been awarded yet"
					+ "\n\nSyntax: >" + this.name());
		}
		
	},
	THEYHAVE(1),
	
	HELP (-1){
		public void execute(String[] params, GuildMessageReceivedEvent event) throws ArrayIndexOutOfBoundsException {
			super.execute(params, event);
			TextChannel sourceChannel = event.getChannel();
			
			if(params.length == 0) sourceChannel.sendMessage(getCommandList()).queue();
			else if(params.length == 1) {
				try {
					Command c = Command.valueOf(params[0]);
					if(c.getClass().isAnnotationPresent(AdminOnly.class))
						sourceChannel.sendMessage("!Admin Only!").queue();
					if(c.getClass().isAnnotationPresent(StizzlerOnly.class))
						sourceChannel.sendMessage("!Stizzler Only!");
					c.help(event);
				} catch(IllegalArgumentException e) {
					sourceChannel.sendMessage("Command " + params[0] + " does not exist. Use HELP for a list of all available commands").queue();
				}
			}
			else {
				sourceChannel.sendMessage("Invalid argument count for command HELP").queue();
				HELP.help(event);
			}
		}
		
		public void help(GuildMessageReceivedEvent event) {
			event.getChannel().sendMessage("Shows a short description of a command and it's syntax or a list of all commands when used without the commandname argument"
					+ "\n\nSyntax:\n>" + this.name() + " [<COMMANDNAME>]").queue();
		}
		
		private String getCommandList() {
			String res = "";
			for(Command c : Command.values())
				res += "\n" + c.toString();
			return res;
		}
	};
	
	
	private final int param_count;
	
	private Command(int param_count) {
		this.param_count = param_count;
	}
	
	public void execute(String[] params, GuildMessageReceivedEvent event) throws ArrayIndexOutOfBoundsException {
		TextChannel sourceChannel = event.getChannel();
		if(!param_count_check(params)) {
			event.getChannel().sendMessage("Invalid argument count for command " + this.name()).queue();
			this.help(event);
			throw new IllegalStateException("Invalid argument count");
		}
	}
	
	public void help(GuildMessageReceivedEvent event) {
		throw new AbstractMethodError();
	}
	
	private boolean param_count_check(String[] params) {
		int supposed_param_count = this.param_count;
		if(supposed_param_count >= 0 && supposed_param_count != params.length) return false;
		else return true;
	}
}
