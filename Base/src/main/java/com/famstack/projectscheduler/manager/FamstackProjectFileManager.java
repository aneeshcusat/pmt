package com.famstack.projectscheduler.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.famstack.projectscheduler.util.StringUtils;

@Component
public class FamstackProjectFileManager extends BaseFamstackManager {

	public void uploadFile(MultipartFile file, String projectCode, HttpServletRequest request) {
		projectCode = sanitize(projectCode);
		if (!file.isEmpty()) {
			try {
				String uploadsDir = "/uploads/";
				String realPathtoUploads = getFilePath(uploadsDir);
				File uploadLocationFile = new File(realPathtoUploads);
				if (!uploadLocationFile.exists()) {
					uploadLocationFile.mkdir();
				}
				String projectFileLocation = realPathtoUploads + "/" + projectCode + "/";
				File projectFile = new File(projectFileLocation);
				if (!projectFile.exists()) {
					projectFile.mkdir();
				}

				logDebug("realPathtoUploads :" + realPathtoUploads);

				String orgName = file.getOriginalFilename();
				String filePath = projectFile + "/" + orgName;
				File dest = new File(filePath);
				file.transferTo(dest);
			} catch (Exception e) {
				logError(e.getMessage());
			}
		}
	}

	private String getFilePath(String uploadsDir) {
		return getFamstackApplicationConfiguration().getFileUploadLocation();
	}

	public List<String> getProjectFiles(String projectCode, HttpServletRequest request) {
		projectCode = sanitize(projectCode);
		List<String> filesName = new ArrayList<>();
		String uploadsDir = "/uploads/";
		String realPathtoUploads = getFilePath(uploadsDir);
		String projectFileLocation = realPathtoUploads + "/" + projectCode + "/";
		File projectFile = new File(projectFileLocation);
		if (projectFile.exists()) {
			File[] files = projectFile.listFiles();
			if (files != null && files.length > 0) {
				for (File file : files) {
					filesName.add(file.getName());
				}
			}
		}
		return filesName;
	}

	private String sanitize(String fileName) {
		if(StringUtils.isNotBlank(fileName) ) {
			fileName = fileName.replaceAll("/...", "");
			fileName = fileName.replaceAll("/..", "");
			fileName = fileName.replaceAll("%00", "");
		}
		return fileName;
	}

	public void deleteFile(String fileName, String projectCode, HttpServletRequest request) {
		fileName = sanitize(fileName);
		projectCode = sanitize(projectCode);
		logDebug("file to be deleted : " + fileName);
		logDebug("folder name :" + projectCode);
		String uploadsDir = "/uploads/";
		String realPathtoUploads = getFilePath(uploadsDir);
		String projectFileLocation = realPathtoUploads + "/" + projectCode + "/" + fileName;
		File projectFile = new File(projectFileLocation);
		if (projectFile.exists()) {
			projectFile.delete();
			logDebug("file go deleted!");
		}
	}

	public File getFile(String fileName, String projectCode, HttpServletRequest request) {
		fileName = sanitize(fileName);
		projectCode = sanitize(projectCode);
		
		String uploadsDir = "/uploads/";
		String realPathtoUploads = getFilePath(uploadsDir);
		String projectFileLocation = realPathtoUploads + "/" + projectCode + "/" + fileName;
		File projectFile = new File(projectFileLocation);
		if (projectFile.exists()) {
			return projectFile;
		}
		return null;
	}
}
