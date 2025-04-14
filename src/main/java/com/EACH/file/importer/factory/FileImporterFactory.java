package com.EACH.file.importer.factory;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.EACH.exceptions.UnsupportedMediaTypeException;
import com.EACH.file.importer.contract.FileImporter;
import com.EACH.file.importer.impl.CsvImporter;
import com.EACH.file.importer.impl.XlsxImporter;

@Component
public class FileImporterFactory {

	private Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);
	
	@Autowired
	private ApplicationContext context;
	
	public FileImporter getImporter(String fileName) throws Exception{
		if(fileName.endsWith(".csv")) {
			//return new CsvImporter();
			return context.getBean(CsvImporter.class);
		}
		else if(fileName.endsWith(".xlsx")) {
			//return new XlsxImporter();
			return context.getBean(XlsxImporter.class);
		}else throw new UnsupportedMediaTypeException("Invalid File Format!");
	}
}
