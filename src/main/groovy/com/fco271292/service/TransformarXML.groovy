package com.fco271292.service

import groovy.util.slurpersupport.GPathResult

import com.fco271292.domain.Complemento
import com.fco271292.domain.Comprobante
import com.fco271292.domain.Concepto
import com.fco271292.domain.Emisor
import com.fco271292.domain.Impuesto
import com.fco271292.domain.Receptor
import com.fco271292.domain.TimbreFiscalDigital
import com.fco271292.domain.Traslado

class TransformarXML {
	
	def convertirXML(File factura){
		def xml = new XmlSlurper().parse(factura).declareNamespace([
			cfdi:"http://www.sat.gob.mx/cfd/3",xsi:"http://www.w3.org/2001/XMLSchema-instance"])
		Comprobante comprobante = new Comprobante()
		
		def datosGeneralesComprobante = obtenerDatosGeneralesComprobante(xml)
		comprobante.folio = datosGeneralesComprobante.folio
		comprobante.fecha = datosGeneralesComprobante.fecha
		comprobante.subTotal = datosGeneralesComprobante.subTotal
		comprobante.descuento = datosGeneralesComprobante.descuento
		comprobante.moneda = datosGeneralesComprobante.moneda
		comprobante.total = datosGeneralesComprobante.total
		comprobante.tipoDeComprobante = datosGeneralesComprobante.tipoDeComprobante
		comprobante.metodoPago = datosGeneralesComprobante.metodoPago
		comprobante.lugarExpedicion = datosGeneralesComprobante.lugarExpedicion
		
		comprobante.emisor = obtenerEmisor(xml)
		comprobante.receptor = obtenerReceptor(xml)
		comprobante.conceptos = obtenerConceptos(xml)
		comprobante.impuestos = obtenerImpuestos(xml)
		comprobante.complemento = obtenerComplemento(xml)
		comprobante
	}
	
	def obtenerDatosGeneralesComprobante (GPathResult xml){
		Comprobante comprobante = new Comprobante()
		
		def datosComprobante = [:]
		String folio = ""
		String fecha = ""
		String subTotal = ""
		String descuento = ""
		String moneda = ""
		String total = ""
		String tipoDeComprobante = ""
		String metodoPago = ""
		String lugarExpedicion = ""
		
		if(xml.@Folio.toString()){
			folio = xml.@Folio.toString()
		}else if(xml.@folio.toString()){
			folio = xml.@folio.toString()
		}
		
		if(xml.@Fecha.toString()){
			fecha = xml.@Fecha.toString()
		}else if(xml.@fecha.toString()){
			fecha = xml.@fecha.toString()
		}
		
		if(xml.@SubTotal.toString()) {
			subTotal = xml.@SubTotal.toString()
		}else if(xml.@subTotal.toString()) {
			subTotal = xml.@subTotal.toString()
		}
		
		if(xml.@Descuento.toString()) {
			descuento = xml.@Descuento.toString()
		}else if(xml.@Descuento.toString()) {
			descuento = xml.@Descuento.toString()
		}
		
		if(xml.@Total.toString()) {
			total = xml.@Total.toString()
		}else if(xml.@total.toString()) {
			total = xml.@total.toString()
		}
		
		if(xml.@Moneda.toString()){
			moneda = xml.@Moneda.toString()
		}else if(xml.@moneda.toString()){
			moneda = xml.@Moneda.toString()
		}
		if(xml.@TipoDeComprobante.toString()){
			tipoDeComprobante= xml.@TipoDeComprobante.toString()
		}else if(xml.@tipoDeComprobante.toString()){
			tipoDeComprobante= xml.@tipoDeComprobante.toString()
		}
		
		if(xml.@MetodoPago.toString()){
			metodoPago = xml.@MetodoPago.toString()
		}else if(xml.@metodoPago.toString()){
			metodoPago = xml.@metodoPago.toString()
		}
		
		if(xml.@LugarExpedicion.toString()){
			lugarExpedicion = xml.@LugarExpedicion.toString()
		}else if(xml.@lugarExpedicion.toString()){
			lugarExpedicion = xml.@lugarExpedicion.toString()
		}
		
		datosComprobante.folio = folio
		datosComprobante.fecha = fecha
		datosComprobante.subTotal = new BigDecimal(subTotal ?: 0)
		datosComprobante.descuento = new BigDecimal( descuento ?: 0)
		datosComprobante.moneda = moneda
		datosComprobante.total = new BigDecimal(total ?: 0)
		datosComprobante.tipoDeComprobante = tipoDeComprobante
		datosComprobante.metodoPago = metodoPago
		datosComprobante.lugarExpedicion = lugarExpedicion
		datosComprobante
	}
	
