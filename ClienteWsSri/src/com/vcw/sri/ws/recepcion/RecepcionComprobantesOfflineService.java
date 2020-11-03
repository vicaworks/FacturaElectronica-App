package com.vcw.sri.ws.recepcion;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.3.3
 * 2020-11-03T09:15:11.854-05:00
 * Generated source version: 3.3.3
 *
 */
@WebServiceClient(name = "RecepcionComprobantesOfflineService",
                  wsdlLocation = "https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl",
                  targetNamespace = "http://ec.gob.sri.ws.recepcion")
public class RecepcionComprobantesOfflineService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesOfflineService");
    public final static QName RecepcionComprobantesOfflinePort = new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesOfflinePort");
    static {
        URL url = null;
        try {
            url = new URL("https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(RecepcionComprobantesOfflineService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public RecepcionComprobantesOfflineService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public RecepcionComprobantesOfflineService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RecepcionComprobantesOfflineService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public RecepcionComprobantesOfflineService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public RecepcionComprobantesOfflineService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public RecepcionComprobantesOfflineService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns RecepcionComprobantesOffline
     */
    @WebEndpoint(name = "RecepcionComprobantesOfflinePort")
    public RecepcionComprobantesOffline getRecepcionComprobantesOfflinePort() {
        return super.getPort(RecepcionComprobantesOfflinePort, RecepcionComprobantesOffline.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RecepcionComprobantesOffline
     */
    @WebEndpoint(name = "RecepcionComprobantesOfflinePort")
    public RecepcionComprobantesOffline getRecepcionComprobantesOfflinePort(WebServiceFeature... features) {
        return super.getPort(RecepcionComprobantesOfflinePort, RecepcionComprobantesOffline.class, features);
    }

}
