package com.falec.comprobantes.recibidos;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.falec.comprobantes.anticaptcha.Anticaptcha;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.JavascriptExecutor;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.concurrent.TimeUnit;
import java.io.File;

public class Chrome
{
	private static Logger LOGGER = Logger.getLogger(Chrome.class);
	private WebDriver driver;
	private WebDriverWait webdriverwait;
	private WebElement webelement;
	public static boolean ingresoSRI = false;
	
	public Chrome(String rutaChromeDriver)
	{
		System.setProperty("webdriver.chrome.driver", rutaChromeDriver);
		WebDriverManager.chromedriver().setup();
	}
	
	public void inicializarDriver()
	{
		ChromeOptions opciones = new ChromeOptions();
		opciones.addArguments("--no-sandbox"); // Confiar totalmente en el contenido que abrirá Chrome
		opciones.addArguments("--disable-dev-shm-usage"); // Evitar la caída del navegador por recursos limitados
		driver = new ChromeDriver(opciones);
		webdriverwait = new WebDriverWait(driver, 30); // Tiempo máximo de espera en segundos
	}
	
	public void iniciarSesion(String ruc, String cIAdicional, String clave)
	{
		try
		{
			// Navega al SRI
			driver.get("https://srienlinea.sri.gob.ec/sri-en-linea/contribuyente/perfil");
			TimeUnit.SECONDS.sleep(5);
			// Llena el RUC
			webdriverwait.until(ExpectedConditions.elementToBeClickable(By.id("usuario"))).sendKeys(ruc);
			// Llena la C.I. adicional si existe
			if(cIAdicional.compareTo("0") != 0)
			{
				webdriverwait.until(ExpectedConditions.elementToBeClickable(By.id("ciAdicional"))).sendKeys(cIAdicional);
			}
			// Llena la clave
			webdriverwait.until(ExpectedConditions.elementToBeClickable(By.id("password"))).sendKeys(clave);
			// Hace clic en el botón Ingresar
			webelement = webdriverwait.until(ExpectedConditions.elementToBeClickable(By.name("login")));
	        ((JavascriptExecutor)driver).executeScript("arguments[0].click()", webelement);
	        // Comprueba si ingresó al SRI
	        TimeUnit.SECONDS.sleep(5);
	        String direccionWeb = driver.getCurrentUrl();
	        if(direccionWeb.substring(0, 63).compareTo("https://srienlinea.sri.gob.ec/sri-en-linea/contribuyente/perfil") == 0) // Si es que ingresó al SRI
	        {
	        	ingresoSRI = true;
	        }
		}
		catch(Exception ex)
		{
			LOGGER.error("No se pudo ingresar al SRI");
		}
	}
	
	public void reintentarIniciarSesion(String ruc, String cIAdicional, String clave)
	{
		try
		{
			// Llena el RUC sin recargar la página del SRI
			webdriverwait.until(ExpectedConditions.elementToBeClickable(By.id("usuario"))).sendKeys(ruc);
			// Llena la C.I. adicional si existe
			if(cIAdicional.compareTo("0") != 0)
			{
				webdriverwait.until(ExpectedConditions.elementToBeClickable(By.id("ciAdicional"))).sendKeys(cIAdicional);
			}
			// Llena la clave
			webdriverwait.until(ExpectedConditions.elementToBeClickable(By.id("password"))).sendKeys(clave);
			// Hace clic en el botón Ingresar
			webelement = webdriverwait.until(ExpectedConditions.elementToBeClickable(By.name("login")));
	        ((JavascriptExecutor)driver).executeScript("arguments[0].click()", webelement);
	        // Comprueba si ingresó al SRI
	        TimeUnit.SECONDS.sleep(5);
	        String direccionWeb = driver.getCurrentUrl();
	        if(direccionWeb.substring(0, 63).compareTo("https://srienlinea.sri.gob.ec/sri-en-linea/contribuyente/perfil") == 0) // Si es que ingresó al SRI
	        {
	        	ingresoSRI = true;
	        }
		}
		catch(Exception ex)
		{
			LOGGER.error("No se pudo ingresar al SRI");
		}
	}
	
