/**
 * 
 */
package com.vcw.falecpv.core.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.GenTipoDocumentoEnum;
import com.vcw.falecpv.core.dao.AppGenericDao;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.modelo.persistencia.Tareacabecera;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class TareacabeceraDao extends AppGenericDao<Tareacabecera, String> {

	/**
	 * @param type
	 */
	public TareacabeceraDao() {
		super(Tareacabecera.class);
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @param idEstablecimiento
	 * @param estadoList
	 * @param prioridadList
	 * @param idUsuario
	 * @param idCliente
	 * @param numCotizacion
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public List<Tareacabecera> consultarByOpciones(String idEmpresa,
													String idEstablecimiento,
													List<String> estadoList,
													List<Integer> prioridadList,
													String idUsuario,
													List<String> idClienteList,
													String numCotizacion,
													GenTipoDocumentoEnum genTipoDocumentoEnum)throws DaoException{
		try {
			QueryBuilder qb = new QueryBuilder(getEntityManager());
			
			// 1 definirEstablecimientos
			List<String> idEstablecimientoList = new ArrayList<>();
			if(idEstablecimiento!=null) {
				idEstablecimientoList.add(idEstablecimiento);
			}else {
				idEstablecimientoList.addAll(
				qb.select("e.idestablecimiento")
					.from(Establecimiento.class,"e")
					.equals("e.empresa.idempresa", idEmpresa).getResultList());
			}
			
			// 2. usuario
			List<String> idUsuarioList = new ArrayList<>();
			if(idUsuario!=null) {
				idUsuarioList.add(idUsuario);
			}else {
				idUsuarioList.addAll(
				qb.select("u.idusuario")
					.from(Usuario.class,"u")
					.equals("u.establecimiento.empresa.idempresa", idEmpresa).getResultList());
			}
			
			Query q = null;
			
			if(numCotizacion==null) {
				
				q= getEntityManager().createQuery("SELECT t FROM Tareacabecera t " +
						" WHERE t.cabecera.establecimiento.idestablecimiento in (:establecimeintoList) " +
						" AND t.cabecera.idusuario in (:usuarioList) " +
						" AND t.cabecera.cliente.idcliente in (:clienteList) " +
						" AND t.cabecera.tipocomprobante.identificador=:tipocomprobante " +
						(estadoList==null?" ":" AND t.estado in (:estadoList) ") +
						(prioridadList==null?" ":" AND t.prioridadvalor in (:prioridadList) ") + 
						" ORDER BY t.prioridadvalor,t.fechalimite");
				
				if(idClienteList==null || idClienteList.isEmpty()) {
					idClienteList = new ArrayList<>();
					idClienteList.add("-1");
				}
				
				q.setParameter("usuarioList", idUsuarioList);
				q.setParameter("clienteList", idClienteList);
				
				if(estadoList!=null) {
					q.setParameter("estadoList", estadoList);
				}
				if(prioridadList!=null) {
					q.setParameter("prioridadList", prioridadList);
				}
			}else {
				q= getEntityManager().createQuery("SELECT t FROM Tareacabecera t " +
						" WHERE t.cabecera.establecimiento.idestablecimiento in (:establecimeintoList) " +
						" AND t.cabecera.tipocomprobante.identificador=:tipocomprobante " +
						" AND t.cabecera.numdocumento like (:numdocumento) " +
						" ORDER BY t.prioridadvalor,t.fechalimite");
				
				q.setParameter("numdocumento", "%" + numCotizacion.replace("-", "").concat("%"));
			}
			
			q.setParameter("establecimeintoList", idEstablecimientoList);
			q.setParameter("tipocomprobante", genTipoDocumentoEnum.getIdentificador());
			
			return q.getResultList();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @return
	 * @throws DaoException
	 */
	public int actualizarEstadoVencido(String idEmpresa)throws DaoException{
		try {
			
			Query q = getEntityManager().createNativeQuery("UPDATE tareacabecera "
					+ " SET estado='VENCIDO' "
					+ " WHERE idcabecera in (select distinct idcabecera from cabecera c inner join establecimiento e2 on c.idestablecimiento = e2.idestablecimiento where idempresa = :idempresa and idtipocomprobante = '10' and c.estado <> 'ARCHIVADO') "
					+ " and estado='PENDIENTE' "
					+ " and fechalimite<:fecha");
			q.setParameter("idempresa", idEmpresa);
			q.setParameter("fecha", new Date());
			return q.executeUpdate();
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
