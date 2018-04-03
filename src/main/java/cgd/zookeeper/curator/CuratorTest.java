package cgd.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

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
            Thread.sleep(5000);
            String s = curator.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT).forPath("/cgd/test", "hello".getBytes());
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
