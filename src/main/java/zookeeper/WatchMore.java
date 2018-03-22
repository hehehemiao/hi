package zookeeper;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import util.BaseZooKeeper;

/**
 * 
 * @Description: 
 *1	��ʼ��ZK�Ķ������
 * 		1.1	����ZK������
 * 		1.2	����/atguigu�ڵ㲢��ֵ
 * 		1.3	��øýڵ��ֵ
 * 
 * 2	watchmore
 * 		2.1	���ֵ֮������һ���۲���watcher�����/atguigu�ýڵ��ֵ�����˱仯��Ҫ��֪ͨClient�ˣ�һ����֪ͨ
 * 
 * 3	watchMore
 * 		3.1	���ֵ֮������һ���۲���watcher�����/atguigu�ýڵ��ֵ�����˱仯��Ҫ��֪ͨClient��,�����۲�
 * 		3.2	���ٴλ���µ�ֵ��ͬʱ��������һ���۲��ߣ������۲첢���ֵ
 * 		3.3	���ٴλ���µ�ֵ��ͬʱ��������һ���۲��ߣ������۲첢���ֵ.�����������ظ���������
 * @author zzyy
 * @date 2018��3��21��
 */
public class WatchMore extends BaseZooKeeper{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(WatchMore.class);


	/**
	 * @throws IOException 
	 * 
	 * 	������, ������	 For 1018
	 * 
	* @Title: startZK
	* @Description: ���ZK��session���Ӷ���ʵ��
	* @param @return
	* @param @throws IOException    ����
	* @return ZooKeeper    ��������
	* @throws
	 */
	public ZooKeeper startZk() throws IOException{
		
		return new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, new Watcher(){

			@Override
			public void process(WatchedEvent event) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	/**
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 * 
	* @Title: createZnode
	* @Description: �ٸ�����·���´���znode�ڵ㲢��ֵ
	* @param @param zk
	* @param @param path
	* @param @param data
	* @param @throws KeeperException
	* @param @throws InterruptedException    ����
	* @return void    ��������
	* @throws
	 */
	public void CreateZnode(String path,String data) throws KeeperException, InterruptedException{
		zk.create(PATH, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}
	
	/**
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 * 
	* @Title: getZnode
	* @Description: ��ȡ���Ƕ�Ӧ�ڵ��ֵ
	* @param @param zk
	* @param @param path
	* @param @return
	* @param @throws KeeperException
	* @param @throws InterruptedException    ����
	* @return String    ��������
	* @throws
	 */
	public String GetZnode(String path) throws KeeperException, InterruptedException{
		String result="";
		byte[] data = zk.getData(PATH, new Watcher(){

			@Override
			public void process(WatchedEvent event) {
				try {
					triggerValue(path);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
		}, new Stat());
		result=new String(data);
		oldValue=result;
		
		return result;
	}

	public  boolean triggerValue(String path) throws KeeperException, InterruptedException {
		String result="";
		byte[] data = zk.getData(path, new Watcher(){

			@Override
			public void process(WatchedEvent event) {
				try
				{
					triggerValue(path);
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			
		}, new Stat());
		result=new String(data);
		newValue=result;
		
		if(newValue.equals(oldValue)){
			logger.info("*********The value no changes*******");
			return false;
		}else{
			logger.info("********oldValue: "+oldValue+"\t newValue: "+newValue);
			oldValue=newValue;
			return true;
		}
		
		
	}
	public static void main(String[] args) throws Exception {
		WatchMore more=new WatchMore();
		more.setZk(more.getZk());
		if(more.getZk().exists(PATH, false)==null){
			more.CreateZnode(PATH, "AAA");
			String result = more.GetZnode(PATH);
			
			if(logger.isInfoEnabled()){
				logger.info("main(String[]) --------init String result=" + result);
			}else{
				logger.info("this node has already exists!!!!");
			}
			Thread.sleep(Long.MAX_VALUE);
			
		}
	}
	
	
}
