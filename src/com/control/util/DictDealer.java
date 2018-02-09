package com.control.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Properties;

public class DictDealer {
	private String dictRootPath; // 根目录下是以任务id命名的文件夹，里面是对应任务的字(目标字典、用户名字典、密码字)
	private String task_ID;
	private String dictPath; // 任务的字典
	private static String token;
	
	static {
		Properties ps = new Properties();
		try {
			ps.load(DictDealer.class.getResourceAsStream("/token.properties"));
			token = ps.getProperty("token");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public DictDealer(String dictRootPath, String task_ID) {
		this.dictRootPath = dictRootPath;
		this.task_ID = task_ID;
		this.dictPath = this.dictRootPath + File.separator + task_ID;
	}

	/*
	 * 从密码攻击平台下载字 字典类型: 0 -> 目标列表; 1 -> 用户名字; 2 -> 密码字典
	 */
	public void dictFetch(String strURL, int dictType) throws IOException, MalformedURLException {
		switch (dictType) {
		case 0:
			downloadGET(strURL+"&token"+token, this.dictPath + File.separator + task_ID + ".targets");
			break;
		case 1:
			downloadGET(strURL+"&token"+token, this.dictPath + File.separator + task_ID + ".users");
			break;
		case 2:
			downloadGET(strURL+"&token"+token, this.dictPath + File.separator + task_ID + ".pwds");
			break;
		default:
			System.out.println("选择字典类型错!");
		}
	}

	// 计算后缀长度
	private static int getLenSuffix(int numNodes) {
		return (int) Math.ceil(Math.log(numNodes) / Math.log(26));
	}

	// 计算第index业后缀应昻, index0, (numNodes - 1)
	public static String getSuffix(int numNodes, int index) { // Modified by
																// Jingyang
																// 20160429
		if (index >= numNodes) {
			return null;
		}
		int len = getLenSuffix(numNodes);
		int[] arg0 = new int[len];
		for (int i = 0; i < arg0.length; i++) {
			arg0[len - 1 - i] = 97 + (int) ((index / Math.pow(26, i)) % 26);
		}
		return new String(arg0, 0, arg0.length);
	}

	public boolean dictSplit(String filePathName, long numChunks) throws Exception{ 

		File fin = new File(filePathName); 
		RandomAccessFile rndAccessFileIn = new RandomAccessFile(fin, "r");
		FileChannel fcin = rndAccessFileIn.getChannel(); 
		String outPath = fin.getParentFile().getCanonicalPath();
		String fileName = filePathName.substring(filePathName.lastIndexOf(File.separator));
		splitFileBySize(fcin, outPath, fileName, numChunks); 
		fcin.close();
		rndAccessFileIn.close();
		return true;
	} 
	
	private static void splitFileBySize(FileChannel fcin, String outPath, String outFileName, long n) throws IOException {
		String strEnter = "\n";
		long totalSize = fcin.size();
		long mappedSize = 0;
		long size = 0;
		for (int i = 0; i < n; i++){
			File fout = new File(outPath + File.separator + outFileName + "_" + (i + 1));
			fout.delete();
			RandomAccessFile rndAccessFileOut = new RandomAccessFile(fout, "rws");
			FileChannel fcout = rndAccessFileOut.getChannel();
						
			size = (long) (Math.ceil((double)(totalSize - mappedSize) / (n - i)));			
			
			
			MappedByteBuffer mappedByteBufferIn = fcin.map(FileChannel.MapMode.READ_ONLY, mappedSize, size);
			mappedByteBufferIn.rewind();
			int remain;
			int position = 0;
			while((remain = mappedByteBufferIn.remaining()) > 0){	
				int defaultSize = 1024 * 1024 * 10; // 如果这部分很大，就10M、10M地写
				byte[] dst = new byte[defaultSize > remain ? remain : defaultSize];
				mappedByteBufferIn.get(dst, 0, dst.length);
				String strDst = new String(dst);
				
				int size2 = strDst.lastIndexOf("\n");
				int size3 = strDst.lastIndexOf("\r");
				if (size2 - 1 == strDst.lastIndexOf("\r")){  // 判断换行符是哪个
					strEnter = "\r\n";
					size2--;
				}
				if (size3 != -1){
					strEnter = "\r\n";
					size2 = size3;
				}
				if (i == n-1){
					size2 = strDst.length();
				}
				if (size2 == -1)
					size2 = 0;
				else
					size2 += dst.length - strDst.length(); // 不可见字符
				
				/*byte[] dst2 = new byte[(int) size];
				mappedByteBufferIn.rewind();
				mappedByteBufferIn.get(dst2, 0, (int)size);
				*/
				//byte[] dst2 = new byte[(int)size];
				//mappedByteBufferIn.get(dst2, 0, (int)size);
				boolean lastRound = (size2 <= remain/*== remain - strEnter.length()*/ && remain < defaultSize);
				int sizeWrite = size2 + (lastRound ? 0 : strEnter.length());
				MappedByteBuffer mappedByteBufferOut = fcout.map(FileChannel.MapMode.READ_WRITE, position, sizeWrite);
				
				position += sizeWrite + (lastRound && size2 != 0 ? strEnter.length() : 0);
				mappedByteBufferOut.put(dst, 0, sizeWrite);
				mappedByteBufferOut.flip();
				int positionActually = position > size? (int)size: position;
				mappedByteBufferIn.position(positionActually);
				
				if (lastRound){
					break;
				}
			}
			mappedSize += position;
			fcout.close();
			rndAccessFileOut.close();
		}
	}
	
	
	private void downloadGET(String strURL, String filePathName) throws IOException, MalformedURLException {
		// 获得连接
		URL url = new URL(strURL+"&token="+token);//添加token验证  2016年6月23日11:35:02  wwb
		HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
		// 设置连接
		httpUrlConn.setRequestMethod("GET"); // GET 提交
		httpUrlConn.setConnectTimeout(30000); // 30 seconds timeout
		// 执连接
		while (true) {
			try {
				httpUrlConn.connect();
				break;
			} catch (java.net.SocketTimeoutException timeoutE) {
				System.out.println("连接超时,正在重试...");
			}
		}

		// 准接受返回的内,准文件
		int length = 1024;
		int lenReal;
		byte[] b = new byte[length];
		File f = new File(filePathName);
		if (!f.exists()) {
			f.getParentFile().mkdirs();
			f.createNewFile();
		}
		OutputStream out = new FileOutputStream(f);

		// 接受返回的内容
		InputStream in = httpUrlConn.getInputStream();

		// 写入文件
		while (-1 != (lenReal = in.read(b, 0, length)))
			out.write(b, 0, lenReal);

		out.close();
		in.close();
	}
}
