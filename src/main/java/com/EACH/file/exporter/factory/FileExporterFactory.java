package com.EACH.file.exporter.factory;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.EACH.exceptions.UnsupportedMediaTypeException;
import com.EACH.file.exporter.MediaTypes;
import com.EACH.file.exporter.contract.FileExporter;
import com.EACH.file.exporter.impl.CsvExporter;
import com.EACH.file.exporter.impl.XlsxExporter;

@Component
public class FileExporterFactory {

	private Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);
	
	@Autowired
	private ApplicationContext context;
	
	public FileExporter getExporter(String acceptHeader) throws Exception{
		if(acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)) {
			//return new CsvExporter();
			return context.getBean(CsvExporter.class);
		}
		else if(acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
			//return new XlsxExporter();
			return context.getBean(XlsxExporter.class);
		}else throw new UnsupportedMediaTypeException("Invalid File Format!");
	}
}
