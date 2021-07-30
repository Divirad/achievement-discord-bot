package com.divirad.discordbot.achievement.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.divirad.discordbot.achievement.lib.CommandAnnotations;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AdminCommandListener extends ListenerAdapter {

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		// Admin commands must run through a channel with a specific name
		if(!event.getChannel().getName().equals("botcommands-admin")) return;
		Message message = event.getMessage();
		String message_content = message.getContentRaw();

		// Must start with command prompt >
		if(!message_content.startsWith(">")) return;
		message_content = message_content.substring(1);
		
		List<String> parameters = new ArrayList<String>();
		Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(message_content);
		while (m.find())
		    parameters.add(m.group(1));
		
		String command = parameters.remove(0);
		try {
			Command c = Command.valueOf(command);
			if(c.getClass().isAnnotationPresent(CommandAnnotations.StizzlerOnly.class)) throw new UnsupportedOperationException();
			c.execute(parameters.toArray(new String[parameters.size()]), event.getChannel());			
		} catch(IllegalArgumentException e) {
			event.getChannel().sendMessage("Command " + command + " does not exist. Use HELP for a list of all available commands").queue();
		} catch(UnsupportedOperationException e) {
			event.getChannel().sendMessage(command + " can not be used in an admin command channel").queue();
		}
	}

	
}
