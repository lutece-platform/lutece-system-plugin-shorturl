/*
 * Copyright (c) 2002-2019, Mairie de Paris
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

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.shorturl.service.ShortUrlRedirectService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;

/**
 * This class provides a simple implementation of an XPage
 */
@Controller( xpageName = "shorturl", pageTitleI18nKey = "shorturl.pageTitle", pagePathI18nKey = "shorturl.pagePathLabel" )
public class ShortUrlApp extends MVCApplication
{
    private static final String VIEW_CREATE = "create";
    private static final String ACTION_DO_CREATE = "do_create";
    private static final String PARAMETER_LONG_URL = "url";
    private static final String TEMPLATE_CREATE_SHORTURL = "skin/plugins/shorturl/create_shorturl.html";
    private static final String MARK_RESULT = "result";

    // private fields
    private String _strKeyCreate=null;

 
     
    
    @Action( ACTION_DO_CREATE )
    public XPage doCreate( HttpServletRequest request )
    {
    	 String strUrl = request.getParameter( PARAMETER_LONG_URL );
    	 _strKeyCreate = ShortUrlRedirectService.createShortener( strUrl );
    	 return redirectView(request, VIEW_CREATE);
    	
    }
    @View( value = VIEW_CREATE, defaultView = true )
    public XPage createShortUrlContent( HttpServletRequest request )
    {
    	
    	Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_RESULT, _strKeyCreate );
       
        return getXPage( TEMPLATE_CREATE_SHORTURL, request.getLocale( ), model );
    }


}
