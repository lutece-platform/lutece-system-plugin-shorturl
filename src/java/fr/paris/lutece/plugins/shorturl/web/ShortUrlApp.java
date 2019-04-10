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

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.shorturl.service.JsonUtil;
import fr.paris.lutece.plugins.shorturl.service.ShortUrlJsonResponse;
import fr.paris.lutece.plugins.shorturl.service.ShortUrlService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.json.ErrorJsonResponse;
import fr.paris.lutece.util.json.JsonResponse;

/**
 * ShortUrlApp use for creating/deleting short Url
 */
@Controller( xpageName = "shorturl", pageTitleI18nKey = "shorturl.pageTitle", pagePathI18nKey = "shorturl.pagePathLabel" )
public class ShortUrlApp extends MVCApplication
{
    private static final String VIEW_CREATE = "create";
    private static final String ACTION_DO_CREATE = "do_create";
    private static final String ACTION_DO_CREATE_WS = "do_create_ws";
    private static final String ACTION_DO_DELETE_WS = "do_delete_ws";

    private static final String PARAMETER_LONG_URL = "url";
    private static final String PARAMETER_KEY_URL = "key";
    private static final String PARAMETER_USE_ONCE = "use_once";

    private static final String PROPERTY_URL_IS_NOT_VALID = "shorturl.UrlIsNotValid";

    private static final String CODE_URL_IS_NOT = "URL_IS_NOT_VALID";

    private static final String TEMPLATE_CREATE_SHORTURL = "skin/plugins/shorturl/create_shorturl.html";
    private static final String MARK_RESULT = "result";

    // private fields
    private String _strUrlShort = null;
    private String _strUrlKey = null;

    /**
     * create short url
     * 
     * @param request
     *            the request
     * @return creation view
     */
    @Action( ACTION_DO_CREATE )
    public XPage doCreate( HttpServletRequest request )
    {
        String strUrl = request.getParameter( PARAMETER_LONG_URL );
        String strUseOnce = request.getParameter( PARAMETER_USE_ONCE );

        if ( !ShortUrlService.isValidShortUrl( strUrl ) )
        {

            addError( PROPERTY_URL_IS_NOT_VALID, getLocale( request ) );
        }

        boolean bUseOnce = !StringUtils.isEmpty( strUseOnce ) && strUseOnce.equals( "true" );
        _strUrlKey = ShortUrlService.createShortener( strUrl, bUseOnce );
        _strUrlShort = ShortUrlService.getServletUrl( _strUrlKey, request );

        return redirectView( request, VIEW_CREATE );

    }

    /**
     * create short url using rest
     * 
     * @param request
     *            the request
     * @return json response containing the url created
     */

    @Action( ACTION_DO_CREATE_WS )
    public XPage doCreateWs( HttpServletRequest request )
    {
        String strUrl = request.getParameter( PARAMETER_LONG_URL );
        String strUseOnce = request.getParameter( PARAMETER_USE_ONCE );

        if ( !ShortUrlService.isValidShortUrl( strUrl ) )
        {
            return responseJSON( JsonUtil.buildJsonResponse( new ErrorJsonResponse( CODE_URL_IS_NOT ) ) );
        }

        boolean bUseOnce = !StringUtils.isEmpty( strUseOnce ) && strUseOnce.equals( "true" );
        _strUrlKey = ShortUrlService.createShortener( strUrl, bUseOnce );
        _strUrlShort = ShortUrlService.getServletUrl( _strUrlKey, request );

        ShortUrlJsonResponse shortUrlJsonResponse = new ShortUrlJsonResponse( _strUrlShort, _strUrlKey );

        return responseJSON( JsonUtil.buildJsonResponse( shortUrlJsonResponse ) );

    }

    /**
     * delete short url using
     * 
     * @param request
     *            the request containing the url key to delete
     * @return a Json containig OK if the short url have been deleted
     */

    @Action( ACTION_DO_DELETE_WS )
    public XPage doDeleteWs( HttpServletRequest request )
    {
        String stKey = request.getParameter( PARAMETER_KEY_URL );
        ShortUrlService.deleteShortener( stKey );

        return responseJSON( JsonUtil.buildJsonResponse( new JsonResponse( "" ) ) );

    }

    /**
     * creation view
     * 
     * @param request
     *            request
     * @return creation view
     */

    @View( value = VIEW_CREATE, defaultView = true )
    public XPage createShortUrlContent( HttpServletRequest request )
    {

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_RESULT, _strUrlShort );

        return getXPage( TEMPLATE_CREATE_SHORTURL, request.getLocale( ), model );
    }

}
