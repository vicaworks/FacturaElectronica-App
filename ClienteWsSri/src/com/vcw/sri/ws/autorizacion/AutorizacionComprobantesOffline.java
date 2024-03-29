package com.vcw.sri.ws.autorizacion;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.3.3
 * 2020-11-03T09:13:31.855-05:00
 * Generated source version: 3.3.3
 *
 */
@WebService(targetNamespace = "http://ec.gob.sri.ws.autorizacion", name = "AutorizacionComprobantesOffline")
@XmlSeeAlso({ObjectFactory.class})
public interface AutorizacionComprobantesOffline {

    @WebMethod
    @RequestWrapper(localName = "autorizacionComprobante", targetNamespace = "http://ec.gob.sri.ws.autorizacion", className = "com.vcw.sri.ws.autorizacion.AutorizacionComprobante")
    @ResponseWrapper(localName = "autorizacionComprobanteResponse", targetNamespace = "http://ec.gob.sri.ws.autorizacion", className = "com.vcw.sri.ws.autorizacion.AutorizacionComprobanteResponse")
    @WebResult(name = "RespuestaAutorizacionComprobante", targetNamespace = "")
    public com.vcw.sri.ws.autorizacion.RespuestaComprobante autorizacionComprobante(

        @WebParam(name = "claveAccesoComprobante", targetNamespace = "")
        java.lang.String claveAccesoComprobante
    );

    @WebMethod
    @RequestWrapper(localName = "autorizacionComprobanteLote", targetNamespace = "http://ec.gob.sri.ws.autorizacion", className = "com.vcw.sri.ws.autorizacion.AutorizacionComprobanteLote")
    @ResponseWrapper(localName = "autorizacionComprobanteLoteResponse", targetNamespace = "http://ec.gob.sri.ws.autorizacion", className = "com.vcw.sri.ws.autorizacion.AutorizacionComprobanteLoteResponse")
    @WebResult(name = "RespuestaAutorizacionLote", targetNamespace = "")
    public com.vcw.sri.ws.autorizacion.RespuestaLote autorizacionComprobanteLote(

        @WebParam(name = "claveAccesoLote", targetNamespace = "")
        java.lang.String claveAccesoLote
    );
}
