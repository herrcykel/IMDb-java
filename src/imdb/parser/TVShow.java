package imdb.parser;

import imdb.IMDb;

import java.io.IOException;
import java.util.HashMap;

public class TVShow extends Title {
	
	private HashMap<Integer, String[]> seasonCache = new HashMap<Integer, String[]>();
	
	protected int seasonCount;

	public TVShow(String id) {
		super(id);
	}
	
	public Episode getEpisode(int season, int episode) throws IOException {
		if(this.getSeasonCount() < season) {
			return null;
		}
		if(!this.hasCachedSeason(season)) {
			this.cacheSeason(season);
		}
		
		if(this.seasonCache.get(season).length < episode) {
			return null;
		}
		return (Episode)IMDb.getImdb().getById(this.seasonCache.get(season)[episode - 1]);

	}
	public int getEpisodeCount(int season) throws IOException {
		if(this.getSeasonCount() < season) {
			return 0;
		}
		
		if(!this.hasCachedSeason(season))
			this.cacheSeason(season);
		
		return this.seasonCache.get(season).length;
	}
	
	public int getSeasonCount() {
		return this.seasonCount;
	}
	
	private boolean hasCachedSeason(int season) {
		return this.seasonCache.containsKey(season);
	}

	private void cacheSeason(int season) throws IOException {
		if(season <= this.getSeasonCount() && !this.hasCachedSeason(season)) {
			this.seasonCache.put(season, ParseHandler.parseSeasonPage(this.id, season));
		}
	}

	/*@Override
	public void testPrint() {
		final int TA_BORT;
		super.testPrint();
		System.out.println("Season Count: " + this.getSeasonCount());
		System.out.println();
	}*/
	
	
}
