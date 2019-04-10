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
package fr.paris.lutece.plugins.shorturl.business;

import java.util.Collection;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * This class provides instances management methods (create, find, ...) for ShortUrl objects
 */

public final class ShortUrlHome
{

    // Static variable pointed at the DAO instance
    private static final String PLUGIN_NAME = "shorturl";
    private static IShortUrlDAO _dao = (IShortUrlDAO) SpringContextService.getBean( ShortUrlDAO.BEAN_NAME );
    private static Plugin _plugin = PluginService.getPlugin( PLUGIN_NAME );

    /**
     * Private constructor - this class need not be instantiated
     */

    private ShortUrlHome( )
    {
    }

    /**
     * Create an instance of the shortener class
     * 
     * @param shortener
     *            The instance of the ShortUrl which contains the informations to store
     * @param plugin
     *            the Plugin
     * @return The instance of shortener which has been created with its primary key.
     */

    public static ShortUrl create( ShortUrl shortener )
    {
        _dao.insert( shortener, _plugin );

        return shortener;
    }

    /**
     * Update of the shortener which is specified in parameter
     * 
     * @param shortener
     *            The instance of the ShortUrl which contains the data to store
     * @param plugin
     *            the Plugin
     * @return The instance of the shortener which has been updated
     */

    public static ShortUrl update( ShortUrl shortener )
    {
        _dao.store( shortener, _plugin );

        return shortener;
    }

    /**
     * Remove the shortener whose identifier is specified in parameter
     * 
     * @param plugin
     *            the Plugin
     */

    public static void remove( int nShortenerId )
    {
        _dao.delete( nShortenerId, _plugin );
    }

    /**
     * Remove the shortener whose identifier is specified in parameter
     * 
     * @param plugin
     *            the Plugin
     */

    public static void removeByKey( String strKey )
    {
        _dao.delete( strKey, _plugin );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a shortener whose identifier is specified in parameter
     * 
     * @param nKey
     *            The shortener primary key
     * @return an instance of ShortUrl
     */

    public static ShortUrl findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Returns an instance of a shortener whose identifier is specified in parameter
     * 
     * @param nKey
     *            The shortener primary key
     * @return an instance of ShortUrl
     */

    public static ShortUrl findByKey( String strKey )
    {
        return _dao.load( strKey, _plugin );
    }

    /**
     * Load the data of all the shortener objects and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the shortener objects
     */

    public static Collection<ShortUrl> getShortenersList( )
    {
        return _dao.selectShortenersList( _plugin );
    }

}
