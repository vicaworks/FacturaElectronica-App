/**
 * 
 */
package com.vcw.falecpv.core.servicio.seg;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.servitec.common.dao.DaoGenerico;
import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.contadores.TCEmpresa;
import com.vcw.falecpv.core.dao.impl.SegperfilusuarioDao;
import com.vcw.falecpv.core.modelo.persistencia.Segperfil;
import com.vcw.falecpv.core.modelo.persistencia.Segperfilusuario;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.servicio.AppGenericService;
import com.vcw.falecpv.core.servicio.ContadorPkServicio;
import com.xpert.persistence.query.QueryBuilder;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class SegperfilusuarioServicio extends AppGenericService<Segperfilusuario, String> {

	@Inject
	private SegperfilusuarioDao segperfilusuarioDao;
	
	@Inject
	private ContadorPkServicio contadorPkServicio;
	
	
	/**
	 * 
	 */
	public SegperfilusuarioServicio() {
	}

	@Override
	public List<Segperfilusuario> consultarActivos() {
		return null;
	}

	@Override
	public List<Segperfilusuario> consultarInactivos() {
		return null;
	}

	@Override
	public DaoGenerico<Segperfilusuario, String> getDao() {
		return segperfilusuarioDao;
	}

	/**
	 * @return the segperfilusuarioDao
	 */
	public SegperfilusuarioDao getSegperfilusuarioDao() {
		return segperfilusuarioDao;
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idUsuario
	 * @param idsegsistema
	 * @return
	 * @throws DaoException
	 */
	public List<Segperfil> getPerfilByUsuario(String idUsuario,String idsegsistema)throws DaoException{
		try {
			
			QueryBuilder qb = new QueryBuilder(segperfilusuarioDao.getEntityManager());
			
			return qb.select("p.segperfil")
						.from(Segperfilusuario.class,"p")
						.equals("p.usuario.idusuario", idUsuario)
						.equals("p.estado","A")
						.equals("p.segperfil.estado","A")
						.equals("p.segperfil.segsistema.idsegsistema",idsegsistema).getResultList();
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idsegsistema
	 * @param usuario
	 * @param segperfilList
	 * @param idEstablecimiento
	 * @throws DaoException
	 */
	public void asignarPerfiles(String idsegsistema,Usuario usuario, List<Segperfil> segperfilList,String idEstablecimiento)throws DaoException{
		try {
			
			// 1 eliminar todos los perfiles asignados
			QueryBuilder qb = new QueryBuilder(segperfilusuarioDao.getEntityManager());
			
			List<String>  idsegperfilusuarioList = qb.select("p.idsegperfilusuario")
														.from(Segperfilusuario.class,"p")
														.equals("p.usuario.idusuario", usuario.getIdusuario())
														.equals("p.estado","A")
														.equals("p.segperfil.estado","A")
														.equals("p.segperfil.segsistema.idsegsistema",idsegsistema).getResultList();

			segperfilusuarioDao.deleteByIds(idsegperfilusuarioList);
			
			// 2 insertar datos
			
			for (Segperfil p : segperfilList.stream().filter(x->x.isSeleccion()).collect(Collectors.toList())) {
				
				Segperfilusuario pu = new Segperfilusuario();
				pu.setUsuario(usuario);
				pu.setSegperfil(p);
				pu.setEstado("A");
				pu.setIdsegperfilusuario(contadorPkServicio.generarContadorTabla(TCEmpresa.PERFIL_USUARIO, idEstablecimiento, true));
				crear(pu);
				
			}
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
