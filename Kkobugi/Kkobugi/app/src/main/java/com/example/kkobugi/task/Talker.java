package com.example.kkobugi.task;

import android.util.Log;

import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;

public class Talker extends AbstractNodeMain {
    private String topic_name;

    public Publisher<std_msgs.String> publisher;

    public Talker() {
        this.topic_name = "chatter";
    }

    public Talker(String topic) {
        this.topic_name = topic;
    }

    public GraphName getDefaultNodeName() {
        return GraphName.of("rosjava_tutorial_pubsub/talker");
    }

    public void onStart(ConnectedNode connectedNode){
        publisher = connectedNode.newPublisher(this.topic_name, "std_msgs/String");
    }

    public void publish(String message){
        Log.d("MyTag",message);
        std_msgs.String str = (std_msgs.String)publisher.newMessage();
        str.setData(message);
        publisher.publish(str);
    }
}
