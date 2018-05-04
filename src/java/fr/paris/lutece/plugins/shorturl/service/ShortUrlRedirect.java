/*
 * Copyright (c) 2002-2017, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
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