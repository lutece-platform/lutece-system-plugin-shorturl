package fr.paris.lutece.plugins.shorturl.service;

import java.util.Random;

import fr.paris.lutece.plugins.shorturl.business.ShortUrl;
import fr.paris.lutece.plugins.shorturl.business.ShortUrlHome;

public class ShortUrlRedirectService {

	private static final char[] symbols = new char[36];

	static {
		for (int idx = 0; idx < 10; ++idx)
			symbols[idx] = (char) ('0' + idx);
		for (int idx = 10; idx < 36; ++idx)
			symbols[idx] = (char) ('a' + idx - 10);
	}

	private final static Random random = new Random();

	private final static char[] buf = new char[128];

	public static String createShortener(String strUrl) {
		ShortUrl shortened = new ShortUrl();
		shortened.setShortenerUrl(strUrl);
		String strAbbreviation = (String) getKey(4);
		shortened.setAbbreviation(strAbbreviation);
		shortened.setCreationDate(new java.sql.Timestamp(new java.util.Date().getTime()));
		ShortUrl shorte = ShortUrlHome.create(shortened);
		return shorte.getAbbreviation();

	}

	public static void deleteShortener(String strKey) {
		ShortUrlHome.removeByKey(strKey);

	}

	public static ShortUrl getShortener(String strKey) {
		return ShortUrlHome.findByKey(strKey);

	}

	public static String getKey(int bKeylength) {

		for (int idx = 0; idx < buf.length; ++idx)
			buf[idx] = symbols[random.nextInt(symbols.length)];
		return bKeylength < buf.length ? new String(buf).substring(0, bKeylength) : new String(buf);
	}
}
