package com.vcw.falecpv.core.email;

import java.io.Serializable;

import com.vcw.falecpv.core.constante.EstadoEnvioEmailEnum;


public interface ActualizaNegocio extends Serializable {

	public void cambiarEstado(EstadoEnvioEmailEnum estado);

}
