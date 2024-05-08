package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import com.krakedev.inventarios.entidad.DetalleVenta;
import com.krakedev.inventarios.entidad.Venta;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class VentasBDD {
	public void insertar(Venta venta) throws KrakeDevException {
		Connection con=null;
		Date fechaActual=new Date();
		Timestamp fechaHoraActual=new Timestamp(fechaActual.getTime());
		int codigoCabecera=0;
		
		try {
			ResultSet rsClave=null;
			con=ConexionBDD.obtenerConexion();
			PreparedStatement ps=con.prepareStatement("insert into cabecera_ventas(fecha,total_sin_iva,iva,total)"
					+ "values(?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
			
			ps.setTimestamp(1,fechaHoraActual);
			ps.setBigDecimal(2, BigDecimal.ZERO);
			ps.setBigDecimal(3, BigDecimal.ZERO);
			ps.setBigDecimal(4, BigDecimal.ZERO);
			ps.executeUpdate();
			
			rsClave=ps.getGeneratedKeys();
			if (rsClave.next()) {
				codigoCabecera=rsClave.getInt(1);
			}
			
			PreparedStatement psDet=null;
			ArrayList<DetalleVenta>detallesVenta=venta.getDetalles();
			DetalleVenta det;
			BigDecimal subtotalSinIva = BigDecimal.ZERO;
			BigDecimal totalConIva = BigDecimal.ZERO;
			BigDecimal resultadoIVA=BigDecimal.ZERO;
			BigDecimal resultadoNormal=BigDecimal.ZERO;
			
			for (int i = 0; i < detallesVenta.size(); i++) {
				det=detallesVenta.get(i);
				psDet=con.prepareStatement(
						"insert into detalle_ventas (cabecera_ventas_dv,producto_dv,cantidad,precio_venta,subtotal,con_iva) "
						+ "values (?,?,?,?,?,?);");
				psDet.setInt(1, codigoCabecera);
				psDet.setInt(2, det.getProducto().getCodigo());
				psDet.setInt(3, det.getCantidad());
				psDet.setBigDecimal(4, det.getProducto().getPrecioVenta());
				
				BigDecimal precioNormal=det.getProducto().getPrecioVenta();
				BigDecimal cantidad= new BigDecimal(det.getCantidad());
				resultadoNormal=cantidad.multiply(precioNormal);
				psDet.setBigDecimal(5, resultadoNormal);
				
				boolean valor=det.getProducto().isTieneIVA();
				if(valor==true) {
					
					BigDecimal IVA=new BigDecimal("1.12");
					BigDecimal precioxIVA=IVA.multiply(det.getProducto().getPrecioVenta()); 
					resultadoIVA=cantidad.multiply(precioxIVA);
					psDet.setBigDecimal(6, resultadoIVA);
				}else{
					BigDecimal IVA_no=new BigDecimal("1");
					BigDecimal precioxIVA=IVA_no.multiply(det.getProducto().getPrecioVenta()); 
					resultadoIVA=cantidad.multiply(precioxIVA);
					psDet.setBigDecimal(6, resultadoIVA);
				}
				psDet.executeUpdate();
				
				subtotalSinIva=subtotalSinIva.add(resultadoNormal);
				totalConIva=totalConIva.add(resultadoIVA);
			}
			
	
			BigDecimal total=totalConIva;
			BigDecimal iva=total.subtract(subtotalSinIva);
			
			ps = con.prepareStatement("update cabecera_ventas set total_sin_iva = ?, iva = ?, total = ? where codigo_cv = ?");
			ps.setBigDecimal(1, subtotalSinIva);
			ps.setBigDecimal(2, iva);
			ps.setBigDecimal(3, total);
			ps.setInt(4, codigoCabecera); 
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new KrakeDevException("Error al insertar venta. Detalle: " + e.getMessage());
		}catch (KrakeDevException e) {
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
	
	public void actualizar(Venta venta) throws KrakeDevException{
		Connection con=null;
		Date fechaActual=new Date();
		Timestamp fechaHoraActual=new Timestamp(fechaActual.getTime()); 
		
		try {
			con=ConexionBDD.obtenerConexion();
			PreparedStatement ps = con.prepareStatement(
					"UPDATE cabecera_ventas SET fecha = ?, total_sin_iva = ?, iva = ?, total = ? WHERE codigo_cv = ?");
			
			    ps.setTimestamp(1, fechaHoraActual);
			    
		        ps.setBigDecimal(2,BigDecimal.ZERO);
		        ps.setBigDecimal(3, BigDecimal.ZERO);
		        ps.setBigDecimal(4, BigDecimal.ZERO);
		        ps.setInt(5, venta.getCodigo());
				ps.executeUpdate();
			
				//eliminar
			PreparedStatement psDetEliminar = con.prepareStatement("DELETE FROM detalle_ventas WHERE cabecera_ventas_dv = ?");
			psDetEliminar.setInt(1, venta.getCodigo());
			psDetEliminar.executeUpdate();
			
			//insertar
			
			PreparedStatement psDet=null;
			PreparedStatement psHis=null;
			ArrayList<DetalleVenta>detallesVenta=venta.getDetalles();
			DetalleVenta det;
			BigDecimal subtotalSinIva = BigDecimal.ZERO;
			BigDecimal totalConIva = BigDecimal.ZERO;
			BigDecimal resultadoIVA=BigDecimal.ZERO;
			BigDecimal resultadoNormal=BigDecimal.ZERO;
			
			for (int i = 0; i < detallesVenta.size(); i++) {
				det=detallesVenta.get(i);
				psDet=con.prepareStatement(
						"insert into detalle_ventas (cabecera_ventas_dv,producto_dv,cantidad,precio_venta,subtotal,con_iva) "
						+ "values (?,?,?,?,?,?);");
				psDet.setInt(1, venta.getCodigo());
				psDet.setInt(2, det.getProducto().getCodigo());
				psDet.setInt(3, det.getCantidad());
				psDet.setBigDecimal(4, det.getProducto().getPrecioVenta());
				
				BigDecimal precioNormal=det.getProducto().getPrecioVenta();
				BigDecimal cantidad= new BigDecimal(det.getCantidad());
				resultadoNormal=cantidad.multiply(precioNormal);
				psDet.setBigDecimal(5, resultadoNormal);
				
				boolean valor=det.getProducto().isTieneIVA();
				if(valor==true) {
					
					BigDecimal IVA=new BigDecimal("1.12");
					BigDecimal precioxIVA=IVA.multiply(det.getProducto().getPrecioVenta()); 
					resultadoIVA=cantidad.multiply(precioxIVA);
					psDet.setBigDecimal(6, resultadoIVA);
				}else{
					BigDecimal IVA_no=new BigDecimal("1");
					BigDecimal precioxIVA=IVA_no.multiply(det.getProducto().getPrecioVenta()); 
					resultadoIVA=cantidad.multiply(precioxIVA);
					psDet.setBigDecimal(6, resultadoIVA);
				}
				psDet.executeUpdate();
				
				subtotalSinIva=subtotalSinIva.add(resultadoNormal);
				totalConIva=totalConIva.add(resultadoIVA);
				
				//historial de venta
				psHis=con.prepareStatement("insert into historial_stock(fecha,referencia_dp_hs,producto_hs,cantidad) "
						+ "values (?,?,?,?);");
				psHis.setTimestamp(1, fechaHoraActual);
				psHis.setString(2, "VENTA "+venta.getCodigo());
				psHis.setInt(3, det.getProducto().getCodigo());
				psHis.setInt(4, det.getCantidad()*-1);
				psHis.executeUpdate();
				//hostorial
			}

			BigDecimal total=totalConIva;
			BigDecimal iva=total.subtract(subtotalSinIva);
			
			ps = con.prepareStatement("update cabecera_ventas set total_sin_iva = ?, iva = ?, total = ? where codigo_cv = ?");
			ps.setBigDecimal(1, subtotalSinIva);
			ps.setBigDecimal(2, iva);
			ps.setBigDecimal(3, total);
			ps.setInt(4, venta.getCodigo()); 
			
			ps.executeUpdate();
			
				
		} catch(KrakeDevException e) {
	
			e.printStackTrace();
			throw  new KrakeDevException("Error al actualizar venta");
			
		}catch (SQLException e) {
	
			e.printStackTrace();
			throw  new KrakeDevException("Error al actualizar venta");
			
		}
	
	}
}