	Emisor obtenerEmisor(GPathResult xml){
		Emisor emisor = new Emisor()
		String rfc = ""
		String nombre = ""
		String regimenFiscal = ""
		if(xml.Emisor.@Rfc.toString()){
			rfc = xml.Emisor.@Rfc.toString()
		}else if(xml.Emisor.@rfc.toString()){
			rfc = xml.Emisor.@rfc.toString()
		}
		
		if(xml.Emisor.@Nombre.toString()){
			nombre = xml.Emisor.@Nombre.toString()
		}else if(xml.Emisor.@nombre.toString()){
			nombre = xml.Emisor.@nombre.toString()
		}
		
		if(xml.Emisor.@RegimenFiscal.toString()){
			regimenFiscal = xml.Emisor.@RegimenFiscal.toString()
		}else if(xml.Emisor.@regimenFiscal.toString()){
			regimenFiscal = xml.Emisor.@regimenFiscal.toString()
		}
		
		emisor.rfc = rfc
		emisor.nombre = nombre 
		emisor.regimenFiscal = regimenFiscal
		emisor
	}
	
	Receptor obtenerReceptor(GPathResult xml){
		Receptor receptor = new Receptor()
		String rfc = ""
		String nombre = ""
		String usoCFDI = ""
		
		if(xml.Receptor.@Rfc.toString()){
			rfc = xml.Receptor.@Rfc.toString()
		}else if(xml.Receptor.@rfc.toString()){
			rfc = xml.Receptor.@rfc.toString()
		}
		
		if(xml.Receptor.@Nombre.toString()){
			nombre = xml.Receptor.@Nombre.toString()
		}else if(xml.Receptor.@nombre.toString()){
			nombre = xml.Receptor.@nombre.toString()
		}
		
		if(xml.Receptor.@UsoCFDI.toString()){
			usoCFDI = xml.Receptor.@UsoCFDI.toString()
		}else if(xml.Receptor.@usoCFDI.toString()){
			usoCFDI = xml.Receptor.@usoCFDI.toString()
		}
		
		receptor.rfc = rfc
		receptor.nombre = nombre
		receptor.usoCFDI = usoCFDI 
		receptor
	}
	
	List<Concepto> obtenerConceptos(GPathResult xml){
		List<Concepto> listaConceptos = []
		xml.Conceptos.Concepto.each{conceptoXML->
			Concepto concepto = new Concepto()
			List<Traslado> traslados = []
			List<Impuesto> impuestos = []
			Impuesto impuesto = new Impuesto()
			String claveProdServ = ""
			String cantidad = ""
			String claveUnidad = ""
			String unidad = ""
			String descripcion = ""
			String valorUnitario = ""
			String importe = ""
			String descuento = ""
			
			if(conceptoXML.@ClaveProdServ.toString()) {
				claveProdServ = conceptoXML.@ClaveProdServ.toString()
			}else if(conceptoXML.@noIdentificacion.toString()) {
				claveProdServ = conceptoXML.@noIdentificacion.toString()
			}
			
			if(conceptoXML.@Cantidad.toString()) {
				cantidad = conceptoXML.@Cantidad.toString()
			}else if(conceptoXML.@cantidad.toString()) {
				cantidad = conceptoXML.@cantidad.toString()
			}
			
			if(conceptoXML.@ClaveUnidad.toString()) {
				claveUnidad = conceptoXML.@Cantidad.toString()
			}else if(conceptoXML.@ClaveUnidad.toString()) {
				claveUnidad = conceptoXML.@cantidad.toString()
			}
			
			if(conceptoXML.@Unidad.toString()) {
				unidad = conceptoXML.@Unidad.toString()
			}else if(conceptoXML.@unidad.toString()) {
				unidad = conceptoXML.@unidad.toString()
			}
			
			if(conceptoXML.@Descripcion.toString()) {
				descripcion = conceptoXML.@Descripcion.toString()
			}else if(conceptoXML.@descripcion.toString()) {
				descripcion = conceptoXML.@descripcion.toString()
			}
			
			if(conceptoXML.@ValorUnitario.toString()) {
				valorUnitario = conceptoXML.@ValorUnitario.toString()
			}else if(conceptoXML.@valorUnitario.toString()) {
				valorUnitario = conceptoXML.@valorUnitario.toString()
			}
			
			if(conceptoXML.@Importe.toString()) {
				importe = conceptoXML.@Importe.toString()
			}else if(conceptoXML.@importe.toString()) {
				importe = conceptoXML.@importe.toString()
			}
			
			if(conceptoXML.@Descuento.toString()) {
				descuento = conceptoXML.@Descuento.toString()
			}else if(conceptoXML.@descuento.toString()) {
				descuento = conceptoXML.@descuento.toString()
			}
			
			concepto.claveProdServ = claveProdServ
			concepto.cantidad = new BigDecimal(cantidad ?: 0) 
			concepto.claveUnidad = claveUnidad
			concepto.unidad = unidad
			concepto.descripcion = descripcion
			concepto.valorUnitario =  new BigDecimal(valorUnitario ?: 0)
			concepto.importe = new BigDecimal(importe ?: 0) 
			concepto.descuento = new BigDecimal(descuento ?: 0)
			concepto.impuestos = []
			
			conceptoXML.Impuestos.Traslados.Traslado.each{trasladoXML->
				Traslado traslado = new Traslado()
				traslado.base = new BigDecimal(trasladoXML.@Base.toString() ?: 0)
				traslado.impuesto = new BigDecimal(trasladoXML.@Impuesto.toString() ?: 0)
				traslado.tipoFactor = trasladoXML.@TipoFactor
				traslado.tasaOCuota = new BigDecimal(trasladoXML.@TasaOCuota.toString() ?: 0)
				traslado.importe = new BigDecimal(trasladoXML.@Importe.toString() ?: 0)
				traslados << traslado
			}
			impuesto.traslados = traslados 
			impuestos << impuesto
			concepto.impuestos = impuestos
			listaConceptos << concepto
		}
		listaConceptos
	}

