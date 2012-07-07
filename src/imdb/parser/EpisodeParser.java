package imdb.parser;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

class EpisodeParser {
	private Document doc;
	public EpisodeParser(Document doc) {
		this.doc = doc.clone();
	}
	
	public void parse(Episode e) {
		e.showId = this.getShowId();
		e.season = this.getSeason();
		e.number = this.getNumber();
	}

	private int getNumber() {
		Element tv_h = doc.getElementsByClass("tv_header").get(0);
		return Integer.valueOf(tv_h.child(1).text().replace("Season", "").replace("Episode", "").split(",")[1].trim());
	}

	private int getSeason() {
		Element tv_h = doc.getElementsByClass("tv_header").get(0);
		return Integer.valueOf(tv_h.child(1).text().replace("Season", "").replace("Episode", "").split(",")[0].trim());
	}

	private String getShowId() {
		Element tv_h = doc.getElementsByClass("tv_header").get(0);
		Matcher m = Pattern.compile("tt\\d+", Pattern.CASE_INSENSITIVE).matcher(tv_h.child(0).attr("href"));
		if(m.find()) {
			return m.group();
		}
		return null;
		
	}


}
