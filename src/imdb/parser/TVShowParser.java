package imdb.parser;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class TVShowParser {
	private Document doc;
	public TVShowParser(Document doc) {
		this.doc = doc.clone();
	}

	public void parse(TVShow tvs) {
		tvs.seasonCount = this.getSeasonCount();
		
	}

	private int getSeasonCount() {
		Elements h4s = doc.getElementsByTag("h4");
		
		for (int i = 0; i < h4s.size(); i++) {
			if(h4s.get(i).hasClass("inline") && h4s.get(i).text().toLowerCase().trim().startsWith("season")) {
				Element sib = h4s.get(i).nextElementSibling();
				if(sib.text().indexOf("|") == -1) {
					return 1;
				}
				else {
					// 160 = &nbsp;
					return Integer.valueOf(sib.text().split("\\|")[0].replace(String.valueOf((char)160), "").trim());
				}
			}
		}
		return 0;
	}

	
}
