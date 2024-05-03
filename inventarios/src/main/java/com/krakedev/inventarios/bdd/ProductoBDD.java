package com.krakedev.inventarios.bdd;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.krakedev.inventarios.entidad.Categoria;
import com.krakedev.inventarios.entidad.Producto;
import com.krakedev.inventarios.entidad.UnidadDeMedida;
import com.krakedev.inventarios.excepciones.KrakeDevException;
import com.krakedev.inventarios.utils.ConexionBDD;

public class ProductoBDD {
	public ArrayList<Producto> buscar(String subcadena) throws KrakeDevException {
		ArrayList<Producto> productos = new ArrayList<Producto>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Producto producto = null;
		try {
			con = ConexionBDD.obtenerConexion();
			ps = con.prepareStatement(
					"select prod.codigo_pro, prod.nombre as nombre_producto, udm.codigo_udm as nombre_udm, udm.descripcion as descripcion_udm,"
							+ " cast (prod.precio_venta as decimal(6,2)), prod.tiene_iva,"
							+ " cast (prod.coste as decimal(5,4)),"
							+ " prod.categoria_pro as codigo_categoria, cat.nombre as nombre_categoria, stock"
							+ " from producto prod, unidades_medida udm, categorias cat"
							+ " where prod.unidad_medida_pro=udm.codigo_udm"
							+ " and prod.categoria_pro=cat.codigo_cat"
							+ " and upper(prod.nombre) like ?");
			ps.setString(1, "%" + subcadena.toUpperCase() + "%");

			rs = ps.executeQuery();

			while (rs.next()) {
				int codigo = rs.getInt("codigo_pro");
				String nombre = rs.getString("nombre_producto");
				String nombre_udm = rs.getString("nombre_udm");
				String descripcion_udm = rs.getString("descripcion_udm");
				BigDecimal precio_venta = rs.getBigDecimal("precio_venta");
				boolean tiene_iva = rs.getBoolean("tiene_iva");
				BigDecimal coste = rs.getBigDecimal("coste");
				int codigoCategoria = rs.getInt("codigo_categoria");
				String nombre_categoria = rs.getString("nombre_categoria");
				int stock = rs.getInt("stock");

				UnidadDeMedida udm = new UnidadDeMedida();
				udm.setNombre(nombre_udm);
				udm.setDescripcion(descripcion_udm);

				Categoria categoria = new Categoria();
				categoria.setCodigo(codigoCategoria);
				categoria.setNombre(nombre_categoria);

				producto = new Producto();
				producto.setCodigo(codigo);
				producto.setNombre(nombre);
				producto.setUnidadMedida(udm);
				producto.setPrecioVenta(precio_venta);
				producto.setTieneIVA(tiene_iva);
				producto.setCoste(coste);
				producto.setCategoria(categoria);
				producto.setStock(stock);

				productos.add(producto);

			}

		} catch (KrakeDevException e) {
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new KrakeDevException("Error al consultar . Detalle: " + e.getMessage());
		}
		return productos;
	}
	
	public void insertar(Producto producto) throws KrakeDevException {
		Connection con = null;

		try {
			con = ConexionBDD.obtenerConexion();
			PreparedStatement ps = con.prepareStatement(
					"insert into producto(nombre,unidad_medida_pro,precio_venta,tiene_iva,coste,categoria_pro,stock) "
					+ "values(?,?,?,?,?,?,?);");
			ps.setString(1, producto.getNombre());
			ps.setString(2, producto.getUnidadMedida().getNombre());
			ps.setBigDecimal(3, producto.getPrecioVenta());
			ps.setBoolean(4, producto.isTieneIVA());
			ps.setBigDecimal(5, producto.getCoste());
			ps.setInt(6, producto.getCategoria().getCodigo());
			ps.setInt(7, producto.getStock());
			ps.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new KrakeDevException("Error al insertar producto. Detalle: " + e.getMessage());

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
}
