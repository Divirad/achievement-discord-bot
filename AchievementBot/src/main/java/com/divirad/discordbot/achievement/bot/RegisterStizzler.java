package com.divirad.discordbot.achievement.bot;

import java.util.List;

import com.divirad.discordbot.achievement.database.Guild.GuildDao;
import com.divirad.discordbot.achievement.database.Stizzler;
import com.divirad.discordbot.achievement.database.Stizzler.StizzlerDao;
import com.divirad.discordbot.achievement.database.StizzlerRefGuild.StizzlerRefGuildDao;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RegisterStizzler extends ListenerAdapter {

	@Override
	public void onReady(ReadyEvent event) {
		System.out.println("oh, ja moin");
		for(Guild g : event.getJDA().getGuildCache()) {
			// Register/Update Guild/Server
			com.divirad.discordbot.achievement.database.Guild dbGuild = null;
			if((dbGuild = GuildDao.instance.getById(g.getId())) == null) {
				dbGuild = new com.divirad.discordbot.achievement.database.Guild();
				dbGuild.guild_id = g.getId();
				dbGuild.servername = g.getName();
			} else if(dbGuild.servername.equals(g.getName())) {
				dbGuild.servername = g.getName();
				GuildDao.instance.updateServername(dbGuild.guild_id, dbGuild.servername);
			}
			
			// Register/Update Stizzler
			List<Member> stizzler = g.getMembersWithRoles(g.getRolesByName("Stizzler", true));
			List<Stizzler> dbStizzler = StizzlerDao.instance.selectAll();
			
			for(Member m : stizzler) {
				Stizzler s = new Stizzler();
				s.uid = m.getId();
				if(!dbStizzler.contains(s))
					// Stizzler doesn't exist yet
					StizzlerDao.instance.insert(s.uid, m.getUser().getAsTag());
				else
					StizzlerDao.instance.updateTag(m.getId(), m.getUser().getAsTag());
				
				
				if(StizzlerRefGuildDao.instance.select(s.uid, g.getId()) == null)
					StizzlerRefGuildDao.instance.joinsServer(s.uid, g.getId());
			}
			
		}
		super.onReady(event);
	}

}
