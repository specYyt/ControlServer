package com.control.log;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.log4j.PropertyConfigurator;

public class Log4j extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		// 读取配置文件 设置参数
		System.setProperty("webappRoot", getServletContext().getRealPath("/"));
		PropertyConfigurator.configure(getServletContext().getRealPath("/") + getInitParameter("configfile"));
	
	}

	public void destroy() {
		super.destroy();
	}
}
