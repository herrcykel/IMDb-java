package imdb.parser;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParseHandler {
	private Document doc;
	
	public Title getTitle(String id) throws IllegalArgumentException, IOException {
		/*if(!id.toLowerCase().startsWith("tt"))
			id = "tt" + id;*/
		if(!Pattern.compile("^tt\\d+$", Pattern.CASE_INSENSITIVE).matcher(id).matches()) {
			throw new IllegalArgumentException(String.format("Bad ID \"%s\" supplied.", id));
		}
		
		this.doc = createDoc(id);
		Title t;
		TitleParser tp = new TitleParser(doc);
		if(this.isTVShow()) {
			TVShowParser tvsp = new TVShowParser(doc);
			t = new TVShow(id);
			tp.parse(t);
			tvsp.parse((TVShow)t);
		}
		else if(this.isEpisode()) {
			EpisodeParser ep = new EpisodeParser(doc);
			t = new Episode(id);
			tp.parse(t);
			ep.parse((Episode)t);
		}
		else if(this.isMovie()) {
			MovieParser mp = new MovieParser(doc);
			t = new Movie(id);
			tp.parse(t);
			mp.parse((Movie)t);
		}
		else {
			throw new IllegalArgumentException(String.format("Bad ID \"%s\" supplied.", id));
		}
		return t;
	}
	
	public String getIdByName(String name) throws IOException {
	
		try {
			URL url = new URL(String.format("http://www.deanclatworthy.com/imdb/?q=%s", URLEncoder.encode(name, "UTF-8")));
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			StringBuilder sb = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			String content = sb.toString();
			try {
				JSONObject json = new JSONObject(content);
				if(!json.has("error")) {
					return json.getString("imdbid");
				}
				
			} catch (JSONException e) { }
		} catch (IOException e) {
			throw new IOException("Could not connect. Try again later");
		}

		
		
		return null;
	}
	
	
	public static String[] parseSeasonPage(String showId, int season) throws IOException {
		String[] ret;
		Document doc = createDoc(showId, season);
		Elements descs = doc.getElementsByAttributeValue("itemprop", "description");
		ret = new String[descs.size()];
		for (int i = 0; i < descs.size(); i++) {
			String href = descs.get(i).previousElementSibling().child(0).attr("href");
			Matcher m = Pattern.compile("tt\\d+", Pattern.CASE_INSENSITIVE).matcher(href);
			m.find();
			ret[i] = m.group();
		}
		
		return ret;
		
	}

	private boolean isEpisode() {
		boolean ret;
		Elements es = doc.getElementsByAttributeValue("property", "og:type");
		if(es.size() > 0) {
			ret = es.get(0).attr("content").toLowerCase().equals("video.episode");
		}
		else {
			ret = doc.getElementsByClass("tv_header").size() > 0;
		}

		return ret;
	}
	private boolean isMovie() {
		boolean ret;
		Elements es = doc.getElementsByAttributeValue("property", "og:type");
		if(es.size() > 0) {
			ret = es.get(0).attr("content").toLowerCase().equals("video.movie");
		}
		else {
			ret = !this.isTVShow() && !this.isEpisode() && doc.getElementsByClass("star-box-giga-star").size() > 0;
		}

		return ret;
	}
	private boolean isTVShow() {
		boolean ret;
		Elements es = doc.getElementsByAttributeValue("property", "og:type");
		if(es.size() > 0) {
			ret = es.get(0).attr("content").toLowerCase().equals("video.tv_show");
		}
		else {
			final String TV_SERIES = "TV Series";
			String infoBarText = doc.getElementsByClass("infobar").text().trim();
			ret = infoBarText.startsWith(TV_SERIES);
		}

		return ret;

	}
	
	public static String[] getSuggestions(String query) {
		try {
			URL url = new URL(String.format("http://sg.media-imdb.com/suggests/i/%s.json", URLEncoder.encode(query, "UTF-8")));
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			
			con.setRequestProperty("Accept", "*/*");
			con.setRequestProperty("Accept-Language", "sv-se,sv;q=0.8,en-us;q=0.5,en;q=0.3");
			con.setRequestProperty("Accept-Encoding", "gzip, deflate");
			con.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
			con.setRequestProperty("Keep-Alive", "115");
			con.setRequestProperty("Connection", "keep-alive");
			con.setRequestProperty("Referer", "http://www.imdb.com/");
	
			if(con.getResponseCode() != HttpURLConnection.HTTP_OK) {
				System.err.println(":( --> " + con.getResponseCode() + ": " + con.getResponseMessage());
				return null;
			}
			System.out.println("N1");
			
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line;
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}
			String json = builder.toString();
			reader.close();
			
			Matcher m = Pattern.compile("\\{.*}").matcher(json);
			if(!m.find())
				return null;
			
			json = m.group();
			
			String[] suggestions = null;
	
			JSONObject jo = new JSONObject(json);
			JSONArray ja = jo.getJSONArray("d");
			suggestions = new String[ja.length()];
			for (int i = 0; i < ja.length(); i++) {
				// ID ja.getJSONObject(i).getString("id")
				String name = ja.getJSONObject(i).getString("l"),
						id = ja.getJSONObject(i).getString("id");
				
				suggestions[i] = name;
				System.out.println(name + " [ " + id + " ]");
	
			}
			return suggestions;
		}
		catch (Exception e) {
			System.err.println("ImdbParser#getSuggestions");
			e.printStackTrace();
			return null;
		}

		
	}
	
	private static Document createDoc(String id) throws IOException {
		try {
			return Jsoup.connect(String.format("http://imdb.com/title/%s", id))
					.timeout(5000)
					.ignoreHttpErrors(true)
					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.162 Safari/535.19")
					.get();

			
		} catch (Exception e) {
			throw new IOException("Could not connect. Try again later");
		}
	}
	private static Document createDoc(String id, int season) throws IOException {

		try {
			return Jsoup.connect(String.format("http://imdb.com/title/%s/episodes?season=%s", id, season))
					.timeout(5000)
					.ignoreHttpErrors(true)
					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.162 Safari/535.19")
					.get();
			
		} catch (IOException e) {
			throw new IOException("Could not connect. Try again later");
		}
	}
	

	
}

