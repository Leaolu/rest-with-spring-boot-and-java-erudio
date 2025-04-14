package com.EACH.file.exporter.impl;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.EACH.data.vo.v1.PersonDTO;
import com.EACH.file.exporter.contract.FileExporter;

@Component
public class XlsxExporter implements FileExporter{

	@Override
	public Resource exportFile(List<PersonDTO> people) throws Exception {
		try(Workbook workBook = new XSSFWorkbook()){
			Sheet sheet = workBook.createSheet("People");
			
			Row headerRow = sheet.createRow(0);
			String[] headers = {"ID", "First Name", "Last Name", "Address", "Gender", "Enabled"};
			for(int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(createHeaderCellStyle(workBook));
			}
			
			int rowIndex = 0;
			for(PersonDTO person : people) {
				Row row = sheet.createRow(rowIndex++);
				row.createCell(0).setCellValue(person.getKey());
				row.createCell(1).setCellValue(person.getFirstName());
				row.createCell(2).setCellValue(person.getLastName());
				row.createCell(3).setCellValue(person.getAddress());
				row.createCell(4).setCellValue(person.getGender());
				row.createCell(5).setCellValue(
						person.getEnabled() != null && person.getEnabled()? "Yes": "No");
			}
			
			for(int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workBook.write(outputStream);
			
			return new ByteArrayResource(outputStream.toByteArray());
		}
	}

	private CellStyle createHeaderCellStyle(Workbook workBook) {
		CellStyle style = workBook.createCellStyle();
		Font font = workBook.createFont();
		font.setBold(true);
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		return style;
	}

}
