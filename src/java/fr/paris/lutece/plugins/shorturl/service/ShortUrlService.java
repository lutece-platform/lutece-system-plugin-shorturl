package fr.paris.lutece.plugins.shorturl.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.shorturl.business.ShortUrl;
import fr.paris.lutece.plugins.shorturl.business.ShortUrlHome;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.string.StringUtil;

public class ShortUrlService
{

    private static final String PROPERTY_SHORTURL_FILTER = "shorturl.filterUrl";

    
    
    public static String createShortener( String strUrl )
    {
        return createShortener( strUrl, false);
    }

    
    public static String createShortener( String strUrl ,boolean bUseOnce)
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
            strServletUrl = AppPathService.getBaseUrl( request ) + "/s/";
        }

        return strServletUrl.endsWith( "/" ) ? strServletUrl + strKey : strServletUrl + "/"+strKey;
    }

}
