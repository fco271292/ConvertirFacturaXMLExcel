package com.fco271292.service

import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.util.HSSFColor
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import com.fco271292.domain.Comprobante
import com.fco271292.domain.Concepto

class FabricaExcel {
	
	def generarLibro(String rutaArchivo,Comprobante comprobante){
		
		XSSFWorkbook workbook = new XSSFWorkbook()
		
		XSSFSheet hojaDatosGenerales = generaHojaExcel(workbook,"Datos_Generales")
		XSSFRow renglonDatosGeneralesTitulos = generaFila(hojaDatosGenerales,0)
		def titulosDatosGenerales = ["Folio","Fecha","Sub Total","Descuento","Moneda","Total","Tipo de Comprobante","Metodo Pago","Lugar Expedicion"]
		titulosDatosGenerales.eachWithIndex{titulo,indice->
			XSSFCell celda = generaCelda(renglonDatosGeneralesTitulos, (indice), titulo)
			celda.setCellStyle(agregarEstilo(workbook))
		}
		
		XSSFRow renglonDatosGenerales = generaFila(hojaDatosGenerales,1 )
		generaCelda(renglonDatosGenerales,0,comprobante.folio)
		generaCelda(renglonDatosGenerales,1,comprobante.fecha)
		generaCelda(renglonDatosGenerales,2,comprobante.subTotal)
		generaCelda(renglonDatosGenerales,3,comprobante.descuento)
		generaCelda(renglonDatosGenerales,4,comprobante.moneda)
		generaCelda(renglonDatosGenerales,5,comprobante.total)
		generaCelda(renglonDatosGenerales,6,comprobante.tipoDeComprobante)
		generaCelda(renglonDatosGenerales,7,comprobante.metodoPago)
		generaCelda(renglonDatosGenerales,8,comprobante.lugarExpedicion)
		
		XSSFSheet hojaEmisor = generaHojaExcel(workbook,"Emisor")
		XSSFRow renglonEmisor = generaFila(hojaEmisor,0)
		def titulosEmisor = ["RFC","Nombre","Regimen Fiscal"]
		titulosEmisor.eachWithIndex{titulo,indice->
			XSSFCell celda = generaCelda(renglonEmisor, (indice), titulo)
			celda.setCellStyle(agregarEstilo(workbook))
		}
		comprobante.emisor.eachWithIndex{emisorObjeto,indice->
			XSSFRow renglon = generaFila(hojaEmisor,(indice+1) )
			generaCelda(renglon,0,emisorObjeto.rfc)
			generaCelda(renglon,1,emisorObjeto.nombre)
			generaCelda(renglon,2,emisorObjeto.regimenFiscal)
		}
		
		XSSFSheet hojaReceptor = generaHojaExcel(workbook,"Receptor")
		XSSFRow renglonReceptor = generaFila(hojaReceptor,0)
		def titulosReceptor = ["RFC","Nombre","Uso CFDI"]
		titulosReceptor.eachWithIndex{titulo,indice->
			XSSFCell celda = generaCelda(renglonReceptor, (indice), titulo)
			celda.setCellStyle(agregarEstilo(workbook))
		}
		comprobante.receptor.eachWithIndex{receptorObjeto,indice->
			XSSFRow renglon = generaFila(hojaReceptor,(indice+1) )
			generaCelda(renglon,0,receptorObjeto.rfc)
			generaCelda(renglon,1,receptorObjeto.nombre)
			generaCelda(renglon,2,receptorObjeto.usoCFDI)
		}
		
		XSSFSheet hojaConceptos = generaHojaExcel(workbook,"Productos")

		XSSFRow renglonConceptos = generaFila(hojaConceptos,0)
		def titulosConceptos = ["Clave Prod Serv","Clave Unidad","Unidad","Descripcion","Cantidad","Valor Unitario","Importe","Descuento"/*,"Impuestos"*/]
		
		titulosConceptos.eachWithIndex{titulo,indice->
			XSSFCell celda = generaCelda(renglonConceptos, (indice), titulo)
			celda.setCellStyle(agregarEstilo(workbook))
		}

		comprobante.conceptos.eachWithIndex{comprobanteObjeto,indice->
			XSSFRow renglon = generaFila(hojaConceptos,(indice+1) )
			generaCelda(renglon,0,comprobanteObjeto.claveProdServ)
			generaCelda(renglon,1,comprobanteObjeto.claveUnidad)
			generaCelda(renglon,2,comprobanteObjeto.unidad)
			generaCelda(renglon,3,comprobanteObjeto.descripcion)
			generaCelda(renglon,4,comprobanteObjeto.cantidad)
			generaCelda(renglon,5,comprobanteObjeto.valorUnitario)
			generaCelda(renglon,6,comprobanteObjeto.importe)
			generaCelda(renglon,7,comprobanteObjeto.descuento)
		}
		
		
		XSSFSheet hojaImpuestos = generaHojaExcel(workbook,"Impuestos")
		XSSFRow renglonImpuestos = generaFila(hojaImpuestos,0)
		def titulosImpuestos = ["Impuesto","Tipo Factor","Tasa o Cuota","Importe"]
		titulosImpuestos.eachWithIndex{titulo,indice->
			XSSFCell celda = generaCelda(renglonImpuestos, (indice), titulo)
			celda.setCellStyle(agregarEstilo(workbook))
		}
		comprobante.impuestos.eachWithIndex{impuestoObjeto,indice->
			impuestoObjeto.traslados.eachWithIndex{traslado,indiceTraslado->
				XSSFRow renglon = generaFila(hojaImpuestos,(indiceTraslado+1) )
				generaCelda(renglon,0,traslado.impuesto)
				generaCelda(renglon,1,traslado.tipoFactor)
				generaCelda(renglon,2,traslado.tasaOCuota)
				generaCelda(renglon,3,traslado.importe)
			}
		}
		
		XSSFSheet hojaComplemento= generaHojaExcel(workbook,"Complemento")
		XSSFRow renglonComplemento = generaFila(hojaComplemento,0)
		def titulosComplemento = ["Rfc Prov Certif","Fecha Timbrado","No Certificado SAT"]
		titulosComplemento.eachWithIndex{titulo,indice->
			XSSFCell celda = generaCelda(renglonComplemento, (indice), titulo)
			celda.setCellStyle(agregarEstilo(workbook))
		}
		comprobante.complemento.timbreFiscalDigital.eachWithIndex{timbreFiscalObjeto,indice->
			XSSFRow renglon = generaFila(hojaComplemento,(indice+1) )
			generaCelda(renglon,0,timbreFiscalObjeto.rfcProvCertif)
			generaCelda(renglon,1,timbreFiscalObjeto.fechaTimbrado)
			generaCelda(renglon,2,timbreFiscalObjeto.noCertificadoSAT)
		}
		
		guardaLibro(workbook,rutaArchivo)
		
	}
	
