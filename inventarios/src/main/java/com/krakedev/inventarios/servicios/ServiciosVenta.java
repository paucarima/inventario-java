package com.krakedev.inventarios.servicios;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import com.krakedev.inventarios.bdd.VentasBDD;

import com.krakedev.inventarios.entidad.Venta;
import com.krakedev.inventarios.excepciones.KrakeDevException;

@Path("ventas")
public class ServiciosVenta {
	@Path("registrar")
	@POST// para las solicitudes de creación
	@Consumes(MediaType.APPLICATION_JSON)//acepta esto datos
	public Response insertar(Venta venta) {//response devolver error
		
	
		VentasBDD ventaBDD=new VentasBDD();
		try {
			ventaBDD.insertar(venta);
			return Response.ok().build();
		} catch (KrakeDevException e) {

			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("actualizar")
	@PUT //para las solicitudes de actualización.
	@Consumes(MediaType.APPLICATION_JSON)//acepta esto datos
	public Response actualizar(Venta venta) {
	
		VentasBDD ventaBDD=new VentasBDD();
		try {
			ventaBDD.actualizar(venta);
			return Response.ok().build();
		} catch (KrakeDevException e) {

			e.printStackTrace();
			return Response.serverError().build();
		}
		
		
	}
}
