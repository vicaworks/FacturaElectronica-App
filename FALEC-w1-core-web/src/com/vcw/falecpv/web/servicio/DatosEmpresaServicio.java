/**
 * 
 */
package com.vcw.falecpv.web.servicio;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.servitec.common.dao.exception.DaoException;
import com.vcw.falecpv.core.constante.EstadoRegistroEnum;
import com.vcw.falecpv.core.constante.parametrosgenericos.PGDatosEnum;
import com.vcw.falecpv.core.modelo.persistencia.Empresa;
import com.vcw.falecpv.core.modelo.persistencia.Ice;
import com.vcw.falecpv.core.modelo.persistencia.Iva;
import com.vcw.falecpv.core.modelo.persistencia.Usuario;
import com.vcw.falecpv.core.servicio.EmpresaServicio;
import com.vcw.falecpv.core.servicio.IceServicio;
import com.vcw.falecpv.core.servicio.IvaServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio;
import com.vcw.falecpv.core.servicio.UsuarioServicio;
import com.vcw.falecpv.core.servicio.ParametroGenericoServicio.TipoRetornoParametroGenerico;

/**
 * @author cristianvillarreal
 *
 */
@Stateless
public class DatosEmpresaServicio {
	
	@Inject
	private ParametroGenericoServicio parametroGenericoServicio;
	
	@Inject
	private IvaServicio ivaServicio;
	
	@Inject
	private IceServicio iceServicio;
	
	@Inject
	private EmpresaServicio empresaServicio;
	
	@Inject
	private UsuarioServicio usuarioServicio;
	
	
	
