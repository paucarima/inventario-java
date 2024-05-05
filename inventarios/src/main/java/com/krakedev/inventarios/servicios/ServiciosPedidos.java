package com.krakedev.inventarios.servicios;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.krakedev.inventarios.bdd.PedidosBDD;
import com.krakedev.inventarios.entidad.Pedido;
import com.krakedev.inventarios.excepciones.KrakeDevException;

@Path("pedidos")
public class ServiciosPedidos {
	@Path("registrar")
	@POST// para las solicitudes de creación
	@Consumes(MediaType.APPLICATION_JSON)//acepta esto datos
	public Response insertar(Pedido pedido) {//response devolver error
		
	
		PedidosBDD pedidoBDD=new PedidosBDD();
		try {
			pedidoBDD.insertar(pedido);
			return Response.ok().build();
		} catch (KrakeDevException e) {

			e.printStackTrace();
			return Response.serverError().build();
		}
	}
	
	@Path("recibir")
	@PUT //para las solicitudes de actualización.
	@Consumes(MediaType.APPLICATION_JSON)//acepta esto datos
	public Response actualizar(Pedido pedido) {
	
		PedidosBDD pedidoBDD=new PedidosBDD();
		try {
			pedidoBDD.actualizar(pedido);
			return Response.ok().build();
		} catch (KrakeDevException e) {

			e.printStackTrace();
			return Response.serverError().build();
		}
		
		
	}
}
