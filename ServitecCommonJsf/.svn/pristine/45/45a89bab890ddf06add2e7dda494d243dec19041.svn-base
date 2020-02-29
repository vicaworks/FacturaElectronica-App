/**
 * 
 */
package com.servitec.common.jsf;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.MethodExpressionActionListener;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author cvillarreal
 * 
 */
public class FacesUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -407448473563736652L;

	/**
	 * Devuelve el servlet context.
	 * 
	 * @return the servlet context
	 */
	public static ServletContext getServletContext() {
		return (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
	}

	/**
	 * Devuelve un context param variables del web.xml
	 * 
	 * @param contextParam
	 *            nombre del context param
	 * @return valor context param
	 */
	public static String getContextParam(String contextParam) {
		return getServletContext().getInitParameter(contextParam);
	}

	/**
	 * Devuelve external context
	 * 
	 * @return external context
	 */
	public static ExternalContext getExternalContext() {
		FacesContext fc = FacesContext.getCurrentInstance();
		return fc.getExternalContext();
	}

	/**
	 * Devuelve la sesi&oacute; si esta est&aacute; creado caso contrario null
	 * 
	 * @param create
	 * @return la sesion HTTP
	 */
	public static HttpSession getHttpSession(boolean create) {
		return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(create);
	}

	/**
	 * Devuelve un managed bean en base al nombre proporcionado.
	 * 
	 * @param beanName
	 *            el nombre del bean
	 * @return el managed bean asociado al nombre del bean
	 */
	public static Object getManagedBean(String beanName) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getApplication().getELResolver().getValue(context.getELContext(), null, beanName);
	}

	/**
	 * Devuelve el managed bean basado en el nombre del bean y un contexto
	 * espec&iacute;fico.<br/>
	 * Util cuando se quiere recuperar a trav&eacute;s de un componente basico
	 * como servlet o filter
	 * 
	 * @param beanName
	 * @param context
	 * @return el managed con el contexto ascociado
	 */
	public static Object getManagedBean(String beanName, FacesContext context) {

		return context.getApplication().getELResolver().getValue(context.getELContext(), null, beanName);
	}

	/**
	 * Remueve la instancia del managed bean asociado a su nombre
	 * 
	 * @param beanName
	 *            el nombre del bean a ser removido
	 */
	public static void resetManagedBean(String beanName) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getApplication().getELResolver().setValue(context.getELContext(), null, beanName, null);
	}

	/**
	 * Almacena el managed bean en el contexto de sesion
	 * 
	 * @param beanName
	 *            el nombre del bean que sera almacenado
	 * @param managedBean
	 *            el managed bean que sera almacenado
	 */
	public static void setManagedBeanInSession(String beanName, Object managedBean) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(beanName, managedBean);
	}

	/**
	 * Devuelve el valor de un par&aacute;metro de petici&oacute;n HTTP asociado
	 * a su nombre.
	 * 
	 * @param name
	 *            el nombre del parametro
	 * @return el valor del parametro
	 */
	public static String getRequestParameter(String name) {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
	}

	/**
	 * Agrega un mensaje de informac&iacute;on.
	 * 
	 * @param msg
	 *            el mensaje de informaci&oacute;n
	 */
	public static void addInfoMessage(String msg) {
		addInfoMessage(null, msg);
	}

	/**
	 * @author phidalgo
	 * 
	 * @param summary
	 * @param detail
	 * @return
	 */
	public static FacesMessage addInfoMessageFC(String summary, String detail) {
		return new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
	}

	/**
	 * @author phidalgo
	 * 
	 * @param summary
	 * @param detail
	 * @return
	 */
	public static FacesMessage addWarningMessageFC(String summary, String detail) {
		return new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail);
	}

	/**
	 * @author phidalgo
	 * 
	 * @param summary
	 * @param detail
	 * @return
	 */
	public static FacesMessage addErrorMessageFC(String summary, String detail) {
		return new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
	}

	/**
	 * Agrega un mensaje de informaci&oacute; para un componente
	 * espec&iacute;co.
	 * 
	 * @param clientId
	 *            el id del componente
	 * @param msg
	 *            el mensaje de informaci&oacute;n
	 */
	public static void addInfoMessage(String clientId, String msg) {
		FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
	}

	/**
	 * @param clientId
	 * @param summary
	 * @param detail
	 */
	public static void addInfoMessage(String clientId, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(clientId,
				new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
	}

	/**
	 * @param clientId
	 * @param summary
	 * @param detail
	 */
	public static void addWarningMessage(String clientId, String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(clientId,
				new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail));
	}

	/**
	 * Agrega un mensaje de error.
	 * 
	 * @param msg
	 *            el mensaje de error
	 */
	public static void addErrorMessage(String msg) {
		addErrorMessage(null, msg);
	}

	/**
	 * Agrega un mensaje de error para un componente espec&iacute;co;
	 * 
	 * @param clientId
	 *            el id del componente
	 * @param msg
	 *            el mensaje de error
	 */
	public static void addErrorMessage(String clientId, String msg) {
		FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
	}

	/**
	 * @param clientId
	 * @param sumary
	 * @param detalle
	 */
	public static void addErrorMessage(String clientId, String sumary, String detalle) {
		FacesContext.getCurrentInstance().addMessage(clientId,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, sumary, detalle));
	}

	/**
	 * Setea el valor de un objeto en una expresion.<br/>
	 * Es util cuando se quiere agregar desde un binding el valor de un objeto
	 * en una expresi&oacute;n
	 * 
	 * @param value
	 *            el valor a asignar
	 * @param expression
	 *            la expresion a evaluar
	 */
	public static void setValue2ValueExpression(final Object value, final String expression) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ELContext elContext = facesContext.getELContext();
		ValueExpression targetExpression = facesContext.getApplication().getExpressionFactory()
				.createValueExpression(elContext, expression, Object.class);
		targetExpression.setValue(elContext, value);
	}

	/**
	 * Agrega el valor de una expresion dentro de otra expresion que puede ser
	 * coleccion, array, o map.<br/>
	 * Ejem.<br/>
	 * <code>FacesUtil.mapVariable2ValueExpression("personBean", "#{personsBean.person['Dav']}");</code>
	 * 
	 * @param variable
	 *            el nombre de la variable que se quiere asignar
	 * @param expression
	 *            la expresion a la se asignar&aacute; el mencionado valor
	 */
	public static void mapVariable2ValueExpression(final String variable, final String expression) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ELContext elContext = facesContext.getELContext();
		ValueExpression targetExpression = facesContext.getApplication().getExpressionFactory()
				.createValueExpression(elContext, expression, Object.class);
		elContext.getVariableMapper().setVariable(variable, targetExpression);
	}

	/**
	 * Devuelve un methodExpresion &uacute;til si usamos composici&oacute;n
	 * v&iacute;a binding
	 * 
	 * @param valueExpression
	 *            el value expresion que define el m&eacute;todo
	 * @param expectedReturnType
	 *            type return
	 * @param expectedParamTypes
	 *            params types
	 * @return method expresion
	 */
	public static MethodExpression createMethodExpression(String valueExpression, Class<?> expectedReturnType,
			Class<?>[] expectedParamTypes) {
		MethodExpression methodExpression = null;
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExpressionFactory factory = facesContext.getApplication().getExpressionFactory();
			methodExpression = factory.createMethodExpression(facesContext.getELContext(), valueExpression,
					expectedReturnType, expectedParamTypes);
		} catch (Exception e) {
			throw new FacesException("Method expression '" + valueExpression + "' could not be created.", e);
		}
		return methodExpression;
	}

	/**
	 * Retorna un methodExpresionActionListener &uacute;til si usamos
	 * composici&oacute;n v&iacute;a binding
	 * 
	 * @param valueExpression
	 *            valueExpression que define el m&eacute;todo
	 * @param expectedReturnType
	 *            el tipo de retorno del m&eacute;
	 * @param expectedParamTypes
	 *            los tipos que definen los parametros
	 * @return MethodExpresionActionListener
	 */
	public static MethodExpressionActionListener createMethodActionListener(String valueExpression,
			Class<?> expectedReturnType, Class<?>[] expectedParamTypes) {
		MethodExpressionActionListener actionListener = null;
		try {
			actionListener = new MethodExpressionActionListener(
					createMethodExpression(valueExpression, expectedReturnType, expectedParamTypes));
		} catch (Exception e) {
			throw new FacesException(
					"Method expression for ActionListener '" + valueExpression + "' could not be created.");
		}
		return actionListener;
	}

	/**
	 * Se devuelve el componente en base a su id dentro de la vista
	 * 
	 * @param id
	 *            id del componente
	 * @return Componente
	 */
	public static UIComponent findComponent(String id) {
		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot viewRoot = context.getViewRoot();
		UIComponent component = viewRoot.findComponent(id);
		return component;
	}

	/**
	 * Buscamos un componente en base a su id dentro del contenedor
	 * 
	 * @param container
	 *            contenedor en donde se ira a buscar
	 * @param id
	 *            id del componente a buscar
	 * @return el componente encontrado
	 */
	public static UIComponent findComponent(final UIComponent container, final String id) {
		if (id.equals(container.getId())) {
			return container;
		}
		Iterator<UIComponent> kids = container.getFacetsAndChildren();
		while (kids.hasNext()) {
			UIComponent found = findComponent(kids.next(), id);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	/**
	 * Retorna el
	 * 
	 * @param context
	 * @return
	 */
	public static String getContextRoot(FacesContext context) {
		String ret = "http://";
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String remote = request.getLocalAddr();
		int port = request.getServerPort();
		return ret + remote + ":" + port + getContextPath(context);
	}

	/**
	 * Retorna la ruta del contexto de la app web
	 * 
	 * @param context
	 *            faces context asociado
	 * @return el path de la app web
	 */
	public static String getContextPath(FacesContext context) {
		return context.getExternalContext().getRequestContextPath();
	}

	/**
	 * Metodo que redirecciona a la url indicada
	 * 
	 * @param url
	 *            url de la vista a mostrar
	 */
	public static void redirect(String url) {
		FacesContext context = FacesContext.getCurrentInstance();

		ExternalContext extContext = context.getExternalContext();
		url = getContextPath(context).concat(url);
		try {

			extContext.redirect(url);
		} catch (IOException ioe) {
			throw new FacesException(ioe);

		}
	}

	/**
	 * @return
	 */
	public static HttpServletRequest getServletRequest() {
		return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}

	/**
	 * @return
	 */
	public static HttpServletResponse getServletResponse() {
		return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	}

	/**
	 * @return
	 */
	public static Boolean existErrors() {
		Severity maximunSeverity = FacesContext.getCurrentInstance().getMaximumSeverity();
		if (FacesMessage.SEVERITY_ERROR.equals(maximunSeverity)) {
			return true;
		}
		return false;

	}

	/**
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getRequestMapParameter(String name) {
		return (T) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(name);
	}

	/**
	 * @return
	 */
	public static String getRemoteUser() {
		return FacesUtil.getServletRequest().getRemoteUser();
	}

}
