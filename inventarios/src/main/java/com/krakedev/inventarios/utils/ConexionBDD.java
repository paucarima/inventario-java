package com.krakedev.inventarios.utils;

import com.krakedev.inventarios.excepciones.KrakeDevException;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;




public class ConexionBDD {
	public static Connection obtenerConexion() throws KrakeDevException{
		Context ctx = null;// del apache
		DataSource ds = null;// componente bd
		Connection con = null;

		try {
			ctx = new InitialContext();// clase contexto
			// JDNI, buscar elementos
			ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/ConexionInventario");
			con = ds.getConnection();
			System.out.println("Conexion exitosa");
		} catch (NamingException | SQLException e) {

			e.printStackTrace();
			throw new KrakeDevException("Error de Conexion");
		}
		return con;

	}

}
