package imdb.parser;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;



public abstract class Title {
	protected String name, id, posterUrl, description, plotSummary;
	protected String[] genres;
	protected int year, votingUsers;
	
	protected float rating;
	protected BufferedImage poster;
	
	private boolean triedPosterDownload = false;
	
	public Title(String id) {
		this.id = id;
	}
	
	public BufferedImage getPoster() throws IOException {
		if(this.poster == null && !this.triedPosterDownload) {
			this.downloadPoster();
		}

		return this.poster;
	}
	
	public void downloadPoster() throws IOException {
		try {
			if (this.poster == null && this.posterUrl != null) {
				this.poster = ImageIO.read(new URL(this.posterUrl));
			}
		}
		finally {
			this.triedPosterDownload = true;
		}
		
	}
	
	public URL getUrl() {
		try {
			return new URL(String.format("http://www.imdb.com/title/%s/", this.id));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getTypeName() {
		return this.getClass().getName().replace(this.getClass().getPackage().getName(), "").substring(1);
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
	
	public String getName() {
		return name;
	}
	
	public int getYear() {
		return year;
	}
	
	public int getVotingUsers() {
		return this.votingUsers;
	}

	public String getId() {
		return id;
	}

	public float getRating() {
		return rating;
	}
	
	public String[] getGenres() {
		return genres;
	}
	
	public String getDescription() {
		return description;
	}

	public String getPlotSummary() {
		return plotSummary;
	}

	public boolean isTVShow() {
		return this instanceof TVShow;
	}
	public boolean isMovie() {
		return this instanceof Movie;
	}
	public boolean isEpisode() {
		return this instanceof Episode;
	}
	public final TVShow asTVShow() {
		return (TVShow)this;
	}
	public final Movie asMovie() {
		return (Movie)this;
	}
	public final Episode asEpisode() {
		return (Episode)this;
	}

	
	/*public void testPrint() {
		final int TA_BORT;
		System.out.printf("*** %s [ID: %s] ***\n", this.getName(), this.getId());
		System.out.printf("Year: %s\n", (year >= 0 ? year : "N/A"));
		System.out.println("URL: " + this.getUrl());
		System.out.printf("Rating: %s\n", this.getRating() != -1 ? this.getRating() + " / 10" : "N/A");
		System.out.printf("Voting users: %s\n", this.getVotingUsers() != -1 ? this.getVotingUsers() : "N/A");
		String[] genres = this.getGenres();
		
		System.out.print("Genres: ");
		if(genres != null) {
			System.out.print(genres[0]);
			for (int i = 1; i < genres.length; i++) {
				System.out.printf(", %s", genres[i]);
			}
		}
        else {
            System.out.print("N/A");
        }
		System.out.println();
		System.out.println("Type: " + this.getTypeName());
		System.out.printf("Desc: \"%s\"\n", this.getDescription() != null ? this.getDescription() : "N/A");
		System.out.printf("Plot Summary: \"%s\"\n", this.getPlotSummary() != null ? this.getPlotSummary() : "N/A");

	}*/
	
	
	
}
