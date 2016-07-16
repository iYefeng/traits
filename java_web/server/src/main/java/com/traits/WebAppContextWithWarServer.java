package com.traits;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class WebAppContextWithWarServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(38080);

        WebAppContext context = new WebAppContext();
        String root = System.getProperty("user.dir");
        System.out.println(root);
        context.setContextPath("/myapp");
        context.setWar("scheduler/target/scheduler-1.0-SNAPSHOT.war");
        //context.setWar("scheduler/target/scheduler.war");
        //context.setContextPath("/test");
        //context.setDescriptor(root + "/scheduler/out/artifacts/scheduler_war_exploded/WEB-INF/web.xml");
        //context.setResourceBase(root + "/scheduler/out/artifacts/scheduler_war_exploded/");
        server.setHandler(context);

        server.start();
        server.join();
    }
}