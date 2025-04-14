package com.EACH.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.EACH.config.FileStorageConfig;
import com.EACH.controllers.FileController;
import com.EACH.exceptions.FileNotFoundException;
import com.EACH.exceptions.FileStorageException;

@Service
public class FileStorageServices {
	
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	
	private  Path fileStorageLocation;
	
	
	public FileStorageServices(FileStorageConfig fileStorageConfig) {
		Path path = Paths.get(fileStorageConfig.getUploadDir())
				.toAbsolutePath().normalize();
		
		
		this.fileStorageLocation = path;
		try {
			logger.info("Creating Directories");
			Files.createDirectories(this.fileStorageLocation);
	
		}catch(Exception e) {
			logger.error("Could not create the directory where the files will be stored!");
			throw new FileStorageException("Could not create the directory where the files will be stored!",e);
		} 
	}
	
	public String storeFile(MultipartFile file) {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if(fileName.contains("..")) {
				logger.error("Sorry, File name contains a invalid path sequence " +fileName);
				throw new FileStorageException("Sorry, File name contains a invalid path sequence " +fileName);
			}
			
			logger.info("Saving File in Disk");
			
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			return fileName;
		}catch(Exception e) {
			logger.error("Could not store file " +fileName+ ". Please try again");
			throw new FileStorageException("Could not store file " +fileName+ ". Please try again", e);
		}
	}
	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if(resource.exists()) {
				return resource;
			}
			throw new FileNotFoundException("File not Found " +fileName);
			
		}catch(Exception e) {
			logger.error("File not Found " +fileName);
			throw new FileNotFoundException("File not Found " +fileName, e);
		}
	}
	
	
}
