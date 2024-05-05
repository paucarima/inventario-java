package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import com.krakedev.inventarios.entidad.DetallePedido;
import com.krakedev.inventarios.entidad.Pedido;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class PedidosBDD {
	public void insertar(Pedido pedido) throws KrakeDevException {
		Connection con = null;
		Date fechaActual=new Date();
		java.sql.Date fechaSQL=new java.sql.Date(fechaActual.getTime());
		int codigoCabecera=0;

		try {
			ResultSet rsClave=null;
			con = ConexionBDD.obtenerConexion();
			PreparedStatement ps = con.prepareStatement(
					"insert into cabecera_pedidos (proveedor_cp,fecha,estado_pedido_cp) "
					+ "values (?,?,?);", Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, pedido.getProveedor().getIdentificador());
			ps.setDate(2, fechaSQL);
			ps.setString(3, "S");
	
			ps.executeUpdate();
			
			rsClave=ps.getGeneratedKeys();
			
			if (rsClave.next()) {
				codigoCabecera=rsClave.getInt(1);
				//toma la primera columna
			}
			PreparedStatement psDet=null;
			ArrayList<DetallePedido>detallesPedido=pedido.getDetalles();
			DetallePedido det;
			for (int i = 0; i < detallesPedido.size(); i++) {
				det = detallesPedido.get(i);
				psDet=con.prepareStatement("insert into detalle_pedidos(cabecera_pedido_dp,producto_dp,cantidad_solicitada,subtotal,cantidad_recibida) "
						+ "values (?,?,?,?,?);");
				psDet.setInt(1, codigoCabecera);
				psDet.setInt(2, det.getProducto().getCodigo());
				psDet.setInt(3, det.getCantidadSolicitada());
				BigDecimal pv=det.getProducto().getPrecioVenta();
				BigDecimal cantidad=new BigDecimal(det.getCantidadSolicitada());
				BigDecimal subtotal=pv.multiply(cantidad);
				psDet.setBigDecimal(4, subtotal);
				psDet.setInt(5, 0);
				
				psDet.executeUpdate();
			}
			
		} catch (SQLException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new KrakeDevException("Error al insertar pedido. Detalle: " + e.getMessage());

		} catch (KrakeDevException e) {
			throw e;
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}
	
	public void actualizar(Pedido pedido) throws KrakeDevException {
		Connection con=null;
	

		try {
			con=ConexionBDD.obtenerConexion();

		
			con = ConexionBDD.obtenerConexion();
			PreparedStatement ps = con.prepareStatement(
					"update cabecera_pedidos "
					+ "set estado_pedido_cp='R' where numero=?;");
			
	
			ps.setInt(1, pedido.getCodigo());
	
			ps.executeUpdate();
			
			
			PreparedStatement psDet=null;
			ArrayList<DetallePedido>detallesPedido=pedido.getDetalles();
			DetallePedido det;
			for (int i = 0; i < detallesPedido.size(); i++) {
				det = detallesPedido.get(i);
				psDet=con.prepareStatement("update detalle_pedidos "
						+ "set cantidad_recibida=?, subtotal=? "
						+ "where codigo_dp=?");
				psDet.setInt(1, det.getCantidadRecibida());			
				BigDecimal pv=det.getProducto().getPrecioVenta();
				BigDecimal cantidad=new BigDecimal(det.getCantidadRecibida());
				BigDecimal subtotal=pv.multiply(cantidad);
				psDet.setBigDecimal(2, subtotal);
				psDet.setInt(3, det.getCodigo());
				
				psDet.executeUpdate();
			}
			
		} catch(KrakeDevException e) {
	
			e.printStackTrace();
			throw  new KrakeDevException("Error al actualizar pedido");
			
		}catch (SQLException e) {
	
			e.printStackTrace();
			throw  new KrakeDevException("Error al actualizar pedido");
			
		}
	
		
	}
}
