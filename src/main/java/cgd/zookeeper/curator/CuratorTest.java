package cgd.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

/**
 * @author cao.guo.dong
 * @create 2018-04-03 16:36
 **/
public class CuratorTest {
    public static void main(String[] args) {
        try {
            //重试策略，初试时间1秒，重试10次
            RetryPolicy policy = new ExponentialBackoffRetry(1000, 10);
            CuratorFramework curator = CuratorFrameworkFactory.builder().connectString("10.0.15.68:2181").connectionTimeoutMs(5000).
                    retryPolicy(policy).build();
            curator.start();
            Thread.sleep(3000);
            //CuratorFrameworkState state = curator.getState();
            //1.0：创建节点 creatingParentsIfNeeded：如果没有父节点，则创建父节点  withMode:节点类型，持久化节点，持久化有序节点，临时节点，临时有序节点
            /*String s = curator.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT).forPath("/cgd/test3", "hello".getBytes());
            System.out.println(s);*/

            //1.1：删除节点 withVersion:-1(表示不根据数据版本条件进行删除)
            /* curator.delete().withVersion(-1).forPath("/cgd/test");*/

            //1.2：判断节点是否存在
            curator.checkExists().usingWatcher(new CuratorWatcher() {
                @Override
                public void process(WatchedEvent watchedEvent) throws Exception {
                    if (Watcher.Event.EventType.NodeDeleted == watchedEvent.getType()) {
                        System.out.println("=============> node is deleted");
                    }

                    if(Watcher.Event.EventType.NodeDataChanged == watchedEvent.getType()){
                        System.out.println("=============> node is NodeDataChanged");
                    }

                    if(Watcher.Event.EventType.NodeChildrenChanged == watchedEvent.getType()){
                        System.out.println("=============> node is NodeChildrenChanged");
                    }

                    if(Watcher.Event.EventType.NodeCreated == watchedEvent.getType()){
                        System.out.println("=============> node is NodeCreated");
                    }

                }
            }).forPath("/cgd/test2");
            System.out.println("11111111111111111111");
            curator.setData().forPath("/cgd/test2", "afasfsad".getBytes());
            System.out.println("11111111111111111111");

            //获取节点
            /* byte[] bytes = curator.getData().forPath("/cgd/test");
            String getData = new String(bytes);
            System.out.println(getData);*/
            //关闭连接
            //System.in.read();
            curator.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
