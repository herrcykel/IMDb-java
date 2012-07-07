package imdb.parser;

public class Movie extends Video {
	protected String director;

	public Movie(String id) {
		super(id);
	}
	
	public String getDirector() {
		return this.director;
	}

	/*@Override
	public void testPrint() {
		final int TA_BORT;
		super.testPrint();
		System.out.println("Director: " + ((this.getDirector() != null) ? this.getDirector() : "N/A"));
		System.out.println();
	}*/
}
