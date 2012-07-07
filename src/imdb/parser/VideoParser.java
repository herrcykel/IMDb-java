package imdb.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class VideoParser {
	private Document doc;
	public VideoParser(Document doc) {
		this.doc = doc.clone();
	}
	
	public void parse(Video v) {
		v.runtime = this.getRuntime();
	}

	private int getRuntime() {
		Elements es = doc.getElementsByClass("infobar");
		
		if(es.size() > 0) {
			Element infoBar = es.get(0).clone();
			infoBar.children().remove();
			Matcher m = Pattern.compile("(\\d+) min", Pattern.CASE_INSENSITIVE).matcher(infoBar.text());
			if(m.find()) {
				return Integer.valueOf(m.group(1)) * 60;
			}
			System.out.println(infoBar.text());
		}
		
		return 0;
	}
}
