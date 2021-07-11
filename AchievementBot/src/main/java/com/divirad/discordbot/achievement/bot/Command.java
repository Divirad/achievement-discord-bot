package com.divirad.discordbot.achievement.bot;

public enum Command {
	
	GRANT {
		public void execute(String[] params) throws ArrayIndexOutOfBoundsException {
			
		}
		
		public void help() {
			throw new AbstractMethodError();
		}
	};
	
	
	
	
	
	
	
	public void execute(String[] params) throws ArrayIndexOutOfBoundsException {
		throw new AbstractMethodError();
	}
	
	public void help() {
		throw new AbstractMethodError();
	}
}