	List<Impuesto> obtenerImpuestos(GPathResult xml){
		List<Impuesto> impuestos = []
		List<Traslado> traslados = []
		Impuesto impuesto = new Impuesto()
		String totalImpuestosTrasladados = ""
		if(xml.Impuestos.@TotalImpuestosTrasladados.toString()) {
			totalImpuestosTrasladados = xml.Impuestos.@TotalImpuestosTrasladados.toString()
		}else if(xml.Impuestos.@totalImpuestosTrasladados.toString()) {
			totalImpuestosTrasladados = xml.Impuestos.@totalImpuestosTrasladados.toString()
		}
		impuesto.totalImpuestosTrasladados = new BigDecimal(totalImpuestosTrasladados ?: 0)
		xml.Impuestos.Traslados.Traslado.each{trasladoXML->
			Traslado traslado = new Traslado()
			traslado.base = new BigDecimal(trasladoXML.@Base.toString() ?: 0)
			String impuestoXML = ""
			if(trasladoXML.@Impuesto.toString()) {
				impuestoXML = trasladoXML.@Impuesto.toString()
			}else if(trasladoXML.@Impuesto.toString() ) {
				impuestoXML = trasladoXML.@impuesto.toString()
			}
			traslado.impuesto = new BigDecimal(impuestoXML ?: 0)
			traslado.tipoFactor = trasladoXML.@TipoFactor
			String tasa = ""
			if(trasladoXML.@TasaOCuota.toString()) {
				tasa = trasladoXML.@TasaOCuota.toString()
			}else if(trasladoXML.@tasa.toString()) {
				tasa = trasladoXML.@tasa.toString()
			}
			traslado.tasaOCuota = new BigDecimal( tasa ?: 0)
			String importe = ""
			if(trasladoXML.@Importe.toString() ) {
				importe = trasladoXML.@Importe.toString() 
			}else if(trasladoXML.@importe.toString() ) {
				importe = trasladoXML.@importe.toString()
			}
			traslado.importe = new BigDecimal(importe ?: 0)
			traslados << traslado
		}
		impuesto.traslados = traslados
		impuestos << impuesto
		impuestos
	}	

	Complemento obtenerComplemento(GPathResult xml){
		Complemento complemento = new Complemento()
		TimbreFiscalDigital timbreFiscalDigital = new TimbreFiscalDigital()
		timbreFiscalDigital.rfcProvCertif = xml.Complemento.TimbreFiscalDigital.@RfcProvCertif
		timbreFiscalDigital.fechaTimbrado = xml.Complemento.TimbreFiscalDigital.@FechaTimbrado
		timbreFiscalDigital.noCertificadoSAT = xml.Complemento.TimbreFiscalDigital.@NoCertificadoSAT ?: xml.Complemento.TimbreFiscalDigital.@noCertificadoSAT
		complemento.timbreFiscalDigital = timbreFiscalDigital
		complemento
	}

}
