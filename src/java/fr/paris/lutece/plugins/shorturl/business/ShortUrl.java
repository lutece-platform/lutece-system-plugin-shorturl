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

import java.sql.Timestamp;

/**
 * This is the business class for the object ShortUrl
 */
public class ShortUrl
{
    // Variables declarations
    private int _nIdShortener;
    private String _strShortenerUrl;
    private String _strAbbreviation;
    private Timestamp _dCreationDate;
    private boolean _bUseOnce;
    private int _nHits;

    /**
     * Returns the IdShortener
     * 
     * @return The IdShortener
     */
    public int getIdShortener( )
    {
        return _nIdShortener;
    }

    /**
     * Sets the IdShortener
     * 
     * @param nIdShortener
     *            The IdShortener
     */
    public void setIdShortener( int nIdShortener )
    {
        _nIdShortener = nIdShortener;
    }

    /**
     * Returns the ShortenerUrl
     * 
     * @return The ShortenerUrl
     */
    public String getShortenerUrl( )
    {
        return _strShortenerUrl;
    }

    /**
     * Sets the ShortenerUrl
     * 
     * @param strShortenerUrl
     *            The ShortenerUrl
     */
    public void setShortenerUrl( String strShortenerUrl )
    {
        _strShortenerUrl = strShortenerUrl;
    }

    /**
     * Returns the Abbreviation
     * 
     * @return The Abbreviation
     */
    public String getAbbreviation( )
    {
        return _strAbbreviation;
    }

    /**
     * Sets the Abbreviation
     * 
     * @param strAbbreviation
     *            The Abbreviation
     */
    public void setAbbreviation( String strAbbreviation )
    {
        _strAbbreviation = strAbbreviation;
    }

    /**
     * Returns the CreationDate
     * 
     * @return The CreationDate
     */
    public Timestamp getCreationDate( )
    {
        return _dCreationDate;
    }

    /**
     * Sets the CreationDate
     * 
     * @param timestamp
     *            The CreationDate
     */
    public void setCreationDate( Timestamp timestamp )
    {
        _dCreationDate = timestamp;
    }

    /**
     * Returns the Hits
     * 
     * @return The Hits
     */
    public int getHits( )
    {
        return _nHits;
    }

    /**
     * Sets the Hits
     * 
     * @param nHits
     *            The Hits
     */
    public void setHits( int nHits )
    {
        _nHits = nHits;
    }

    public boolean isUseOnce( )
    {
        return _bUseOnce;
    }

    public void setUseOnce( boolean _bUseOnce )
    {
        this._bUseOnce = _bUseOnce;
    }
}
