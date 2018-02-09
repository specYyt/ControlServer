package com.control.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.control.dao.NodeDao;
import com.control.model.Node;

public class NodeAction extends SuperAction {

	private static final long serialVersionUID = 1L;
	private String token;
	private List<Node> nodes = new ArrayList<Node>();
	private NodeDao nodedao = new NodeDao();
	private PrintWriter writer;
	private final Logger logger = Logger.getLogger(ReceiveAction.class);

	// 生成token,存入application
	public String execute() throws IOException {
		writer = response.getWriter();
		token = UUID.randomUUID().toString();
		application.setAttribute("NodeToken", token);
		logger.info("存储token成功,token=" + token);
		writer.print("{\"token\":\"" + token + "\"}");
		return null;
	}

	// 验证token,生成Node_id,加入集群，把节点id返回给节点。
	public String join() throws IOException {
		String Token = (String) application.getAttribute("NodeToken");
		if (Token != null && Token.equals(token)) {
			writer = response.getWriter();
			String node_id = UUID.randomUUID().toString();// 生成节点id
			Node node = new Node();
			node.setNode_id(node_id);
			node.setToken(token);
			nodedao.addNode(node);
			logger.info("添加Node成功,node_id:" + node_id);
			writer.print("{\"Node_id\":\"" + node_id + "\"}");
			application.removeAttribute("NodeToken");
		}
		return null;
	}

	// 显示所有节点
	public String listNodes() {
		nodes = nodedao.getAllNodeList();
		return SUCCESS;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

}