	public void cargarDatosEmpresaFacade(String idEmpresa)throws DaoException{
		
		Empresa empresa = empresaServicio.consultarByPk(idEmpresa);
		
		if(empresa.getActualizardatos().toUpperCase().contains("S")) {
			
			// parametros genericos
			empresaServicio.parametrosGenericos(idEmpresa);
			
			// iva 
			cargarDatosEmpresaIva(idEmpresa);
			
			// ice
			
			cargarDatosEmpresaIce(idEmpresa);
			empresa.setActualizardatos("N");
			empresaServicio.actualizar(empresa);
		}
		
	}
	
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @throws DaoException
	 */
	public void cargarDatosEmpresaIva(String idEmpresa)throws DaoException{
		try {
			
			Empresa empresa = empresaServicio.consultarByPk(idEmpresa);
			empresaServicio.getEmpresaDao().getEntityManager().detach(empresa);
			Usuario usuario = usuarioServicio.getUsuarioDao().getByEmpresaEstado(EstadoRegistroEnum.TODOS, idEmpresa).get(0);
			usuarioServicio.getUsuarioDao().getEntityManager().detach(usuario);
			
			
			String path = parametroGenericoServicio.consultarParametro(PGDatosEnum.DATOS_IVA, TipoRetornoParametroGenerico.STRING);
			
			File template = new File(path);
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(template));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			Row row = null;
			Cell cell = null;
			
			Iterator<Row> rowIterator = sheet.iterator();
			inicio:while (rowIterator.hasNext()) {
				row = rowIterator.next();
				
				if(row.getRowNum()<1) {
					continue inicio;
				}
				
				Iva iva = new Iva();
				
				// codigo
				cell = row.getCell(0);
				cell.setCellType(CellType.STRING);
				iva.setCodigo(cell.getStringCellValue());
				
				
				//.cell porcentaje
				cell = row.getCell(1);
				cell.setCellType(CellType.STRING);
				iva.setPorcentaje(cell.getStringCellValue());
				
				//defecto
				cell = row.getCell(2);
				cell.setCellType(CellType.NUMERIC);
				iva.setDefecto((int)cell.getNumericCellValue());
				
				//valor
				cell = row.getCell(3);
				cell.setCellType(CellType.NUMERIC);
				iva.setValor(BigDecimal.valueOf(cell.getNumericCellValue()));
				
				//.codigo iva
				cell = row.getCell(4);
				cell.setCellType(CellType.STRING);
				iva.setCodigoIva(cell.getStringCellValue());
				
				//.label factura
				cell = row.getCell(5);
				cell.setCellType(CellType.STRING);
				iva.setLabelfactura(cell.getStringCellValue());
				
				//orden
				cell = row.getCell(6);
				cell.setCellType(CellType.NUMERIC);
				iva.setOrdenfactura((int)cell.getNumericCellValue());
				
				//.label factura
				cell = row.getCell(7);
				cell.setCellType(CellType.STRING);
				iva.setEstado(cell.getStringCellValue());
				
				iva.setEmpresa(empresa);
				iva.setUpdated(new Date());
				
				// consulta si existe 
				Iva ivaTemp = ivaServicio.getIvaDao().getIva(idEmpresa, iva.getCodigo());
				if(ivaTemp!=null) {
					ivaServicio.getIvaDao().getEntityManager().detach(ivaTemp);
				}
				if(ivaTemp!=null) {
					iva.setIdiva(ivaTemp.getIdiva());
					iva.setIdusuario(ivaTemp.getIdusuario());
					ivaServicio.actualizar(iva);
				}else {
					iva.setIdusuario(usuario.getIdusuario());
					ivaServicio.guardar(iva);
				}
				
			}
			
			
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
	/**
	 * @author cristianvillarreal
	 * 
	 * @param idEmpresa
	 * @throws DaoException
	 */
	public void cargarDatosEmpresaIce(String idEmpresa)throws DaoException{
		try {
			
			Empresa empresa = empresaServicio.consultarByPk(idEmpresa);
			empresaServicio.getEmpresaDao().getEntityManager().detach(empresa);
			Usuario usuario = usuarioServicio.getUsuarioDao().getByEmpresaEstado(EstadoRegistroEnum.TODOS, idEmpresa).get(0);
			usuarioServicio.getUsuarioDao().getEntityManager().detach(usuario);
			
			
			String path = parametroGenericoServicio.consultarParametro(PGDatosEnum.DATOS_ICE, TipoRetornoParametroGenerico.STRING);
			
			File template = new File(path);
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(template));
			XSSFSheet sheet = wb.getSheetAt(0);
			
			Row row = null;
			Cell cell = null;
			
			Iterator<Row> rowIterator = sheet.iterator();
			inicio:while (rowIterator.hasNext()) {
				row = rowIterator.next();
				
				if(row.getRowNum()<1) {
					continue inicio;
				}
				
				Ice ice = new Ice();
				
				// codigo
				cell = row.getCell(0);
				cell.setCellType(CellType.STRING);
				ice.setCodigo(cell.getStringCellValue());
				
				
				//.cell porcentaje
				cell = row.getCell(1);
				cell.setCellType(CellType.STRING);
				ice.setDescripcion(cell.getStringCellValue());
				
				//defecto
				cell = row.getCell(2);
				cell.setCellType(CellType.STRING);
				ice.setTarifaadvalorem(cell.getStringCellValue());
				
				//valor
				cell = row.getCell(3);
				cell.setCellType(CellType.NUMERIC);
				ice.setValor(BigDecimal.valueOf(cell.getNumericCellValue()));
				
				//.codigo iva
				cell = row.getCell(4);
				cell.setCellType(CellType.STRING);
				ice.setCodigoIce(cell.getStringCellValue());
				
				//.label factura
				cell = row.getCell(5);
				cell.setCellType(CellType.STRING);
				ice.setEstado(cell.getStringCellValue());
				
				ice.setEmpresa(empresa);
				ice.setUpdated(new Date());
				
				// consulta si existe 
				Ice iceTemp = iceServicio.getIceDao().getIce(idEmpresa, ice.getCodigo());
				if(iceTemp!=null) {
					iceServicio.getIceDao().getEntityManager().detach(iceTemp);
				}
				if(iceTemp!=null) {
					ice.setIdice(iceTemp.getIdice());
					ice.setIdusuario(iceTemp.getIdusuario());
					iceServicio.actualizar(ice);
				}else {
					ice.setIdusuario(usuario.getIdusuario());
					iceServicio.guardar(ice);
				}
				
			}
			
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
