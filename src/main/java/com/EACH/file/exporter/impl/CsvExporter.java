package com.EACH.file.exporter.impl;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.EACH.data.vo.v1.PersonDTO;
import com.EACH.file.exporter.contract.FileExporter;

@Component
public class CsvExporter implements FileExporter{

	@Override
	public Resource exportFile(List<PersonDTO> people) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
		
		CSVFormat csvFormat = CSVFormat.Builder.create()
				.setHeader("ID", "First Name", "Last Name", "Address", "Gender", "Enabled")
				.setSkipHeaderRecord(false)
				.get();
		
		try(CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
			for(PersonDTO person : people) {
				csvPrinter.printRecord(
						person.getKey(),
						person.getFirstName(),
						person.getLastName(),
						person.getAddress(),
						person.getGender(),
						person.getEnabled());
			}
		}
		return new ByteArrayResource(outputStream.toByteArray());
	}

}
