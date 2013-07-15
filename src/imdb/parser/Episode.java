package imdb.parser;

import imdb.IMDb;

import java.io.IOException;


public class Episode extends Video {
	
	protected int season, number;
	protected String showId;
	
	public Episode(String id) {
		super(id);
	}
	
	public TVShow getShow() throws IOException {
		return (TVShow)IMDb.getInstance().getById(this.showId);
	}
	
	public int getSeason() {
		return this.season;
	}
	public int getNumber() {
		return this.number;
	}
	
	public String getEpisodeIdStr() {
		return String.format("S%02dE%02d", this.season, this.number);
	}
	
	/*@Override
	public void testPrint() {
		final int TA_BORT;
		super.testPrint();
		System.out.println("Episode: " + this.getEpisodeIdStr());
		System.out.println();
	}*/
	

}
