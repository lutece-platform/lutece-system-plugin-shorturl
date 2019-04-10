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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import fr.paris.lutece.plugins.shorturl.business.ShortUrl;
import fr.paris.lutece.plugins.shorturl.business.ShortUrlHome;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class ShortUrlService
{

    private final static PathMatcher PATH_MATCHER = new AntPathMatcher( );
    private static final String PROPERTY_SHORTURL_FILTER = "shorturl.filterUrl";
    private static final String PROPERTY_AUTHORIZED_DOMAINS_URL = "shortUrl.authorizedDomainsShortUrl";
    private static final String PROPERTY_PATTERN_UNAUTHORIZED_URL = "shortUrl.unauthorizedDomainsShortUrl";

    public static String createShortener( String strUrl )
    {
        return createShortener( strUrl, false );
    }

    public static String createShortener( String strUrl, boolean bUseOnce )
    {

        ShortUrl shortened = new ShortUrl( );
        shortened.setShortenerUrl( strUrl );
        String strAbbreviation = AbstractKeyService.getService( ).generateNewKey( );
        shortened.setAbbreviation( strAbbreviation );
        shortened.setCreationDate( new java.sql.Timestamp( new java.util.Date( ).getTime( ) ) );
        shortened.setUseOnce( bUseOnce );
        ShortUrlHome.create( shortened );
        return shortened.getAbbreviation( );
    }

    public static void deleteShortener( String strKey )
    {
        ShortUrlHome.removeByKey( strKey );

    }

    public static ShortUrl getShortener( String strKey )
    {
        return ShortUrlHome.findByKey( strKey );

    }

    public static String getServletUrl( String strKey, HttpServletRequest request )
    {

        String strServletUrl = AppPropertiesService.getProperty( PROPERTY_SHORTURL_FILTER );
        if ( StringUtils.isEmpty( strServletUrl ) )
        {
            String strBaseUrl = AppPathService.getBaseUrl( request );
            if ( !strBaseUrl.endsWith( "/" ) )
            {
                strBaseUrl += "/";
            }
            strServletUrl = strBaseUrl + "s/";
        }

        return strServletUrl.endsWith( "/" ) ? strServletUrl + strKey : strServletUrl + "/" + strKey;
    }

    /**
     * Test if the BacUrl is a valid url
     * 
     * @param strBackUrl
     *            the back url to test
     * @return Test if the BackUrl is a valid url
     */
    public static Boolean isValidShortUrl( String strShortUrl )
    {
        boolean isValid = true;
        String strAuthorizedDomains = AppPropertiesService.getProperty( PROPERTY_AUTHORIZED_DOMAINS_URL );
        String strPatternUnAuthorizedUrl = AppPropertiesService.getProperty( PROPERTY_PATTERN_UNAUTHORIZED_URL );

        // test on domains url
        if ( !StringUtils.isEmpty( strAuthorizedDomains ) )
        {
            isValid = false;

            String [ ] tabAuthorizedDomains = strAuthorizedDomains.split( "," );

            for ( int i = 0; i < tabAuthorizedDomains.length; i++ )
            {
                if ( PATH_MATCHER.match( tabAuthorizedDomains [i], strShortUrl ) )
                {
                    isValid = true;

                    break;
                }
            }
        }

        // test on pattern url not authorized
        if ( isValid && !StringUtils.isEmpty( strPatternUnAuthorizedUrl ) )
        {
            isValid = !strShortUrl.matches( strPatternUnAuthorizedUrl );
        }

        return isValid;
    }

}
