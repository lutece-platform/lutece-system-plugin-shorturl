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
package fr.paris.lutece.plugins.shorturl.service;

import java.sql.Timestamp;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import fr.paris.lutece.plugins.shorturl.business.ShortUrl;
import fr.paris.lutece.plugins.shorturl.business.ShortUrlHome;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.http.AntPathMatcher;

/**
 * Creation, lookup and deletion of short urls.
 */
public final class ShortUrlService
{
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher( );
    private static final String PROPERTY_SHORTURL_FILTER = "shorturl.filterUrl";
    private static final String PROPERTY_AUTHORIZED_DOMAINS_URL = "shortUrl.authorizedDomainsShortUrl";
    private static final String PROPERTY_PATTERN_UNAUTHORIZED_URL = "shortUrl.unauthorizedDomainsShortUrl";
    private static final String PATH_FILTER = "s/";
    private static final String SLASH = "/";
    private static final String COMMA = ",";

    /**
     * Private constructor
     */
    private ShortUrlService( )
    {
    }

    /**
     * Creates a short url usable several times.
     *
     * @param strUrl
     *            the url to shorten
     * @return the key of the short url
     */
    public static String createShortener( String strUrl )
    {
        return createShortener( strUrl, false );
    }

    /**
     * Creates a short url.
     *
     * @param strUrl
     *            the url to shorten
     * @param bUseOnce
     *            true if the short url is deleted at its first use
     * @return the key of the short url
     */
    public static String createShortener( String strUrl, boolean bUseOnce )
    {
        ShortUrl shortened = new ShortUrl( );
        shortened.setShortenerUrl( strUrl );
        shortened.setAbbreviation( AbstractKeyService.getService( ).generateNewKey( ) );
        shortened.setCreationDate( new Timestamp( System.currentTimeMillis( ) ) );
        shortened.setUseOnce( bUseOnce );
        ShortUrlHome.create( shortened );

        return shortened.getAbbreviation( );
    }

    /**
     * Deletes a short url.
     *
     * @param strKey
     *            the key of the short url
     */
    public static void deleteShortener( String strKey )
    {
        ShortUrlHome.removeByKey( strKey );
    }

    /**
     * Returns a short url.
     *
     * @param strKey
     *            the key of the short url
     * @return the short url, or null when the key is unknown
     */
    public static ShortUrl getShortener( String strKey )
    {
        return ShortUrlHome.findByKey( strKey );
    }

    /**
     * Builds the public url of a short url: the configured filter url, else the base url of the request followed by s/.
     *
     * @param strKey
     *            the key of the short url
     * @param request
     *            the request
     * @return the public url
     */
    public static String getServletUrl( String strKey, HttpServletRequest request )
    {
        String strServletUrl = AppPropertiesService.getProperty( PROPERTY_SHORTURL_FILTER );

        if ( StringUtils.isEmpty( strServletUrl ) )
        {
            String strBaseUrl = AppPathService.getBaseUrl( request );

            if ( !strBaseUrl.endsWith( SLASH ) )
            {
                strBaseUrl += SLASH;
            }

            strServletUrl = strBaseUrl + PATH_FILTER;
        }

        return strServletUrl.endsWith( SLASH ) ? strServletUrl + strKey : strServletUrl + SLASH + strKey;
    }

    /**
     * Tests whether an url may be shortened: it matches one of the authorized domains, when some are configured, and not
     * the unauthorized pattern.
     *
     * @param strShortUrl
     *            the url to test
     * @return true if the url may be shortened
     */
    public static Boolean isValidShortUrl( String strShortUrl )
    {
        if ( strShortUrl == null )
        {
            return false;
        }

        boolean isValid = true;
        String strAuthorizedDomains = AppPropertiesService.getProperty( PROPERTY_AUTHORIZED_DOMAINS_URL );
        String strPatternUnAuthorizedUrl = AppPropertiesService.getProperty( PROPERTY_PATTERN_UNAUTHORIZED_URL );

        if ( !StringUtils.isEmpty( strAuthorizedDomains ) )
        {
            isValid = false;

            for ( String strAuthorizedDomain : strAuthorizedDomains.split( COMMA ) )
            {
                if ( PATH_MATCHER.match( strAuthorizedDomain, strShortUrl ) )
                {
                    isValid = true;

                    break;
                }
            }
        }

        if ( isValid && !StringUtils.isEmpty( strPatternUnAuthorizedUrl ) )
        {
            isValid = !strShortUrl.matches( strPatternUnAuthorizedUrl );
        }

        return isValid;
    }
}
