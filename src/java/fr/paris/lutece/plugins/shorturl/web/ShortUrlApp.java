/*
 * Copyright (c) 2002-2011, Mairie de Paris
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

 
package fr.paris.lutece.plugins.shorturl.web;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;


import fr.paris.lutece.plugins.shorturl.business.ShortUrl;
import fr.paris.lutece.plugins.shorturl.business.ShortUrlHome;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 * This class provides a simple implementation of an XPage
 */
public class ShortUrlApp implements XPageApplication
{
 private static final String PARAMETER_PAGE = "page";
 private static final String PARAMETER_ACTION_VIEW = "view";
 private static final String PARAMETER_ACTION_DO_CREATE = "do_create";
 private static final String PARAMETER_LONG_URL="url";
 
 private static final String PROPERTY_PAGE_PATH = "shorturl.pagePathLabel";
 private static final String PROPERTY_PAGE_TITLE = "shorturl.pageTitle";
 private static final String PARAMETER_ACTION = "action";
 private static final String PARAMETER_RESULT = "result";
 private static final String TEMPLATE_CREATE_SHORTURL = "skin/plugins/shorturl/create_shorturl.html";
 private static final String PARAMETER_SHORTURL_ABBREV = "abbreviation";
private static final String MESSAGE_ABBREVIATION_EXISTS = null;
private static final String MARK_RESULT = "result";
 
     // private fields
    private Plugin _plugin;   
     /**
     * Returns the content of the page shorturl. 
     * @param request The http request
     * @param nMode The current mode
     * @param plugin The plugin object
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException Message displayed if an exception occurs
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
        throws SiteMessageException
    {
        XPage page = new XPage(  );

        String strPluginName = request.getParameter( PARAMETER_PAGE );
        String strAbbreviation = request.getParameter( PARAMETER_SHORTURL_ABBREV );
        String strAction = request.getParameter( PARAMETER_ACTION );
        String strResult = request.getParameter( PARAMETER_RESULT);
        
        _plugin = PluginService.getPlugin( strPluginName );

        page.setTitle( AppPropertiesService.getProperty( PROPERTY_PAGE_TITLE ) );
        page.setPathLabel( AppPropertiesService.getProperty( PROPERTY_PAGE_PATH ) );
        
        if (  strAction == null && strAbbreviation == null  )
        {
        	 page.setContent("");
        	 
        }
        if ( ( strAction != null ) && strAction.equals( PARAMETER_ACTION_VIEW ) )
        {
            page.setContent( viewShortenerUrl( strAbbreviation ) );
        }
        
        if ( ( strAction != null ) && strAction.equals( PARAMETER_ACTION_DO_CREATE ) )
        {
            if( ShortUrlHome.findByPrimaryKey(strAbbreviation, plugin)!= null)
            {
            	SiteMessageService.setMessage( request, MESSAGE_ABBREVIATION_EXISTS, SiteMessage.TYPE_STOP );
            }
            String strUrl =  request.getParameter( PARAMETER_LONG_URL );
            
            String strGenerated = doCreateShortener( strUrl,   new java.sql.Timestamp( new java.util.Date(  ).getTime(  ) ) );
            page.setContent( createShortUrlContent( strAbbreviation , strGenerated) );
        }
        if ( ( strAction != null ) && strAction.equals( PARAMETER_ACTION_VIEW ) )
        {
            page.setContent( createShortUrlContent( strAbbreviation ,strResult) );
        }
        
        return page;
    }

	private String doCreateShortener(String strUrl,
			Timestamp timestamp) {
		ShortUrl shortened = new ShortUrl(  );
		shortened.setShortenerUrl(strUrl);
		String strAbbreviation=(String) getKey().subSequence(0,4);
		shortened.setAbbreviation(strAbbreviation);
		shortened.setCreationDate(new java.sql.Timestamp( new java.util.Date(  ).getTime(  ) ));
		ShortUrl shorte = ShortUrlHome.create(shortened, _plugin);
		return shorte.getAbbreviation();
		
	}
	private String viewShortenerUrl(String strAbbreviation) {
		// TODO Auto-generated method stub
		return null;
	}
	private String createShortUrlContent(String strPageName, String strResult) {
   	 Map<String, Object> model = new HashMap<String, Object>(  );
     model.put( MARK_RESULT, strResult );
     HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_SHORTURL
    		 , Locale.getDefault(  ), model );
     return template.getHtml(  );
	}
	
	  private static final char[] symbols = new char[36];

	  static {
	    for (int idx = 0; idx < 10; ++idx)
	      symbols[idx] = (char) ('0' + idx);
	    for (int idx = 10; idx < 36; ++idx)
	      symbols[idx] = (char) ('a' + idx - 10);
	  }

	  private final Random random = new Random();

	  private final char[] buf =new char[6];



	  public String getKey()
	  {
	    for (int idx = 0; idx < buf.length; ++idx) 
	      buf[idx] = symbols[random.nextInt(symbols.length)];
	    return new String(buf);
	  }

	
}