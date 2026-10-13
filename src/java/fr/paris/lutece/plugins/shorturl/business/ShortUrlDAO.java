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

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * This class provides Data Access methods for ShortUrl objects
 */
@ApplicationScoped
public final class ShortUrlDAO implements IShortUrlDAO
{
    private static final String SQL_QUERY_SELECT = "SELECT id_shorturl, shorturl_url, abbreviation, creation_date, hits, use_once FROM shorturl WHERE id_shorturl = ?";
    private static final String SQL_QUERY_SELECT_BY_ABBRV = "SELECT id_shorturl, shorturl_url, abbreviation, creation_date, hits, use_once FROM shorturl WHERE abbreviation = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO shorturl ( shorturl_url, abbreviation, creation_date, hits , use_once) VALUES ( ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM shorturl WHERE id_shorturl = ? ";
    private static final String SQL_QUERY_DELETE_BY_ABBRV = "DELETE FROM shorturl WHERE abbreviation  = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE shorturl SET shorturl_url = ?, abbreviation = ?, creation_date = ?, hits = ? , use_once = ? WHERE id_shorturl = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_shorturl, shorturl_url, abbreviation, creation_date, hits, use_once FROM shorturl";

    /**
     * {@inheritDoc}
     */
    @Override
    public void insert( ShortUrl shortUrl, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, Statement.RETURN_GENERATED_KEYS, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, shortUrl.getShortenerUrl( ) );
            daoUtil.setString( nIndex++, shortUrl.getAbbreviation( ) );
            daoUtil.setTimestamp( nIndex++, shortUrl.getCreationDate( ) );
            daoUtil.setInt( nIndex++, shortUrl.getHits( ) );
            daoUtil.setBoolean( nIndex, shortUrl.isUseOnce( ) );
            daoUtil.executeUpdate( );

            if ( daoUtil.nextGeneratedKey( ) )
            {
                shortUrl.setIdShortener( daoUtil.getGeneratedKeyInt( 1 ) );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShortUrl load( int nId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nId );
            daoUtil.executeQuery( );

            return daoUtil.next( ) ? dataToObject( daoUtil ) : null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShortUrl load( String strKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ABBRV, plugin ) )
        {
            daoUtil.setString( 1, strKey );
            daoUtil.executeQuery( );

            return daoUtil.next( ) ? dataToObject( daoUtil ) : null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nShortenerId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nShortenerId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( String strKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_ABBRV, plugin ) )
        {
            daoUtil.setString( 1, strKey );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( ShortUrl shortUrl, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, shortUrl.getShortenerUrl( ) );
            daoUtil.setString( nIndex++, shortUrl.getAbbreviation( ) );
            daoUtil.setTimestamp( nIndex++, shortUrl.getCreationDate( ) );
            daoUtil.setInt( nIndex++, shortUrl.getHits( ) );
            daoUtil.setBoolean( nIndex++, shortUrl.isUseOnce( ) );
            daoUtil.setInt( nIndex, shortUrl.getIdShortener( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<ShortUrl> selectShortenersList( Plugin plugin )
    {
        Collection<ShortUrl> shortenerList = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                shortenerList.add( dataToObject( daoUtil ) );
            }
        }

        return shortenerList;
    }

    /**
     * Builds a short url from the current row.
     *
     * @param daoUtil
     *            the DAOUtil positioned on a row
     * @return the short url
     */
    private ShortUrl dataToObject( DAOUtil daoUtil )
    {
        ShortUrl shortUrl = new ShortUrl( );
        int nIndex = 1;
        shortUrl.setIdShortener( daoUtil.getInt( nIndex++ ) );
        shortUrl.setShortenerUrl( daoUtil.getString( nIndex++ ) );
        shortUrl.setAbbreviation( daoUtil.getString( nIndex++ ) );
        shortUrl.setCreationDate( daoUtil.getTimestamp( nIndex++ ) );
        shortUrl.setHits( daoUtil.getInt( nIndex++ ) );
        shortUrl.setUseOnce( daoUtil.getBoolean( nIndex ) );

        return shortUrl;
    }
}
