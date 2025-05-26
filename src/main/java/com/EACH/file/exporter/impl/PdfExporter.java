package com.EACH.file.exporter.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.EACH.data.vo.v1.PersonDTO;
import com.EACH.exceptions.FileNotFoundException;
import com.EACH.file.exporter.contract.FileExporter;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class PdfExporter implements FileExporter{

	@Override
	public Resource exportFile(List<PersonDTO> people) throws Exception {
		// TODO Auto-generated method stub
		InputStream inputStream = getClass().getResourceAsStream("/templates/people.jrxml");
		if(inputStream == null) {
			throw new FileNotFoundException("Template file not found: /templates/people.jrxml");
		}
		
		JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
		
		List<Map<String, Object>> reportData = people.stream().map(person -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", person.getKey());
            map.put("name", person.getName()); // usando o método getName() que você criou
            map.put("address", person.getAddress());
            map.put("gender", person.getGender());
            map.put("enabled", person.getEnabled());
            return map;
        }).collect(Collectors.toList());
		
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
		
		Map<String, Object> parameters = new HashMap<>();
		//parameters.put("title", "People Report");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters , dataSource);
		try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
			JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
			return new ByteArrayResource(outputStream.toByteArray());
		}
	}

}
