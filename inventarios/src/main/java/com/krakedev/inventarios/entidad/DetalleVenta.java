package com.krakedev.inventarios.entidad;

import java.math.BigDecimal;

public class DetalleVenta {

	private int codigo;
	private Venta venta;
	private Producto producto;
	private int cantidad;
	private BigDecimal precio;
	private BigDecimal subtotal;
	private BigDecimal subtotal_con_iva;
	
	
	public DetalleVenta(int codigo, Venta venta, Producto producto, int cantidad, BigDecimal precio,
			BigDecimal subtotal, BigDecimal subtotal_con_iva) {
		super();
		this.codigo = codigo;
		this.venta = venta;
		this.producto = producto;
		this.cantidad = cantidad;
		this.precio = precio;
		this.subtotal = subtotal;
		this.subtotal_con_iva = subtotal_con_iva;
	}
	
	
	public DetalleVenta() {
		super();
	}


	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public Venta getVenta() {
		return venta;
	}
	public void setVenta(Venta venta) {
		this.venta = venta;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public BigDecimal getPrecio() {
		return precio;
	}
	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}
	public BigDecimal getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}
	public BigDecimal getSubtotal_con_iva() {
		return subtotal_con_iva;
	}
	public void setSubtotal_con_iva(BigDecimal subtotal_con_iva) {
		this.subtotal_con_iva = subtotal_con_iva;
	}


	@Override
	public String toString() {
		return "DetalleVenta [codigo=" + codigo + ", venta=" + venta + ", producto=" + producto + ", cantidad="
				+ cantidad + ", precio=" + precio + ", subtotal=" + subtotal + ", subtotal_con_iva=" + subtotal_con_iva
				+ "]";
	}
	
	
	
}
