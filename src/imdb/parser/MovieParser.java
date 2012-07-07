package imdb.parser;


import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

class MovieParser {
	private Document doc;
	public MovieParser(Document doc) {
		this.doc = doc.clone();
	}

	public void parse(Movie m) {
		m.director = this.getDirector();
	}

	private String getDirector() {
		Elements dir = doc.getElementsByAttributeValue("itemprop", "director");
		if(dir.size() > 0) {
			return dir.get(0).text();
		}
		return null;
	}

}
