package imdb;

import imdb.parser.Title;

import java.util.HashMap;


class Cache {
	private HashMap<String, Title> titleCache = new HashMap<String, Title>();
	
	public void addTitle(String id, Title t) {
		if(!this.titleCache.containsKey(id)) {
			this.titleCache.put(id, t);
		}
		
	}
	
	public boolean hasTitleCached(String id) {
		return this.titleCache.containsKey(id);
	}
	
	public Title getTitle(String id) {
		if(this.hasTitleCached(id)) {
			//System.out.println("FRÅN TITLE CACHE =))");
			return this.titleCache.get(id);
		}
		return null;
	}
	

}
