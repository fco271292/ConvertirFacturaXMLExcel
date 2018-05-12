package com.fco271292.domain

import java.util.List;

class Comprobante {
	
	String folio
	String fecha
	BigDecimal subTotal
	BigDecimal descuento
	String moneda
	BigDecimal total
	String tipoDeComprobante
	String metodoPago
	String lugarExpedicion
	Emisor emisor
	Receptor receptor
	List<Concepto> conceptos
	List<Impuesto> impuestos
	Complemento complemento
	
}
