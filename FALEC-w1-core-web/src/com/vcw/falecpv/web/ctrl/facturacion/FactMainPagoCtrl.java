/**
 * 
 */
package com.vcw.falecpv.web.ctrl.facturacion;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.AppConfiguracion;
import com.servitec.common.util.TextoUtil;
import com.vcw.falecpv.core.modelo.persistencia.Cabecera;
import com.vcw.falecpv.core.servicio.ClienteServicio;
import com.vcw.falecpv.web.common.BaseCtrl;
import com.vcw.falecpv.web.util.AppJsfUtil;

/**
 * @author cristianvillarreal
 *
 */
@Named
@SessionScoped
public class FactMainPagoCtrl extends BaseCtrl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3828995775819666807L;
	
	@EJB
	private ClienteServicio clienteServicio;
	
	
	private Cabecera cabecerSelected;
	private String criterioCliente;
	
	/**
	 * 
	 */
	public FactMainPagoCtrl() {
	}
	
	public void buscarConsumidorFinal() {
		try {
			if(cabecerSelected==null) return;
			
			consultarCliente("9999999999999");
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void buscarCliente() {
		try {
			
			if(cabecerSelected==null) return;
			
			if(criterioCliente==null || criterioCliente.trim().length()==0) {
				AppJsfUtil.addErrorMessage("formMain:inpCriterioCliente", "ERROR", "REQUERIDO");
				return;
			}
			
			consultarCliente(criterioCliente);
			
		} catch (Exception e) {
			e.printStackTrace();
			AppJsfUtil.addErrorMessage("formMain", "ERROR", TextoUtil.imprimirStackTrace(e, AppConfiguracion.getInteger("stacktrace.length")));
		}
	}
	
	public void consultarCliente(String identificador) throws DaoException {
		cabecerSelected.setCliente(null);
		cabecerSelected.setCliente(clienteServicio.getClienteDao().getByIdentificador(identificador,
				AppJsfUtil.getEstablecimiento().getEmpresa().getIdempresa()));
	}
	
	public void initPago(Cabecera cabeceraFac) {
		cabecerSelected = cabeceraFac;
		criterioCliente = null;
	}
	
	/**
	 * @return the cabecerSelected
	 */
	public Cabecera getCabecerSelected() {
		return cabecerSelected;
	}


	/**
	 * @param cabecerSelected the cabecerSelected to set
	 */
	public void setCabecerSelected(Cabecera cabecerSelected) {
		this.cabecerSelected = cabecerSelected;
	}

	/**
	 * @return the criterioCliente
	 */
	public String getCriterioCliente() {
		return criterioCliente;
	}

	/**
	 * @param criterioCliente the criterioCliente to set
	 */
	public void setCriterioCliente(String criterioCliente) {
		this.criterioCliente = criterioCliente;
	}

}
