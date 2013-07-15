package imdb.parser;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TitleParser {
	private Document doc;
	public TitleParser(Document doc) {
		this.doc = doc.clone();
	}

	public void parse(Title t) {
		//Utan bild: http://www.imdb.com/title/tt0098801/
		t.rating = this.getRating();
		t.name = this.getName();
		t.posterUrl = this.getPosterUrl();
		t.genres = this.getGenres();
		t.description = this.getDescription();
		t.plotSummary = this.getPlotSummary();
		t.year = this.getYear();
		t.votingUsers = this.getVotingUsers();
		
		if(t instanceof Video) {
			VideoParser vp = new VideoParser(this.doc);
			vp.parse((Video)t);
		}
		
	}

	private String[] getGenres() {
		Elements h4s = doc.getElementsByTag("h4");
		
		for (int i = 0; i < h4s.size(); i++) {
			if(h4s.get(i).hasClass("inline") && h4s.get(i).text().toLowerCase().trim().startsWith("genres")) {
				Element parent = h4s.get(i).parent();
				h4s.get(i).remove();
				String[] ret = parent.text().split("\\|");
				for (int j = 0; j < ret.length; j++) {
					// 160 = &nbsp;
					ret[j] = ret[j].replace(String.valueOf((char)160), "").trim();
				}
				return ret;
			}
		}
		return null;
	}

	private String getPosterUrl() {
		Pattern p = Pattern.compile("^.+_V1", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
		Matcher m = p.matcher(doc.getElementsByAttributeValue("property", "og:image").attr("content"));
		if(m.find()) {
			return m.group() + "_SX640.jpg";
		}
		return null;
	}

	private String getName() {
		try {
			Element e = doc.getElementsByAttributeValue("itemprop", "name").get(0).clone();
			e.children().remove();

			return e.text().trim();
		}
		catch(Exception e) {
			Pattern p = Pattern.compile("^(.+?)(\\(.*)?$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
			Matcher m = p.matcher(doc.getElementsByAttributeValue("property", "og:title").attr("content"));
			if(m.find()) {
				return m.group(1).trim();
			}
			return null;
		}
	}
	

	private int getYear() {
		try {
			String t = doc.getElementsByAttributeValue("itemprop", "name").get(0).nextElementSibling().text();
			Matcher m = Pattern.compile("\\(.*?(\\d{4})").matcher(t);
			if(m.find()) {
				return Integer.valueOf(m.group(1));
			}
			
			return -1;
		}
		catch (Exception e) {
			System.err.println("ne");
			return -1;
		}

	}
	
	private int getVotingUsers() {
		Elements e = doc.getElementsByAttributeValue("itemprop", "ratingCount");
		if(e.size() > 0) {
			return Integer.valueOf(e.get(0).text().replace(",", ""));
		}
		return -1;
	}

	private float getRating() {
		try {
			return Float.valueOf(doc.getElementsByAttributeValue("itemprop", "ratingValue").get(0).text());
		} catch (Exception e) {
			Elements es = doc.getElementsByClass("star-box-giga-star");
			if(es.size() > 0) {
				return Float.valueOf(es.get(0).text());
			}
			return -1;
		}

	}
	
	private String getDescription() {
		Elements es = doc.getElementsByAttributeValue("itemprop", "description");
		if(es.size() > 0 && es.get(0).text().length() > 0) {
			
			if(es.get(0).children().size() > 0) {
				es.get(0).children().remove();
			}
			// 187 = »
			// 160 = &nbsp;
			return es.get(0).text().replace(String.valueOf((char)187), "").replace(String.valueOf((char)160), "").trim();
		}
		
		return null;
	}
	
	private String getPlotSummary() {
        Element e1 = doc.getElementById("titleStoryLine");
        if(e1 != null) {
            Element e2 = e1.getElementsByAttributeValue("itemprop", "description").get(0);
            Element e3 = e2.child(0);
            e3.children().remove();
            return e3.text();
        }
        return null;
	}

}
