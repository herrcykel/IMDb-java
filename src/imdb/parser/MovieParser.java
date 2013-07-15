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
		Elements e1 = doc.getElementsByAttributeValue("itemprop", "director");
		if(e1.size() > 0) {
			Elements e2 = e1.get(0).getElementsByAttributeValue("itemprop", "name");
            if(e2.size() > 0) {
                return e2.get(0).text();
            }

		}
		return null;
	}

}
