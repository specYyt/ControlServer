package com.control.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import com.control.exception.MyException;
import com.control.model.Node;
import com.control.util.DBCPUtil;

public class NodeDao {

	private final Logger logger = Logger.getLogger(NodeDao.class);
	private QueryRunner qr = new QueryRunner(DBCPUtil.getDataSource());

	// 查找Node_id是否存在
	public boolean findNodeId(Node node) {
		String sql = "select node_id from node where node_id = ?";
		Object[] params = { node.getNode_id() };
		try {
			node = qr.query(sql, new BeanHandler<Node>(Node.class), params);
		} catch (SQLException e) {
			logger.error("查找节点id出错"+ e.getMessage());
			e.printStackTrace();
		}
		if (node != null) {
			return true;
		}
		return false;
	}

	// 添加节点
	public void addNode(Node node) {
		try {
			String sql = "insert into node set node_id = ?, token = ?,join_time = ?";
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String join_time = df.format(new Date());
			Object params[] = { node.getNode_id(), node.getToken(), join_time };
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new MyException("添加节点出错");
		}
	}

	// 更新节点最后心跳时间
	public void updateNodeActiveTime(Node node) {
		try {
			String sql = "update node set last_active_time=? where node_id = ?";
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String join_time = df.format(new Date());
			Object params[] = { join_time, node.getNode_id() };
			qr.update(sql, params);
		} catch (SQLException e) {
			logger.error("更新心跳时间出错"+e.getMessage());
			e.printStackTrace();
		}
	}

	// 获得可用节点
	public List<Node> getNodeList() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currTime = df.format(new Date());
		String sql = "select * from node where TIMESTAMPDIFF(SECOND,last_active_time, ?) < 60";//最大延迟为60秒
		Object params[] = { currTime };
		try {
			return qr.query(sql, new BeanListHandler<Node>(Node.class), params);
		} catch (SQLException e) {
			throw new MyException("获得可用节点出错");
		}
	}

	//获得所有节点
	public List<Node> getAllNodeList() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currTime = df.format(new Date());
		String sql = "select *,TIMESTAMPDIFF(SECOND,last_active_time, ?) < 60 status from node";
		Object params[] = { currTime };
		try {
			return qr.query(sql, new BeanListHandler<Node>(Node.class), params);
		} catch (SQLException e) {
			throw new MyException("获得所有节点出错");
		}
	}

}
