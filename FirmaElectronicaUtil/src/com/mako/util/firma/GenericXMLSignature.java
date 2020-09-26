/**
 * 
 */
package com.mako.util.firma;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.FirmaXML;

/**
 * @author Jorge
 *
 */
public abstract class GenericXMLSignature
{
	private String pathSignature;
    private String passSignature;

    public String getPathSignature()
    {
        return this.pathSignature;
    }

    public void setPathSignature(String pathSignature)
    {
        this.pathSignature = pathSignature;
    }

    public String getPassSignature()
    {
        return this.passSignature;
    }

    public void setPassSignature(String passSignature)
    {
        this.passSignature = passSignature;
    }

    /**
     * @throws CertificateException
     * @throws FirmaElectronicaException
     */
    protected void execute() throws CertificateException,FirmaElectronicaException
    {
        KeyStore keyStore = getKeyStore();
        if(keyStore == null)
        {
        	throw new FirmaElectronicaException("No se pudo obtener el archivo de firma electronica");
        }
        String alias = getAlias(keyStore);

        X509Certificate certificate = null;
        try
        {
            certificate = (X509Certificate) keyStore.getCertificate(alias);
            if(certificate == null)
            {
            	throw new FirmaElectronicaException("No existe ningun certifcado para firmar");
            }
        }
        catch(KeyStoreException e1)
        {
        	throw new FirmaElectronicaException(e1);
        }
        PrivateKey privateKey = null;
        KeyStore tmpKs = keyStore;
        try
        {
            privateKey = (PrivateKey) tmpKs.getKey(alias, this.passSignature.toCharArray());
        }
        catch(UnrecoverableKeyException e)
        {
        	e.printStackTrace();
            throw new FirmaElectronicaException("No existe clave privada para firmar");
        }
        catch(KeyStoreException e)
        {
        	e.printStackTrace();
            throw new FirmaElectronicaException("No existe clave privada para firmar");
        }
        catch(NoSuchAlgorithmException e)
        {
        	e.printStackTrace();
            throw new FirmaElectronicaException("No existe clave privada para firmar");
        }
        Provider provider = keyStore.getProvider();

        DataToSign dataToSign = createDataToSign();

        FirmaXML firma = new FirmaXML();

        Document docSigned = null;
        try
        {
            Object[] res = firma.signFile(certificate, dataToSign, privateKey, provider);
            docSigned = (Document) res[0];
        }
        catch(Exception ex)
        {
        	throw new FirmaElectronicaException("Error realizando la firma: " ,ex);
        }
        String filePath = getPathOut() + File.separatorChar + getSignatureFileName();
        System.out.println("Firma guardada en: " + filePath);

        saveDocumenteDisk(docSigned, filePath);
    }
    
    
    /**
     * @author cristianvillarreal
     * @param xmlToSign
     * @param penSignature
     * @return
     * @throws CertificateException
     */
    protected byte[] execute(byte[] xmlToSign,byte[] penSignature) throws CertificateException,FirmaElectronicaException{
    	
    	// 1 keystore a partir del archivo de firma digital
    	KeyStore keyStore = getKeyStore(penSignature);
    	
    	if(keyStore == null)
        {
    		throw new FirmaElectronicaException("No se pudo obtener el archivo de firma electronica");
        }
    	
    	String alias = getAlias(keyStore);
    	
    	X509Certificate certificate = null;
        try
        {
            certificate = (X509Certificate) keyStore.getCertificate(alias);
            if(certificate == null)
            {
            	throw new FirmaElectronicaException("No existe ningun certifcado para firmar");
            }
        }
        catch(KeyStoreException e1)
        {
        	throw new FirmaElectronicaException(e1);
        }
    	
        PrivateKey privateKey = null;
        KeyStore tmpKs = keyStore;
        
        try
        {
            privateKey = (PrivateKey) tmpKs.getKey(alias, this.passSignature.toCharArray());
        }
        catch(UnrecoverableKeyException e)
        {
        	e.printStackTrace();
            throw new FirmaElectronicaException("No existe clave privada para firmar");
        }
        catch(KeyStoreException e)
        {
        	e.printStackTrace();
            throw new FirmaElectronicaException("No existe clave privada para firmar");
        }
        catch(NoSuchAlgorithmException e)
        {
        	e.printStackTrace();
            throw new FirmaElectronicaException("No existe clave privada para firmar");
        }
        
        Provider provider = keyStore.getProvider();
        
        DataToSign dataToSign = createDataToSign(xmlToSign);
        
        FirmaXML firma = new FirmaXML();

        Document docSigned = null;
        try
        {
            Object[] res = firma.signFile(certificate, dataToSign, privateKey, provider);
            docSigned = (Document) res[0];
        }
        catch(Exception ex)
        {
        	throw new FirmaElectronicaException("Error realizando la firma: " ,ex);
        }
        
    	return documenteToByte(docSigned);
    	
    }

