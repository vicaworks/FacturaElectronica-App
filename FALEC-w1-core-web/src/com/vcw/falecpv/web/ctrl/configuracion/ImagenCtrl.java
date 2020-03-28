package com.vcw.falecpv.web.ctrl.configuracion;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import com.vcw.falecpv.core.modelo.persistencia.Establecimiento;
import com.vcw.falecpv.core.servicio.EstablecimientoServicio;

/**
 * @author isitk
 *
 */

@ManagedBean
@ApplicationScoped
public class ImagenCtrl implements Serializable {

	private static final long serialVersionUID = -8788719067123516137L;
	
	@EJB
	private EstablecimientoServicio establecimientoServicio;
	
	
	/**
	 * @return
	 * @throws IOException
	 */
	public StreamedContent getImage() throws IOException
	{
        FacesContext context = FacesContext.getCurrentInstance();

        if(context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE)
        {
            return new DefaultStreamedContent();
        }
        else
        {
            String id = context.getExternalContext().getRequestParameterMap().get("id");
            Establecimiento estab = establecimientoServicio.getDao().buscar(id);
            return new DefaultStreamedContent(new ByteArrayInputStream(estab.getLogo()));
        }
    }
}  