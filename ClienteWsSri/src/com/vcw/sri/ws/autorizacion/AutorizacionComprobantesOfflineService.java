package com.vcw.sri.ws.autorizacion;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.3.3
 * 2020-11-03T09:13:31.889-05:00
 * Generated source version: 3.3.3
 *
 */
@WebServiceClient(name = "AutorizacionComprobantesOfflineService",
                  wsdlLocation = "https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl",
                  targetNamespace = "http://ec.gob.sri.ws.autorizacion")
public class AutorizacionComprobantesOfflineService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesOfflineService");
    public final static QName AutorizacionComprobantesOfflinePort = new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesOfflinePort");
    static {
        URL url = null;
        try {
            url = new URL("https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(AutorizacionComprobantesOfflineService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public AutorizacionComprobantesOfflineService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public AutorizacionComprobantesOfflineService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public AutorizacionComprobantesOfflineService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public AutorizacionComprobantesOfflineService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public AutorizacionComprobantesOfflineService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public AutorizacionComprobantesOfflineService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns AutorizacionComprobantesOffline
     */
    @WebEndpoint(name = "AutorizacionComprobantesOfflinePort")
    public AutorizacionComprobantesOffline getAutorizacionComprobantesOfflinePort() {
        return super.getPort(AutorizacionComprobantesOfflinePort, AutorizacionComprobantesOffline.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AutorizacionComprobantesOffline
     */
    @WebEndpoint(name = "AutorizacionComprobantesOfflinePort")
    public AutorizacionComprobantesOffline getAutorizacionComprobantesOfflinePort(WebServiceFeature... features) {
        return super.getPort(AutorizacionComprobantesOfflinePort, AutorizacionComprobantesOffline.class, features);
    }

}
