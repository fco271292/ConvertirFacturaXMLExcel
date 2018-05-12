package com.fco271292.service

import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.filechooser.FileSystemView

class InterfazGraficaUsuario {
	
	AdministradorFuncionesFactura administradorFuncionesFactura = new AdministradorFuncionesFactura()

	def abrirArchivoSwing() {
		JFileChooser jfileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory())
		jfileChooser.setDialogTitle("Buscar facturas")
		jfileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES)
		
		FileNameExtensionFilter fileNameExtensionFilterXML = new FileNameExtensionFilter("Factura", "xml")
		jfileChooser.resetChoosableFileFilters()
		jfileChooser.addChoosableFileFilter(fileNameExtensionFilterXML)
		jfileChooser.setAcceptAllFileFilterUsed(false)
		
		Integer returnValue = jfileChooser.showOpenDialog(null)
		
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			File archivoSeleccionado = jfileChooser.getSelectedFile()
			if(archivoSeleccionado.isFile()) {
				administradorFuncionesFactura.generaFacturaExcel(archivoSeleccionado.absolutePath)
			}else{
				administradorFuncionesFactura.generaMultipleFacturaExcel(archivoSeleccionado.absolutePath)
			}
		}
		else if(returnValue == JFileChooser.CANCEL_OPTION) {
			println "Cancelado"
		}
	}
	
}
