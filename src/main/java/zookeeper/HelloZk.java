package zookeeper;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import lombok.Getter;
import lombok.Setter;
/**
 * eclipse�˴�ΪClient��,CentOSΪzookeeperd Server��
 * 1.ͨ��java����,�½�����zk,����Ϊjdbc��connection,open.session
 * 2.�½�һ��znode�ڵ�/atguigu������hello1018,��ͬ��create /atguigu hello1018
 * 3.��õ�ǰ�ڵ�/atguigu������ֵ      get /atguigu
 * 4.�ر�����
 * @author pc
 *
 */
public class HelloZk {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(HelloZk.class);

	//ʵ������
	private static final String CONNECTSTRING="192.168.40.128";
	private static final int SESSION_TIMEOUT=20*1000;
	private static final  @Getter String PATH="/atguigu";
	
	//ʵ������
	
	/**
	 * @throws IOException 
	* @Title: startZK
	* @Description: ���ZK������ʵ��
	* @param @return    ����
	* @return ZooKeeper    ��������
	* @throws
	 */
	public ZooKeeper startZk() throws IOException{
		return new ZooKeeper(CONNECTSTRING, SESSION_TIMEOUT, null);
		
	}
	
	/**
	 * 
	* @Title: createZnode
	* @Description: ����һ���ڵ㲢��ֵ
	* @param @param zk
	* @param @param path
	* @param @param data
	* @param @throws KeeperException
	* @param @throws InterruptedException    ����
	* @return void    ��������
	* @throws
	 */
	public void CreateZnode(ZooKeeper zk,String path,String data) throws KeeperException, InterruptedException{
		zk.create(path, data.getBytes(),Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}
	
	/**
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 * 
	* @Title: getZnode
	* @Description: ��ýڵ��ֵ
	* @param @param zk
	* @param @param path
	* @param @return
	* @param @throws KeeperException
	* @param @throws InterruptedException    ����
	* @return String    ��������
	* @throws
	 */
	public String getZnode(ZooKeeper zk,String path) throws KeeperException, InterruptedException{
		String result="";
		zk.getData(PATH, false, new Stat());
		return path;
		
	}
	
	/**
	 * @throws InterruptedException 
	 * 
	* @Title: stopZK
	* @Description:�ر�����
	* @param @param zk
	* @param @throws InterruptedException    ����
	* @return void    ��������
	* @throws
	 */
	
	public void stopZk(ZooKeeper zk) throws InterruptedException{
		if(zk!=null) zk.close();
	}
	public static void main(String[] args) throws Exception {
		HelloZk hello =new HelloZk();
		ZooKeeper zk = hello.startZk();
		if(zk.exists(PATH, false) ==null){
			hello.CreateZnode(zk, PATH, "hello1018");
			
			String result =hello.getZnode(zk, PATH);
			
			if(logger.isTraceEnabled()){
				logger.info("main(String[]) ***************** String result=" + result);
			}
		}else{
			logger.info("This node is exists......");
		}
		hello.stopZk(zk);
	}
}
