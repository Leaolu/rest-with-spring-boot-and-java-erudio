package com.EACH.controllers.docs;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.EACH.data.vo.v1.UploadFileResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "File Endpoint")
public interface FileControllerDocs {
	
	@Operation(summary = "Uploads one File", description = "Upload one File per request", tags = "File Endpoint", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500"),
			@ApiResponse(description = "Not Found", responseCode = "404")
	})
	UploadFileResponseDTO uploadFile(MultipartFile file);
	
	@Operation(summary = "Uploads Multiple Files", description = "Uploads loads of Files all at once", tags = "File Endpoint", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500"),
			@ApiResponse(description = "Not Found", responseCode = "404")
	})
	List<UploadFileResponseDTO> uploadMultipleFiles(MultipartFile[] files);
	
	@Operation(summary = "Downloads one File", description = "Downloads one File per request", tags = "File Endpoint", responses = {
			@ApiResponse(description = "Success", responseCode = "200"),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Server Error", responseCode = "500"),
			@ApiResponse(description = "Not Found", responseCode = "404")
	})
	ResponseEntity<Resource> downloadFile(String fileName, HttpServletRequest request);

}
