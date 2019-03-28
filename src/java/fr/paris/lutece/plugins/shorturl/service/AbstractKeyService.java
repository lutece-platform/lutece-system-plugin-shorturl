package fr.paris.lutece.plugins.shorturl.service;

import fr.paris.lutece.portal.service.spring.SpringContextService;

public abstract class AbstractKeyService implements IKeyService
{
    private static IKeyService _singleton;
    /**
     * Returns the instance of the singleton
     *
     * @return The instance of the singleton
     */
    public static IKeyService getService(  )
    {
        if ( _singleton == null )
        {
           _singleton = SpringContextService.getBean(BEAN_KEY_SERVICE );
           _singleton.initService( );
        }

        return _singleton;
    }
}
