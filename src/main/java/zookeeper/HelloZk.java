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
 * eclipse此处为Client端,CentOS为zookeeperd Server端
 * 1.通过java程序,新建连接zk,类似为jdbc的connection,open.session
 * 2.新建一个znode节点/atguigu炳设置hello1018,等同于create /atguigu hello1018
 * 3.获得当前节点/atguigu的最新值      get /atguigu
 * 4.关闭连接
 * @author pc
 *
 */
public class HelloZk {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(HelloZk.class);

	//实例常量
	private static final String CONNECTSTRING="192.168.40.128";
	private static final int SESSION_TIMEOUT=20*1000;
	private static final  @Getter String PATH="/atguigu";
	
	//实例变量
	
	/**
	 * @throws IOException 
	* @Title: startZK
	* @Description: 获得ZK的连接实例
	* @param @return    参数
	* @return ZooKeeper    返回类型
	* @throws
	 */
	public ZooKeeper startZk() throws IOException{
		return new ZooKeeper(CONNECTSTRING, SESSION_TIMEOUT, null);
		
	}
	
	/**
	 * 
	* @Title: createZnode
	* @Description: 创建一个节点并赋值
	* @param @param zk
	* @param @param path
	* @param @param data
	* @param @throws KeeperException
	* @param @throws InterruptedException    参数
	* @return void    返回类型
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
	* @Description: 获得节点的值
	* @param @param zk
	* @param @param path
	* @param @return
	* @param @throws KeeperException
	* @param @throws InterruptedException    参数
	* @return String    返回类型
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
	* @Description:关闭连接
	* @param @param zk
	* @param @throws InterruptedException    参数
	* @return void    返回类型
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
