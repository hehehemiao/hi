package zookeeper;

import org.apache.log4j.Logger;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import util.BaseZooKeeper;

public class WatchOne extends BaseZooKeeper{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(WatchOne.class);
	

	
	/**
	 * @throws IOException 
	 * @throws IOException 
	* @Title: startZK
	* @Description: 获得ZK的连接实例
	* @param @return    参数
	* @return ZooKeeper    返回类型
	* @throws
	 */
	public ZooKeeper Startzk() throws IOException{
		
		return new ZooKeeper(CONNECTSTRING, SESSION_TIMEOUT, new Watcher(){

			public void process(WatchedEvent event) {
				
			}
			
		});
	}
	
	
	/**
	 * @throws InterruptedException 
	 * @throws KeeperException 
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
	public void createZnode(String path,String data) throws KeeperException, InterruptedException{
		zk.create(PATH, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
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
	public String getZnode( final String path) throws KeeperException, InterruptedException{
		
		String result="";
		byte[] data2 = zk.getData(path, new Watcher(){

			public void process(WatchedEvent event) {
				//业务逻辑,业绩出发了/atguigu节点的变更后,我需要立即获取最新值
				//将本部分的业务逻辑提出来,新变成一个方法
				try{
					triggerValue(path);
				}catch(KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}, new Stat());
		result =new String(data2);
		return result;
		
	}


	public  void triggerValue(String path) throws KeeperException, InterruptedException {
		String result="";
		byte[] data = zk.getData(PATH, null,new Stat());
		result = new String(data);
		logger.info("*********watcher after triggerValue result : "+result);
	}
	
	public static void main(String[] args) throws Exception {
		WatchOne one =new WatchOne();
		one.setZk(one.Startzk());
		if(one.getZk().exists(PATH, false)==null){
			one.createZnode(PATH, "AAA");
			String result =one.getZnode(PATH);
			logger.info("*********main init result : "+result);
		}else {
			logger.info("This node is exists......");
		}
		Thread.sleep(Long.MAX_VALUE);
	}
	
	
}