    /**
     * @return
     * @throws FirmaElectronicaException
     */
    protected abstract DataToSign createDataToSign() throws FirmaElectronicaException;
    /**
     * @param xmlToSign
     * @return
     * @throws FirmaElectronicaException
     */
    protected abstract DataToSign createDataToSign(byte[] xmlToSign) throws FirmaElectronicaException;

    /**
     * @return
     */
    protected abstract String getSignatureFileName();

    /**
     * @return
     */
    protected abstract String getPathOut();

    /**
     * @param resource
     * @return
     * @throws FirmaElectronicaException
     */
    protected Document getDocument(String resource)throws FirmaElectronicaException
    {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        File file = new File(resource);
        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(file);
        }
        catch(ParserConfigurationException | SAXException | IOException | IllegalArgumentException ex)
        {
            throw new FirmaElectronicaException(ex);
        }
        return doc;
    }
    
    /**
     * @author cristianvillarreal
     * 
     * @param xmlToSign
     * @return
     * @throws FirmaElectronicaException 
     */
    protected Document getDocument(byte[] xmlToSign) throws FirmaElectronicaException
    {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new ByteArrayInputStream(xmlToSign));
        }
        catch(ParserConfigurationException | SAXException | IOException | IllegalArgumentException ex)
        {
        	throw new FirmaElectronicaException(ex);
        }
        return doc;
    }

    /**
     * @return
     * @throws CertificateException
     * @throws FirmaElectronicaException
     */
    private KeyStore getKeyStore() throws CertificateException,FirmaElectronicaException
    {
        KeyStore ks = null;
        try
        {
            ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(this.pathSignature), this.passSignature.toCharArray());
        }
        catch(KeyStoreException e)
        {
        	throw new FirmaElectronicaException(e);
        }
        catch(NoSuchAlgorithmException e)
        {
        	throw new FirmaElectronicaException(e);
        }
        catch(CertificateException e)
        {
        	throw new FirmaElectronicaException(e);
        }
        catch(IOException e)
        {
        	throw new FirmaElectronicaException(e);
        }
        return ks;
    }
    
    /**
     * @author cristianvillarreal
     * 
     * @param penSignature
     * @return
     * @throws CertificateException
     */
    private KeyStore getKeyStore(byte[] penSignature) throws CertificateException,FirmaElectronicaException
    {
        KeyStore ks = null;
        try
        {
            ks = KeyStore.getInstance("PKCS12");
            ks.load(new ByteArrayInputStream(penSignature),this.passSignature.toCharArray());
        }
        catch(KeyStoreException e)
        {
        	throw new FirmaElectronicaException(e);
        }
        catch(NoSuchAlgorithmException e)
        {
        	throw new FirmaElectronicaException(e);
        }
        catch(CertificateException e)
        {
        	throw new FirmaElectronicaException(e);
        }
        catch(IOException e)
        {
        	throw new FirmaElectronicaException(e);
        }
        return ks;
    }

    /**
     * @param keyStore
     * @return
     * @throws FirmaElectronicaException
     */
    private static String getAlias(KeyStore keyStore)throws FirmaElectronicaException
    {
        String alias = null;
        try
        {
            @SuppressWarnings("rawtypes")
			Enumeration nombres = keyStore.aliases();
            while(nombres.hasMoreElements())
            {
                String tmpAlias = (String) nombres.nextElement();
                if(keyStore.isKeyEntry(tmpAlias))
                {
                    alias = tmpAlias;
                }
            }
        }
        catch(KeyStoreException e)
        {
        	throw new FirmaElectronicaException(e);
        }
        return alias;
    }

    /**
     * @author cristianvillarreal
     * @param document
     * @param pathXml
     * @throws FirmaElectronicaException
     */
    public static void saveDocumenteDisk(Document document, String pathXml)throws FirmaElectronicaException
    {
        try
        {
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(pathXml));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(source, result);
        }
        catch(TransformerConfigurationException e)
        {
        	throw new FirmaElectronicaException(e);
        }
        catch(TransformerException e)
        {
        	throw new FirmaElectronicaException(e);
        }
    }
    
    /**
     * @author cristianvillarreal
     * 
     * @param document
     * @return
     * @throws FirmaElectronicaException
     */
    public static byte[] documenteToByte(Document document)throws FirmaElectronicaException
    {
    	ByteArrayOutputStream arr = new ByteArrayOutputStream();
        try
        {
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(arr);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(source, result);
            
        }
        catch(TransformerConfigurationException e)
        {
        	throw new FirmaElectronicaException(e);
        }
        catch(TransformerException e)
        {
        	throw new FirmaElectronicaException(e);
        }
        return arr.toByteArray();
    }
}