package com.krakedev.inventarios.servicios;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.TipoDocumentoBDD;
import com.krakedev.inventarios.entidad.TipoDocumento;
import com.krakedev.inventarios.excepciones.KrakeDevException;


	@Path("tiposdocumento")
public class ServiciosTipoDocumento {
		
	@Path("recuperar")
	@GET // se utiliza para las solicitudes de lectura
	@Produces(MediaType.APPLICATION_JSON) //sirve para transforma a formato json, produce datos
	
	public Response obtenerTipoDocumentos(){
		
		TipoDocumentoBDD td=new TipoDocumentoBDD();
		ArrayList<TipoDocumento>tipoDocumentos=null;
		try {
			tipoDocumentos=td.recuperarTodos();
			return Response.ok(tipoDocumentos).build();
			
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	
	}
}
