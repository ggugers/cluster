package com.example.cluster.config;

import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.ha.session.ClusterSessionListener;
import org.apache.catalina.ha.session.DeltaManager;
import org.apache.catalina.ha.session.JvmRouteBinderValve;
import org.apache.catalina.ha.tcp.ReplicationValve;
import org.apache.catalina.ha.tcp.SimpleTcpCluster;
import org.apache.catalina.tribes.group.GroupChannel;
import org.apache.catalina.tribes.group.interceptors.MessageDispatchInterceptor;
import org.apache.catalina.tribes.group.interceptors.StaticMembershipInterceptor;
import org.apache.catalina.tribes.group.interceptors.TcpFailureDetector;
import org.apache.catalina.tribes.group.interceptors.TcpPingInterceptor;
import org.apache.catalina.tribes.membership.StaticMember;
import org.apache.catalina.tribes.transport.ReplicationTransmitter;
import org.apache.catalina.tribes.transport.nio.NioReceiver;
import org.apache.catalina.tribes.transport.nio.PooledParallelSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

public class TomcatStaticClusterContextCustomizer implements TomcatContextCustomizer {
    private String objname;

    public TomcatStaticClusterContextCustomizer() {
    }

    public TomcatStaticClusterContextCustomizer(String objname) {
        this.objname = objname;
    }

    @Override
    public void customize(final Context context) {
        context.setDistributable(true);

        DeltaManager manager = new DeltaManager();
        manager.setExpireSessionsOnShutdown(false);
        manager.setNotifyListenersOnReplication(true);
        context.setManager(manager);
        configureCluster((Engine) context.getParent().getParent());
    }
    private void configureCluster(Engine engine) {

        System.out.println(" ===================== objname : " + objname);

        //cluster
        SimpleTcpCluster cluster = new SimpleTcpCluster();
        cluster.setChannelStartOptions(3);
        cluster.setChannelSendOptions(8);

        //channel
        GroupChannel channel = new GroupChannel();

        StaticMembershipInterceptor staticMembershipInterceptor = new StaticMembershipInterceptor();

        if ("1".equals(objname)) {

            /** [WAS1] 설정 기준*/
            // 대상 정보 - was2정보
            StaticMember staticMember = new StaticMember();
            staticMember.setPort(4056);
            staticMember.setSecurePort(-1); // default
            staticMember.setHost("127.0.0.1");
            staticMember.setUniqueId("{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2}");
            staticMembershipInterceptor.addStaticMember(staticMember);

            //receiver(현재 자신의 정보) - was1
            NioReceiver receiver = new NioReceiver();
            receiver.setAddress("127.0.0.1");
            receiver.setMaxThreads(6);
            receiver.setPort(4055);  // was1: 4055, was2: 4056
            channel.setChannelReceiver(receiver);
        } else {

            /** [WAS2] 설정 기준 */
            // 대상 정보 - was1정보
            StaticMember staticMember = new StaticMember();
            staticMember.setPort(4055);
            staticMember.setSecurePort(-1); // default
            staticMember.setHost("127.0.0.1");
            staticMember.setUniqueId("{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1}");
            staticMembershipInterceptor.addStaticMember(staticMember);

            //receiver(현재 자신의 정보) - was2
            NioReceiver receiver = new NioReceiver();
            receiver.setAddress("127.0.0.1");
            receiver.setMaxThreads(6);
            receiver.setPort(4056);  // was1: 4055, was2: 4056
            channel.setChannelReceiver(receiver);
        }

        channel.addInterceptor(staticMembershipInterceptor);

        //sender
        ReplicationTransmitter sender = new ReplicationTransmitter();
        sender.setTransport(new PooledParallelSender());
        channel.setChannelSender(sender);

        //interceptor
        channel.addInterceptor(new TcpPingInterceptor());
        channel.addInterceptor(new TcpFailureDetector());
        channel.addInterceptor(new MessageDispatchInterceptor());
        cluster.addValve(new ReplicationValve());
        cluster.addValve(new JvmRouteBinderValve());
        cluster.setChannel(channel);
        cluster.addClusterListener(new ClusterSessionListener());
        engine.setCluster(cluster);
    }

}
