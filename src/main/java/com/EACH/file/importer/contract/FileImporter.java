package com.EACH.file.importer.contract;

import java.io.InputStream;
import java.util.List;

import com.EACH.data.vo.v1.PersonDTO;

public interface FileImporter {
	
	List<PersonDTO> importFile(InputStream inputStream) throws Exception; 
}
