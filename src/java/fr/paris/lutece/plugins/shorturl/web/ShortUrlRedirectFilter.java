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

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import fr.paris.lutece.plugins.shorturl.business.ShortUrl;
import fr.paris.lutece.plugins.shorturl.service.ShortUrlService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * Redirects a short url to its target.
 */
public class ShortUrlRedirectFilter implements Filter
{

    private static final String MESSAGE_SHORTURL_DOES_NOT_EXIST = "shorturl.messageShortUrlDoesNotExist";
    private static final String MESSAGE_SHORTURL_DOES_NOT_EXIST_TITLE = "shorturl.messageShortUrlDoesNotExist.title";
    private static final String DEFAUlT_ERROR_URL_REDIRECT = "shorturl.defaultErrorUrlRedirect";

    /**
     * {@inheritDoc}
     */
    @Override
    public void init( FilterConfig config ) throws ServletException
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy( )
    {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String strUri = req.getRequestURI( );
        String strKey = strUri.substring( strUri.lastIndexOf( '/' ) + 1 );
        ShortUrl shortUrl = ShortUrlService.getShortener( strKey );
        String strDestination = "";
        if ( shortUrl != null )
        {
            strDestination = shortUrl.getShortenerUrl( );
            if ( shortUrl.isUseOnce( ) )
            {
                ShortUrlService.deleteShortener( strKey );
            }
        }
        else
        {
            String strDefaultRedirectUrl = AppPropertiesService.getProperty( DEFAUlT_ERROR_URL_REDIRECT );
            try
            {
                SiteMessageService.setMessage( req, MESSAGE_SHORTURL_DOES_NOT_EXIST, null, MESSAGE_SHORTURL_DOES_NOT_EXIST_TITLE, strDefaultRedirectUrl, "",
                        SiteMessage.TYPE_STOP );
            }
            catch( SiteMessageException lme )
            {
                strDestination = AppPathService.getSiteMessageUrl( req );
            }
        }

        resp.sendRedirect( resp.encodeRedirectURL( strDestination ) );
        return;
    }
}
