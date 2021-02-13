/**
 * 
 */
package com.vcw.falecpv.core.servicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import com.servitec.common.dao.exception.DaoException;
import com.servitec.common.util.ValidarParametro;
import com.servitec.common.util.exceptions.ParametroRequeridoException;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.constante.contadores.TablaContadorBaseEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGEmpresaSucursal;
import com.vcw.falecpv.core.dao.DBUtilGenericoApp;
import com.vcw.falecpv.core.servicio.ParametroGenericoEmpresaServicio.TipoRetornoParametroGenerico;

/**
 * @author cristianvillarreal
 *
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class ContadorPkServicio extends DBUtilGenericoApp {

	
	@Inject
	private ParametroGenericoEmpresaServicio parametroGenericoEmpresaServicio;
	
	@Inject
	private EstablecimientoServicio establecimientoServicio;
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param tablaContador
	 * @param idEstablecimiento
	 * @return
	 * @throws DaoException
	 * @throws ParametroRequeridoException
	 */
	@Lock(LockType.WRITE)
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String generarContadorTabla(TablaContadorBaseEnum tablaContador,String idEstablecimiento,Object... generarSucursal)throws DaoException, ParametroRequeridoException{
		
		
		
		ValidarParametro.validar(tablaContador, "TABLA_CONTADOR");
//		ValidarParametro.validar(idEstablecimiento, "ESTABLECIMIENTO");
		
		boolean flagSucursal = false;
		String sucursal = null;
		
		// 1 verifica si el establecimiento tiene generacion sucursal
		if(idEstablecimiento!=null) {
			if(generarSucursal.length==0) {
				flagSucursal = parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.GENERAR_SUCURSAL, TipoRetornoParametroGenerico.BOOLEAN, idEstablecimiento);
			}else if((boolean)generarSucursal[0]==true){
				flagSucursal = parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.GENERAR_SUCURSAL, TipoRetornoParametroGenerico.BOOLEAN, idEstablecimiento);
			}else {
				flagSucursal = false;
			}
			
			if(flagSucursal) {
				// 2. consulta la sucursal
				sucursal = parametroGenericoEmpresaServicio.consultarParametroEstablecimiento(PGEmpresaSucursal.SUCURSAL, TipoRetornoParametroGenerico.STRING, idEstablecimiento);
			}
		}
		
		Connection cnn = null;
		PreparedStatement stm = null;
		ResultSet rs = null;
		
		String sql = "SELECT contador FROM contador WHERE nombreTabla=?";

		Long valor = null;
		try {
			
			cnn = getDataSource().getConnection();
			
			stm = cnn.prepareStatement(sql);
			stm.setString(1, tablaContador.getNombreTabla().trim());
			
			rs = stm.executeQuery();
			
			while (rs.next()) {
				valor = rs.getLong("contador");
				break;
			}
			
		} catch (Exception e) {
			throw new DaoException("Error consulta: ",e);
		} finally{
			try {
				rs.close();
				stm.close();
				cnn.close();
			} catch (Exception e2) {
			}
		}

		
		sql = "UPDATE contador SET contador=? WHERE nombreTabla=?";

		try {
			cnn = getDataSource().getConnection();
			valor +=1;
			
			stm = cnn.prepareStatement(sql);
			stm.setLong(1, valor);
			stm.setString(2, tablaContador.getNombreTabla());
			
			stm.executeUpdate();
			
		} catch (Exception e) {
			throw new DaoException("Error Actualizar : ",e);
		}finally{
			try {
				
				stm.close();
				cnn.close();
				
			} catch (Exception e2) {
			}
		}
		
		if (sucursal!=null){
			return  sucursal.concat(valor+"");
		}
		return valor + "";
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param geTipoDocumentoEnum
	 * @param idEStablecimiento
	 * @return
	 * @throws DaoException
	 */
//	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	@Lock(LockType.WRITE)
	public String generarNumeroDocumento(GenTipoDocumentoEnum geTipoDocumentoEnum,String idEStablecimiento)throws DaoException{
		return establecimientoServicio.generarNumeroDocumento(geTipoDocumentoEnum, idEStablecimiento);
	}
	
}
