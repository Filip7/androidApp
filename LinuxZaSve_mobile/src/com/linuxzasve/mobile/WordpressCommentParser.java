package com.linuxzasve.mobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class WordpressCommentParser {
	private String pageUrl;
	private String fullPageContent = "";
	
	private List<Komentar> listaKomentara;
	
	private static final String AUTHOR_PATTERN = "<cite class=\"fn\">(.*)</cite>";
	private static final String AUTHOR_WITH_URL_PATTERN = "rel=\"external nofollow\" class=\"url\">(.*?)</a>";
	private static final String THUMBNAIL_PATTERN = "src=\"(.*?)\"";
	private static final String PUBLISHED_TIME_PATTERN = ">(.*)</a>";
	
	public WordpressCommentParser(String url) throws IOException {
		listaKomentara = new ArrayList<Komentar>();
		this.pageUrl = url;
		
		Document doc = Jsoup.connect(pageUrl).get();
		
		Elements komentari = doc.select(".comment-body");
		
		
		
		for (Element e : komentari) {
			Komentar komentar = new Komentar();
			komentar.setCreator( parseInput(AUTHOR_PATTERN, e.select(".comment-author").toString()).contains("</a>") 
					? parseInput(AUTHOR_WITH_URL_PATTERN, e.select(".comment-author").toString())
					: parseInput(AUTHOR_PATTERN, e.select(".comment-author").toString()));
			komentar.setThumbnailUrl(parseInput(THUMBNAIL_PATTERN, e.select(".comment-author").toString()));
			komentar.setPublishDate(parseInput(PUBLISHED_TIME_PATTERN, e.select(".comment-meta").toString()));
			komentar.setContent(e.select("p").toString());
			
			listaKomentara.add(0, komentar);
		}
	}
	
	/**
	 * Funkcija vraca listu postova
	 * 
	 * @return listu postova
	 */
	public List<Komentar> getPosts(){
		return listaKomentara;
	}
	
	public String parseInput(String pattern, String input) {
	      Pattern r = Pattern.compile(pattern);
	      Matcher m = r.matcher(input);
	      if (m.find( )) {
	    	  return m.group(1);
	      }		
		return "";
	}
}
