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

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.shorturl.business.ShortUrl;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * Tests of ShortUrlService.
 */
public class ShortUrlServiceTest extends LuteceTestCase
{
    private static final String URL_LOCALHOST = "http://localhost:8080/site/jsp/site/Portal.jsp?page=test";

    /**
     * Creates, reads and deletes a short url.
     */
    @Test
    public void testCreateFindDelete( )
    {
        String strKey = ShortUrlService.createShortener( URL_LOCALHOST, true );

        assertEquals( 8, strKey.length( ), "key length from shortUrl.keyLength" );

        ShortUrl shortUrl = ShortUrlService.getShortener( strKey );
        assertNotNull( shortUrl, "short url found by its key" );
        assertEquals( URL_LOCALHOST, shortUrl.getShortenerUrl( ), "target url" );
        assertTrue( shortUrl.isUseOnce( ), "use once flag" );

        ShortUrlService.deleteShortener( strKey );
        assertNull( ShortUrlService.getShortener( strKey ), "short url deleted" );
    }

    /**
     * Checks the authorized domains and the unauthorized pattern of the plugin properties.
     */
    @Test
    public void testIsValidShortUrl( )
    {
        assertTrue( ShortUrlService.isValidShortUrl( "http://localhost:8080" ), "localhost is authorized" );
        assertTrue( ShortUrlService.isValidShortUrl( "https://localhost" ), "https localhost is authorized" );
        assertFalse( ShortUrlService.isValidShortUrl( URL_LOCALHOST ), "the default pattern covers one path segment" );
        assertFalse( ShortUrlService.isValidShortUrl( "http://example.com" ), "other domain refused" );
        assertFalse( ShortUrlService.isValidShortUrl( "http://javascript.localhost" ), "script pattern refused" );
        assertFalse( ShortUrlService.isValidShortUrl( null ), "no url refused" );
    }

    /**
     * Checks the GUID key service.
     */
    @Test
    public void testGuidKeyService( )
    {
        assertEquals( 36, new GuidKeyService( ).generateNewKey( ).length( ), "GUID length" );
    }
}
