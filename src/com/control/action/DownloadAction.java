package com.control.action;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.struts2.ServletActionContext;

public class DownloadAction extends SuperAction {

	private static final long serialVersionUID = 1L;

	private String fileName;
	private String task_id;
	private String inputPath = "doc";

	public InputStream getTargetFile() throws Exception {
		inputPath += File.separator + task_id + File.separator + fileName;
		return ServletActionContext.getServletContext().getResourceAsStream(inputPath);
	}

	public String execute() {
		return SUCCESS;
	}

	public String getFileName() throws UnsupportedEncodingException {
		this.fileName = new String(fileName.getBytes(), "ISO-8859-1");
		return this.fileName;
	}

	public void setFileName(String fileName) {
		try {
			fileName = new String(fileName.getBytes("ISO-8859-1"), "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		this.fileName = fileName;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

}
