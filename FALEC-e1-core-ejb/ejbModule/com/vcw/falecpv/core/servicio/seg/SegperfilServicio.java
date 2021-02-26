/**
 * 
 */
package com.vcw.falecpv.core.servicio.seg;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.dao.impl.SegperfilDao;
import com.vcw.falecpv.core.modelo.persistencia.Segopcion;
import com.vcw.falecpv.core.modelo.persistencia.Segperfil;
import com.vcw.falecpv.core.servicio.AppGenericService;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegperfilServicio extends AppGenericService<Segperfil, String> {

	@Inject
	private SegperfilDao segperfilDao;
	
	/**
	 * 
	 */
	public SegperfilServicio() {
	}

	@Override
	public List<Segperfil> consultarActivos() {
		return null;
	}

	@Override
	public List<Segperfil> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Segperfil, String> getDao() {
		return segperfilDao;
	}

	/**
	 * @return the segperfilDao
	 */
	public SegperfilDao getSegperfilDao() {
		return segperfilDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idUsuario
	 * @return
	 * @throws DaoException
	 */
	public List<Segopcion> getPerfilOpcionAcceso(String idUsuario,String idsegsistema)throws DaoException{
		
		try {
			
			String sql = "select " +
						"		distinct  " +
						"		op.idsegopcion, " +
						"		op.idsegtipoopcion, " +
						"		op.idsegsistema, " +
						"		sis.iniciales, " +
						"		op.idsegopcionpadre, " +
						"		op.identificador, " +
						"		op.nombre as opcion, " +
						"		op.url, " +
						"		op.nivel, " +
						"		op.orden, " +
						"		op.icono " +
						"	from  " +
						"		segopcion op inner join segtipoopcion opt on op.idsegtipoopcion = opt.idsegtipoopcion " + 
						"		inner join segsistema sis on sis.idsegsistema = op.idsegsistema  " +
						"		inner join segperfilopcion po on po.idsegopcion = op.idsegopcion  " +
						"		inner join segperfil pf on pf.idsegperfil = po.idsegperfil  " +
						"	where  " +
						"		po.idsegperfil in (select distinct pf.idsegperfil from segperfil pf inner join segperfilusuario pu on pu.idsegperfil=pf.idsegperfil where pu.idusuario='" + idUsuario + "' and pf.estado='A' and pu.estado='A') " +
						"		and op.estado = 'A' " +
						"		and po.estado = 'A' " +
						"		and pf.estado = 'A' " +
						"		and sis.idsegsistema = '" + idsegsistema + "' " +
						"	order by  " +
						"		op.idsegopcion ";
			
			return resultList(sql, Segopcion.class, false);
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
		
	}
	

}
