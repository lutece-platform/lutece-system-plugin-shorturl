package fr.paris.lutece.plugins.shorturl.service;

import java.util.Random;

import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class ShortUrlKeyService extends AbstractKeyService
{

    private static final String PROPERTY_KEY_LENGTH = "shortUrl.keyLength";
    private static char [ ] _symbols;
    private final static Random random = new Random( );
    private static char [ ] _buf;

    /**
     * SecurityTokenService
     */
    private ShortUrlKeyService( )
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized  String generateNewKey( )
    {
        for ( int idx = 0; idx < _buf.length; ++idx )
            _buf [idx] = _symbols [random.nextInt( _symbols.length )];
        return new String( _buf );

    }

    @Override
    public void initService( )
    {
        _symbols = new char [ 36];

        for ( int idx = 0; idx < 10; ++idx )
            _symbols [idx] = (char) ( '0' + idx );
        for ( int idx = 10; idx < 36; ++idx )
            _symbols [idx] = (char) ( 'a' + idx - 10 );
        int nKeyLength = AppPropertiesService.getPropertyInt( PROPERTY_KEY_LENGTH, 4 );
        _buf = new char [ nKeyLength];

    }

}