	public void irAComprobantesRecibidos()
	{
		try
		{
			TimeUnit.SECONDS.sleep(3);
			// Abre el menú lateral izquierdo
			webelement = webdriverwait.until(ExpectedConditions.elementToBeClickable(By.id("sri-menu")));
			((JavascriptExecutor)driver).executeScript("arguments[0].click()", webelement);
			// Despliega el menú "FACTURACIÓN ELECTRÓNICA"
			webelement = webdriverwait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(), 'FACTURACIÓN ELECTRÓNICA')]")));
            ((JavascriptExecutor)driver).executeScript("arguments[0].click()", webelement);
            // Hace clic en el submenú "Comprobantes electrónicos recibidos"
            webelement = webdriverwait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[contains(text(), 'Comprobantes electrónicos recibidos')]")));
            ((JavascriptExecutor)driver).executeScript("arguments[0].click()", webelement);
		}
		catch(Exception ex)
		{
			LOGGER.error("Error de navegación el la página del SRI");
		}
	}
	
	public void consultarReporte(String nombreMes, String anio)
	{
		try
		{
			// Clic en el ComboBox del año y selecciona el año
			webdriverwait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@name='frmPrincipal:ano']/option[text()='" + anio + "']"))).click();
			// Clic en el ComboBox del mes y selecciona el mes
			webdriverwait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@name='frmPrincipal:mes']/option[text()='" + nombreMes + "']"))).click();
			// Clic en el ComboBox del día y selecciona "Todos"
			webdriverwait.until(ExpectedConditions.elementToBeClickable(By.xpath("//select[@name='frmPrincipal:dia']/option[text()='Todos']"))).click();
		}
		catch(Exception ex)
		{
			LOGGER.error("Error al consultar el reporte");
		}
	}
	
	public boolean resolverCaptcha()
	{
		try
		{
			TimeUnit.SECONDS.sleep(1);
			String direccionActual = driver.getCurrentUrl();
			LOGGER.info("Dirección Captcha: " + direccionActual);
			String sitekey = webdriverwait.until(ExpectedConditions.elementToBeClickable(By.className("g-recaptcha"))).getAttribute("data-sitekey");
			LOGGER.info("Sitekey: " + sitekey);
			String respuesta = Anticaptcha.resolverNoCaptchaProxyless(direccionActual, sitekey);
			if(respuesta != null)
			{
				JavascriptExecutor js;
				if(driver instanceof JavascriptExecutor)
				{
                    js = (JavascriptExecutor) driver;
                    js.executeScript("document.getElementById(\"g-recaptcha-response\").innerHTML = \"" + respuesta + "\";");
                    TimeUnit.SECONDS.sleep(1);
                    js.executeScript("rcBuscar(\"" + respuesta + "\");");
                    return true;
                }
			}
		}
		catch(Exception ex)
		{
			LOGGER.error("Error al resolver el Captcha");
		}
		return false;
	}
	
	public boolean descargarReporte(String ruc, String rutaDescargaReporte)
	{
		try
		{
            TimeUnit.SECONDS.sleep(2);
            String mensajeCaptchaIncorrecta = null;
            try
            {
            	mensajeCaptchaIncorrecta = driver.findElement(By.className("ui-messages-warn-summary")).getText();
            }
            catch(Exception ex)
            {
            	LOGGER.info("No apareció el mensaje Captcha incorrecta");
            }
            if((mensajeCaptchaIncorrecta != null) && (mensajeCaptchaIncorrecta.compareTo("Captcha incorrecta")) == 0) // Captcha incorrecta
            {
                return false;
            }
            else // Captcha correcta
            {
                TimeUnit.SECONDS.sleep(3);
                webdriverwait.until(ExpectedConditions.elementToBeClickable(By.id("frmPrincipal:lnkTxtlistado"))).click();
                TimeUnit.SECONDS.sleep(10);
                String mensajeNoExistenDatos = null;
                try
                {
                	mensajeNoExistenDatos = driver.findElement(By.className("ui-messages-warn-summary")).getText();
                }
                catch(Exception ex)
                {
                	LOGGER.info("No apareció el mensaje No existen datos para los parámetros ingresados");
                }
                if((mensajeNoExistenDatos != null) && (mensajeNoExistenDatos.compareTo("No existen datos para los parámetros ingresados")) == 0) // No existen datos para los parámetros ingresados
                {
                    return true;
                }
                else // Sí existen datos para los parámetros ingresados
                {
                    // Mover el reporte descargado de Descargas a la ruta de descarga de reportes
                    File report = new File(System.getProperty("user.home") + java.nio.file.FileSystems.getDefault().getSeparator() + "Downloads" + java.nio.file.FileSystems.getDefault().getSeparator() + ruc + "_Recibidos.txt");
                    report.renameTo(new File(rutaDescargaReporte + java.nio.file.FileSystems.getDefault().getSeparator() + ruc + "_Recibidos.txt"));
                    report = new File(rutaDescargaReporte + java.nio.file.FileSystems.getDefault().getSeparator() + ruc + "_Recibidos.txt");
                    return report.exists();
                }
            }
        }
		catch(Exception ex)
		{
        	LOGGER.error("Error al descargar el reporte del SRI");
            return false;
        }
	}
	
	public void cerrarNavegador()
	{
        try
        {
            driver.close();
            driver.quit();
        }
        catch(Exception e)
        {
        }
    }
}