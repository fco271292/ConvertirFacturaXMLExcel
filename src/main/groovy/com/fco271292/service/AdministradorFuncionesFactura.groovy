package com.fco271292.service

import com.fco271292.domain.Comprobante
import com.fco271292.domain.Concepto

class AdministradorFuncionesFactura {
	
	TransformarXML transformarXML = new TransformarXML()
	FabricaExcel fabricaExcel = new FabricaExcel()
	List<Concepto> conceptos = []

	def generaFacturaExcel(String rutaArchivo) {
		File archivo = new File(rutaArchivo)
		Comprobante comprobante = transformarXML.convertirXML(archivo)
		String nombreEmisor = comprobante.emisor.nombre.replaceAll(~/(\s+|\.+|,+)/,"_")
		String fechaEmision = comprobante.fecha.replaceAll(~/(-+|:+)/,"_")
		String folioFactura = comprobante.folio
		String nombreExcel = "${nombreEmisor}_${fechaEmision}__${folioFactura?:"NA"}.xlsx"
		String directorio = archivo.parent
		fabricaExcel.generarLibro("${directorio}${nombreExcel}",comprobante)
	}
	
	def generaMultipleFacturaExcel(String rutaDirectorio) {
		new File(rutaDirectorio).eachFileMatch(~/.*.xml/){file->
			File archivo = new File(file.getAbsolutePath())
			Comprobante comprobante = transformarXML.convertirXML(archivo)
			conceptos << comprobante.conceptos
			String nombreEmisor = comprobante.emisor.nombre.replaceAll(~/(\s+|\.+|,+)/,"_")
			String fechaEmision = comprobante.fecha.replaceAll(~/(-+|:+)/,"_")
			String folioFactura = comprobante.folio
			String nombreExcel = "${nombreEmisor}_${fechaEmision}__${folioFactura?:"NA"}.xlsx"
			String directorio = archivo.parent
			fabricaExcel.generarLibro("${directorio}${nombreExcel}",comprobante)
		}
		String nombreExcelGeneral = "${rutaDirectorio}_Productos_${new Date().format("ddMMyyyy_hhmm")}.xlsx"
		fabricaExcel.generarLibroGeneral(nombreExcelGeneral,conceptos.flatten())
	}
	
}