	def generarLibroGeneral(String rutaArchivo,List<Concepto> conceptos){
		
		XSSFWorkbook workbook = new XSSFWorkbook()
		
		XSSFSheet hojaConceptos = generaHojaExcel(workbook,"Productos")

		XSSFRow renglonConceptos = generaFila(hojaConceptos,0)
		def titulosConceptos = ["Clave Prod Serv","Clave Unidad","Unidad","Descripcion","Cantidad","Valor Unitario","Importe","Descuento"]
		
		titulosConceptos.eachWithIndex{titulo,indice->
			XSSFCell celda = generaCelda(renglonConceptos, (indice), titulo)
			celda.setCellStyle(agregarEstilo(workbook))
		}

		conceptos.eachWithIndex{comprobanteObjeto,indice->
			XSSFRow renglon = generaFila(hojaConceptos,(indice+1) )
			generaCelda(renglon,0,comprobanteObjeto.claveProdServ)
			generaCelda(renglon,1,comprobanteObjeto.claveUnidad)
			generaCelda(renglon,2,comprobanteObjeto.unidad)
			generaCelda(renglon,3,comprobanteObjeto.descripcion)
			generaCelda(renglon,4,comprobanteObjeto.cantidad)
			generaCelda(renglon,5,comprobanteObjeto.valorUnitario)
			generaCelda(renglon,6,comprobanteObjeto.importe)
			generaCelda(renglon,7,comprobanteObjeto.descuento)
		}
		
		guardaLibro(workbook,rutaArchivo)
		
	}
	
	def guardaLibro(XSSFWorkbook workbook,String rutaArchivo) {
		
		FileOutputStream excel = new FileOutputStream(rutaArchivo)
		try{
			workbook.write(excel)
		}catch(Exception e){
			println "Error al generar archivo de Excel ${e.message}"
		}
	}
	
	XSSFSheet generaHojaExcel(XSSFWorkbook workbook,String nombreHoja){
	
		XSSFSheet hoja = workbook.createSheet(nombreHoja)
	}
	
	XSSFRow generaFila(XSSFSheet sheet,Integer numeroFila){
		XSSFRow renglon = sheet.createRow(numeroFila)
	}
	
	XSSFCell generaCelda(XSSFRow renglon,Integer numeroCelda,def contenido) {
		XSSFCell celda = renglon.createCell(numeroCelda)
		celda.setCellValue(contenido)
		celda
	}
	
	XSSFCellStyle agregarEstilo(XSSFWorkbook workbook) {
		XSSFCellStyle estilo = workbook.createCellStyle()
		estilo.setFillBackgroundColor(IndexedColors.BLACK.getIndex())
		estilo.setFillPattern(FillPatternType.SOLID_FOREGROUND)
		Font fuente = workbook.createFont()
		fuente.setColor(HSSFColorPredefined.WHITE.index)
		estilo.setFont(fuente)
		estilo
	}
	
}
