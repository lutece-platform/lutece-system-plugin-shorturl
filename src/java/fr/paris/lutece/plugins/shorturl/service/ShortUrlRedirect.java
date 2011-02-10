package fr.paris.lutece.plugins.shorturl.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.paris.lutece.plugins.shorturl.business.ShortUrl;
import fr.paris.lutece.plugins.shorturl.business.ShortUrlHome;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class ShortUrlRedirect extends HttpServlet {
	private static final String PLUGIN_NAME = "shorturl";
	private static final String PARAMETER_KEY = "key";
	private static final String MESSAGE_URL_NOT_EXISTS = null;
	private static final String PROPERTY_SHORTURL_DOES_NOT_EXIST = "shorturl.urlPageNotExist";
	Plugin _plugin = PluginService.getPlugin(PLUGIN_NAME);

	public static String getUrlParam(HttpServletRequest req) {

		String reqUri = req.getRequestURI().toString();
		String queryString = req.getQueryString();

		if (queryString != null) {
			reqUri += "?" + queryString;
		}
		return reqUri;
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String strParam = getUrlParam(request);
		String strKey = strParam.substring(strParam.lastIndexOf("/"), strParam.length());
		ShortUrl shortUrl = ShortUrlHome.findByPrimaryKey(strKey, _plugin);
		String strDestination = "";
		if (shortUrl == null) {
			strDestination = AppPropertiesService
					.getProperty(PROPERTY_SHORTURL_DOES_NOT_EXIST)
					+ strKey;
		} else {
			strDestination = shortUrl.getShortenerUrl();
		}
		response.sendRedirect(response.encodeRedirectURL(strDestination));
	}
}