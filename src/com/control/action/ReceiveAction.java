package com.control.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSONObject;
import com.control.dao.NodeDao;
import com.control.dao.TaskDao;
import com.control.dao.TaskNodeDao;
import com.control.model.Node;
import com.control.model.Task;
import com.control.model.TaskForNodes;
import com.control.util.DictDealer;
import com.control.util.JsonParamUtil;

public class ReceiveAction extends SuperAction {

	private static final long serialVersionUID = 1L;
	private String param;
	private String savePath = "doc";// 文件存储根目录
	private NodeDao nodedao = new NodeDao();
	private TaskDao taskdao = new TaskDao();
	private TaskNodeDao tasknodedao = new TaskNodeDao();
	private final Logger logger = Logger.getLogger(ReceiveAction.class);
	private static String token;
	
	static {
		Properties ps = new Properties();
		try {
			ps.load(ReceiveAction.class.getResourceAsStream("/token.properties"));
			token = ps.getProperty("token");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	//接收密码平台的请求
	public void addServiceTask() throws Exception {
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, Object> paramMap = JsonParamUtil.getJsonObject(param);
		Map<String, Object> errorMap = new HashMap<String, Object>();
		if (paramMap == null || paramMap.isEmpty()) {
			errorMap.put("status", "100");
			errorMap.put("desc", "失败：参数格式错误");
			writer.write(JSONObject.toJSONString(errorMap));
			return;
		}
		String task_id = paramMap.get("task_id").toString();
		if(taskdao.findTask(task_id)){
			errorMap.put("status", "1");
			errorMap.put("desc", "任务已存在");
			writer.write(JSONObject.toJSONString(errorMap));
			return;
		}
		String type = paramMap.get("type").toString();
		String targeturl =URLDecoder.decode(paramMap.get("target").toString(), "utf-8");
		String userdocurl = URLDecoder.decode(paramMap.get("userdoc").toString(), "utf-8");
		String passdocurl = URLDecoder.decode(paramMap.get("passdoc").toString(), "utf-8");
		String custom = paramMap.get("custom").toString();
		int split = (int) paramMap.get("split");
		Task task = new Task(task_id, type, targeturl, userdocurl, passdocurl, custom, split);
		taskdao.addTask(task);// 添加任务信息
		logger.info(task_id + "已添加到数据库");
		if (!this.dictSplit(task_id, targeturl, userdocurl, passdocurl, split )) {
			logger.error("下载分割字典失败");
		}
		String path = request.getContextPath();
		String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
				+ "/" +"download.action?" + "task_id=" + task_id + "&fileName=" + task_id;//
		this.setTaskForNodes(task_id, type, targeturl, userdocurl, passdocurl, custom , url, split);
	}

	// 从密码平台下载用户名字典和密码字典，然后分割
	private boolean dictSplit(String task_id, String targeturl, String userdocurl, String passdocurl,  int split ) throws Exception {
		List<Node> nodeList = nodedao.getNodeList();//节点列表
		long nodecount = nodeList.size();// 节点数量
		boolean flag = false;
		String dictRootPath = ServletActionContext.getServletContext().getRealPath(savePath);
		DictDealer dictdealer = new DictDealer(dictRootPath, task_id);
		String dictPath = dictRootPath + File.separator + task_id;
		try {
			dictdealer.dictFetch(targeturl, 0);// 下载目标字典
			dictdealer.dictFetch(userdocurl, 1);// 下载用户名字典
			dictdealer.dictFetch(passdocurl, 2);// 下载密码字典
			String[] splitFileType = new String[2];
			splitFileType[0] = ".targets";
			splitFileType[1] = ".pwds";
			String fileName2Split = dictPath + File.separator + task_id + splitFileType[split - 1]; 
			flag = dictdealer.dictSplit(fileName2Split, nodecount);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}

	// 存储每个节点的任务
	public void setTaskForNodes(String task_id, String type, String targeturl,String userdocurl, String passdocurl, String custom, String url, int split) {
		List<Node> nodeList = nodedao.getNodeList();//节点列表
		long nodecount = nodeList.size();// 节点数量
		String[] splitFileType = { ".targets", ".pwds" };
		//添加对单个节点的判断 2016年7月31日20:06:48
		if(nodecount == 1){
			TaskForNodes tasknode = new TaskForNodes(task_id, nodeList.get(0).getNode_id(), type, targeturl+"&token=" +token, userdocurl+"&token=" +token,
					passdocurl+"&token=" +token, custom);
			tasknodedao.addTaskForNodes(tasknode);
			return;
		}
		for (int i = 0; i < nodecount; i++) {
			String splitedDocURL = url + splitFileType[split - 1] + "_" + (i + 1);
			TaskForNodes tasknode = new TaskForNodes() ;
			if (split == 1) {
				targeturl = splitedDocURL;
				tasknode = new TaskForNodes(task_id, nodeList.get(i).getNode_id(), type, targeturl, userdocurl+"&token=" +token,
						passdocurl+"&token=" +token, custom);
			} else if (split == 2) {
				passdocurl = splitedDocURL;
				tasknode = new TaskForNodes(task_id, nodeList.get(i).getNode_id(), type, targeturl+"&token=" +token, userdocurl+"&token=" +token,
						passdocurl, custom);
			}
			tasknodedao.addTaskForNodes(tasknode);
		}
	}

	public String getSavePath() {
		return savePath;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
	
}
