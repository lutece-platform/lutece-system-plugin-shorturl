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
            String strBaseUrl=AppPathService.getBaseUrl( request );
            if(!strBaseUrl.endsWith( "/" ))
            {
                strBaseUrl+="/";
            }
            strServletUrl =  strBaseUrl+"s/";
        }

        return strServletUrl.endsWith( "/" ) ? strServletUrl + strKey : strServletUrl + "/"+strKey;
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
        String strPatternUnAuthorizedUrl = AppPropertiesService.getProperty(PROPERTY_PATTERN_UNAUTHORIZED_URL );

        // test on domains url
        if ( !StringUtils.isEmpty( strAuthorizedDomains ) )
        {
            isValid = false;

            String [ ] tabAuthorizedDomains = strAuthorizedDomains.split( ",");

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
