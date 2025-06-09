package com.EACH.data.vo.v1;

import java.io.Serializable;
import java.util.Objects;

public class UploadFileResponseDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String fileName;
	private String fileDownloadURI;
	private String fileType;
	private Long size;
	
	public UploadFileResponseDTO() {
		
	}

	public UploadFileResponseDTO(String fileName, String fileDownloadURI, String fileType, Long size) {
		this.fileName = fileName;
		this.fileDownloadURI = fileDownloadURI;
		this.fileType = fileType;
		this.size = size;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileDownloadURI() {
		return fileDownloadURI;
	}

	public void setFileDownloadURI(String fileDownloadURI) {
		this.fileDownloadURI = fileDownloadURI;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fileDownloadURI, fileName, fileType, size);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UploadFileResponseDTO other = (UploadFileResponseDTO) obj;
		return Objects.equals(fileDownloadURI, other.fileDownloadURI) && Objects.equals(fileName, other.fileName)
				&& Objects.equals(fileType, other.fileType) && Objects.equals(size, other.size);
	}
	
	

}
