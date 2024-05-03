package com.krakedev.inventarios.servicios;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

	public Response buscar(@PathParam("sub") String subcadena) {

		ProductoBDD prodBDD = new ProductoBDD();
		ArrayList<Producto> productos = null;
		try {
			productos = prodBDD.buscar(subcadena);
			return Response.ok(productos).build();

		} catch (KrakeDevException e) {
			e.printStackTrace();
			return Response.serverError().build();
		}

	}

	@Path("crear")
	@POST // para las solicitudes de creación
	@Consumes(MediaType.APPLICATION_JSON) // acepta esto datos
	public Response insertar(Producto producto) {// response devolver error

		ProductoBDD prodBDD = new ProductoBDD();
		try {
			prodBDD.insertar(producto);
			return Response.ok().build();
		} catch (KrakeDevException e) {

			e.printStackTrace();
			return Response.serverError().build();
		}
	}
}
