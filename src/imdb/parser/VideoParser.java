package imdb.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            Element infoBar = es.get(0);
            es = infoBar.getElementsByTag("time");
            if(es.size() > 0) {
                Element time = es.get(0);
                Matcher m = Pattern.compile("(\\d+)\\s*min", Pattern.CASE_INSENSITIVE).matcher(time.text());
                if(m.find()) {
                    return Integer.valueOf(m.group(1)) * 60;
                }
            }

        }

		
		return 0;
	}
}
