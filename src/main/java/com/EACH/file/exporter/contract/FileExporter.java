package com.EACH.file.exporter.contract;

import java.util.List;

import org.springframework.core.io.Resource;

import com.EACH.data.vo.v1.PersonDTO;

public interface FileExporter {
	
	Resource exportFile(List<PersonDTO> people) throws Exception; 
}
