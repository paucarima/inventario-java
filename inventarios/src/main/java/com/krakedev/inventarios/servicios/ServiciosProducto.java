package com.krakedev.inventarios.servicios;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.ProductoBDD;
import com.krakedev.inventarios.entidad.Producto;
import com.krakedev.inventarios.excepciones.KrakeDevException;

@Path("productos")
public class ServiciosProducto {
	@Path("buscar/{sub}")
	@GET 
	@Produces(MediaType.APPLICATION_JSON) 
	
	public Response buscar(@PathParam("sub") String subcadena){

		ProductoBDD prodBDD=new ProductoBDD();
		ArrayList<Producto>productos=null;
		try {
			productos=prodBDD.buscar(subcadena);
			return Response.ok(productos).build();
			
		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	
	}
}
