package imdb;

import imdb.parser.ParseHandler;
import imdb.parser.Title;

import java.io.IOException;



public class IMDb {
	private static final IMDb instance = new IMDb();
	private IMDb() {}
	public static IMDb getInstance() {
		return instance;
	}
	
	
	private Cache cache = new Cache();
	private ParseHandler ph = new ParseHandler();
	
	public Title getById(String id) throws IOException {
		
		if(this.cache.hasTitleCached(id)) {
			return this.cache.getTitle(id);
		}
		
		else {
			Title t = ph.getTitle(id);
			this.cache.addTitle(t.getId(), t);
			return t;
		}
		
	}
	
	public Title getByName(String name) throws IOException {
		String id = this.ph.getIdByName(name);
		
		if(id != null) {
			return this.getById(id);
		}
		return null;
	}

    public String[] getSearchSuggestions(String query) throws Exception {
        return ParseHandler.getSuggestions(query);
    }
	

}
