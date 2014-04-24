/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for ShortUrl objects
 */

public final class ShortUrlDAO implements IShortUrlDAO
{
	
	// Constants
	
	private static final String SQL_QUERY_NEW_PK = "SELECT max( id_shorturl ) FROM shorturl";
	private static final String SQL_QUERY_SELECT = "SELECT id_shorturl, shorturl_url, abbreviation, creation_date, hits FROM shorturl WHERE id_shorturl = ?";
	private static final String SQL_QUERY_SELECT_BY_ABBRV = "SELECT id_shorturl, shorturl_url, abbreviation, creation_date, hits FROM shorturl WHERE abbreviation = ?";
	
	private static final String SQL_QUERY_INSERT = "INSERT INTO shorturl ( id_shorturl, shorturl_url, abbreviation, creation_date, hits ) VALUES ( ?, ?, ?, ?, ? ) ";
	private static final String SQL_QUERY_DELETE = "DELETE FROM shorturl WHERE id_shorturl = ? ";
	private static final String SQL_QUERY_UPDATE = "UPDATE shorturl SET id_shorturl = ?, shorturl_url = ?, abbreviation = ?, creation_date = ?, hits = ? WHERE id_shorturl = ?";
	private static final String SQL_QUERY_SELECTALL = "SELECT id_shorturl, shorturl_url, abbreviation, creation_date, hits FROM shorturl";


	
	/**
	 * Generates a new primary key
         * @param plugin The Plugin
	 * @return The new primary key
	 */
    
	public int newPrimaryKey( Plugin plugin)
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK , plugin  );
		daoUtil.executeQuery();

		int nKey;

		if( !daoUtil.next() )
		{
			// if the table is empty
			nKey = 1;
		}

		nKey = daoUtil.getInt( 1 ) + 1;
		daoUtil.free();

		return nKey;
	}




	/**
	 * Insert a new record in the table.
	 * @param shorturl instance of the ShortUrl object to insert
         * @param plugin The plugin
	 */

	public void insert( ShortUrl shortUrl, Plugin plugin )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT , plugin );
                
		shortUrl.setIdShortener( newPrimaryKey( plugin ) );
                
                daoUtil.setInt ( 1, shortUrl.getIdShortener ( ) );
                daoUtil.setString ( 2, shortUrl.getShortenerUrl ( ) );
                daoUtil.setString ( 3, shortUrl.getAbbreviation ( ) );
                daoUtil.setTimestamp( 4, shortUrl.getCreationDate ( ) );
                daoUtil.setInt ( 5, shortUrl.getHits ( ) );

		daoUtil.executeUpdate();
		daoUtil.free();
	}


	/**
	 * Load the data of the shortener from the table
	 * @param nId The identifier of the shortener
         * @param plugin The plugin
	 * @return the instance of the ShortUrl
	 */


        public ShortUrl load( int nId, Plugin plugin )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT , plugin );
		daoUtil.setInt( 1 , nId );
		daoUtil.executeQuery();

		ShortUrl shortUrl = null;

		if ( daoUtil.next() )
		{
			shortUrl = new ShortUrl();

                        shortUrl.setIdShortener( daoUtil.getInt(  1 ) );
                        shortUrl.setShortenerUrl( daoUtil.getString(  2 ) );
                        shortUrl.setAbbreviation( daoUtil.getString(  3 ) );
                        shortUrl.setCreationDate( daoUtil.getTimestamp(  4 ) );
                        shortUrl.setHits( daoUtil.getInt(  5 ) );
		}

		daoUtil.free();
		return shortUrl;
	}


	/**
	 * Delete a record from the table
         * @param nShortenerId The identifier of the shortener
         * @param plugin The plugin
	 */

	public void delete( int nShortenerId, Plugin plugin )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE , plugin );
		daoUtil.setInt( 1 , nShortenerId );
		daoUtil.executeUpdate();
		daoUtil.free();
	}


	/**
	 * Update the record in the table
	 * @param shortUrl The reference of the shortener
         * @param plugin The plugin
	 */

	public void store( ShortUrl shortUrl, Plugin plugin )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE , plugin );
                
        daoUtil.setInt( 1, shortUrl.getIdShortener( ) );
        daoUtil.setString( 2, shortUrl.getShortenerUrl( ) );
        daoUtil.setString( 3, shortUrl.getAbbreviation( ) );
        daoUtil.setTimestamp( 4, shortUrl.getCreationDate( ) );
        daoUtil.setInt( 5, shortUrl.getHits( ) );
        daoUtil.setInt( 6, shortUrl.getIdShortener( ) );
                
		daoUtil.executeUpdate( );
		daoUtil.free( );
	}



	/**
	 * Load the data of all the shorteners and returns them as a collection
         * @param plugin The plugin
	 * @return The Collection which contains the data of all the shorteners
	 */

        public Collection<ShortUrl> selectShortenersList( Plugin plugin )
	{
		Collection<ShortUrl> shortenerList = new ArrayList<ShortUrl>(  );
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL , plugin );
		daoUtil.executeQuery(  );

		while ( daoUtil.next(  ) )
		{
                ShortUrl shortener = new ShortUrl(  );

                    shortener.setIdShortener( daoUtil.getInt( 1 ) );
                    shortener.setShortenerUrl( daoUtil.getString( 2 ) );
                    shortener.setAbbreviation( daoUtil.getString( 3 ) );
                    shortener.setCreationDate( daoUtil.getTimestamp( 4 ) );
                    shortener.setHits( daoUtil.getInt( 5 ) );

                shortenerList.add( shortener );
		}

		daoUtil.free();
		return shortenerList;
	}




	public ShortUrl load(String strKey, Plugin plugin) {
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ABBRV , plugin );
		daoUtil.setString( 1 , strKey );
		daoUtil.executeQuery();

		ShortUrl shortUrl = null;

		if ( daoUtil.next() )
		{
			shortUrl = new ShortUrl();

                        shortUrl.setIdShortener( daoUtil.getInt(  1 ) );
                        shortUrl.setShortenerUrl( daoUtil.getString(  2 ) );
                        shortUrl.setAbbreviation( daoUtil.getString(  3 ) );
                        shortUrl.setCreationDate( daoUtil.getTimestamp(  4 ) );
                        shortUrl.setHits( daoUtil.getInt(  5 ) );
		}

		daoUtil.free();
		return shortUrl;
	}

}
