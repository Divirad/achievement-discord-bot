package com.divirad.discordbot.achievement.database;

import java.util.EventListener;

public interface DaoListener extends EventListener {
	
	public void rowInserted(DaoEvent<?> e);
	public void rowDeleted(DaoEvent<?> e);
	public void rowUpdated(DaoEvent<?> e);
	public void rowSelected(DaoEvent<?> e);
	public void rowReplaced(DaoEvent<?> e);
	
}
