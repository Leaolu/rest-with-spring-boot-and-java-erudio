package com.EACH.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.EACH.controllers.docs.FileControllerDocs;
import com.EACH.data.vo.v1.UploadFileResponseDTO;
import com.EACH.services.FileStorageServices;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/file/v1")
public class FileController implements FileControllerDocs{
	
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	
	@Autowired
	private FileStorageServices service;
	
	@PostMapping("/uploadFile")
	@Override
	public UploadFileResponseDTO uploadFile(@RequestParam MultipartFile file) {
		var fileName = service.storeFile(file);
		//http://localhost:8080/api/file/v1/downloadFile/{fileName}.docx
		var fileDownloadURI = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/api/file/v1/downloadFile/")
				.path(fileName)
				.toUriString();
		return new UploadFileResponseDTO(fileName, fileDownloadURI, file.getContentType(), file.getSize());
	}
	
	@PostMapping("/uploadMultipleFiles")
	@Override
	public List<UploadFileResponseDTO> uploadMultipleFiles(@RequestParam MultipartFile[] files) {
		// TODO Auto-generated method stub
		return Arrays.asList(files).stream().map(file -> uploadFile(file)).collect(Collectors.toList());
	}
	
	@GetMapping("/downloadFile/{fileName:.+}")
	@Override
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// TODO Auto-generated method stub
		Resource resource = service.loadFileAsResource(fileName);
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		}catch(Exception e){
			logger.error("Could not Determine File type");
		}
		if(contentType == null) {
			contentType = "application/ocet-stream";
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION
						,"attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

}
