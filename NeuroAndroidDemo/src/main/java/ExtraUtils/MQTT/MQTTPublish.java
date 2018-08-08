package ExtraUtils.MQTT;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.json.simple.JSONObject;
import org.msgpack.MessagePack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import ExtraUtils.Configuration;
import ExtraUtils.URLUtils;

/**
 * Created by LinZh on 2/19/2016.
 */
public class MQTTPublish extends WorkerBase implements MqttCallback {
    private URLUtils mEndpoint;
    private int mQoS;
    private String mClientID;
    private String mOutputNode;         // The output node
    /**Private instance variables*/
    private MqttClient mClient;
    private String brokerUrl;
    private boolean quietMode;
    private MqttConnectOptions conOpt;
    private boolean clean;
    private String password;
    private String userName;
    /**sharing data*/
//    public ArrayList<JSONObject> mData = null;
    /**
     * constructor
     * @param endpoint
     * @param clientID
     * @param qos
     */
    public MQTTPublish(URLUtils endpoint, String clientID, int qos)
    {
        this.mEndpoint = endpoint;
        this.mQoS = qos;
        this.mClientID = clientID;
        /** MQTT Clien Callback*/
        this.brokerUrl= "tcp://" + mEndpoint.url;
        this.mClientID=clientID;
        this.clean = true;
        this.quietMode = true;
        this.userName=null;
        this.password = null;

        // init MQTT
        InitMQTT();
    }

    /**
     * Init MQTT
     */
    private void InitMQTT() {
        String tmpDir = System.getProperty("java.io.tmpdir");
        MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);
        try {
            /**Construct the connection options object that contains connection parameters*/
            conOpt = new MqttConnectOptions();
            conOpt.setCleanSession(clean);
            if (password != null) {
                conOpt.setPassword(this.password.toCharArray());
            }
            if (userName != null) {
                conOpt.setUserName(this.userName);
            }

            // Construct an MQTT blocking mode client
            this.mClient = new MqttClient(this.brokerUrl, this.mClientID, dataStore);

            // Set this wrapper as the callback handler
            this.mClient.setCallback(this);

        } catch (MqttException e) {
            e.printStackTrace();
            log("Unable to set up client: " + e.toString());
            System.exit(1);
        }
    }

    /**
     * Publish / send a bluetooth_message to an MQTT server
     *
     * @param topicName
     *            the name of the topic to publish to
     * @param qos
     *            the quality of service to delivery the bluetooth_message at (0,1,2)
     * @param payload
     *            the set of bytes to send to the MQTT server
     * @throws MqttException
     */
    public void publish(String topicName, int qos, byte[] payload)
            throws MqttException {
        // Connect to the MQTT server
        log("Connecting to " + brokerUrl + " with client ID " + mClient.getClientId());

        if(!mClient.isConnected()) {
            try {
                mClient.connect(conOpt);
            }catch (MqttException e) {
                log(e.toString());
            }
        }
//        log("Connected");
//      String time = new Timestamp(System.currentTimeMillis()).toString();
        log("Publishing to topic \"" + topicName + "\" qos " + qos);

        MqttMessage message = new MqttMessage(payload);
        message.setQos(qos);

        /** Send the bluetooth_message to the server, control is not returned until
         * it has been delivered to the server meeting the specified
         * quality of service.
         */
        try {
            mClient.publish(topicName, message);
        }catch (MqttException e) {
            log(e.toString());
        }
//		// Disconnect the client
//		mClient.disconnect();
//		log("Disconnected");
    }

//    /**
//     * Subscribe to a topic on an MQTT server Once subscribed this method waits
//     * for the messages to arrive from the server that match the subscription.
//     * It continues listening for messages until the enter key is pressed.
//     *
//     * @param topicName
//     *            to subscribe to (can be wild carded)
//     * @param qos
//     *            the maximum quality of service to receive messages at for this
//     *            subscription
//     * @throws MqttException
//     */
//    public void subscribe(String topicName, int qos) {
//
//        // Connect to the MQTT server
//        if(!mClient.isConnected()) {
//            try {
//                mClient.connect(conOpt);
//            } catch (MqttException e) {
//                log(e.toString());
//            }
//        }
//        log("Connected to " + brokerUrl + " with client ID " + mClient.getClientId());
//
//        // Subscribe to the requested topic
//        // The QoS specified is the maximum level that messages will be sent to
//        // the client at.
//        // For instance if QoS 1 is specified, any messages originally published
//        // at QoS 2 will
//        // be downgraded to 1 when delivering to the client but messages
//        // published at 1 and 0
//        // will be received at the same level they were published at.
//        log("Subscribing to topic \"" + topicName + "\" qos " + qos);
//        try {
//            mClient.subscribe(topicName, qos);
//        } catch (MqttException e) {
//            log(e.toString());
//        }
//
////		// Continue waiting for messages until the Enter is pressed
////		log("Press <Enter> to exit");
////		try {
////			System.in.read();
////		} catch (IOException e) {
////			// If we can't read we'll just exit
////		}
////
////		// Disconnect the client from the server
////		try {
////			mClient.disconnect();
////		} catch (MqttException e) {
////			log(e.toString());
////		}
////		log("Disconnected");
//    }

    /**
     * Utility method to handle logging. If 'quietMode' is set, this method does
     * nothing
     * @param message the bluetooth_message to log
     */
    private void log(String message) {
        if (!quietMode) {
            System.out.println(message);
        }
    }

    /**
     * disconnect mqtt
     */
    public void Disconnect(){
        // Disconnect the client from the server
        try {
            mClient.disconnect();
        } catch (MqttException e) {
            log(e.toString());
        }
        log("Disconnected");
    }

    @Override
    public void connectionLost(Throwable throwable) {
        log( "Connection to " + brokerUrl + " lost!" + throwable);
        //System.exit(1);
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        log("Subscribed message arrived in MQTTPublish");
        // Called when a bluetooth_message arrives from the server that matches any
        // subscription made by the client
        //String time = new Timestamp(System.currentTimeMillis()).toString();
        /**
         * If CONTENT-TYPE:JSON then, no need to use MsgPack, and simply use
         * new String(bytes, "UTF-8"), and the data would be parsed from JSONObject,
         *
         * However, if using msgpack, then should follow the below steps.
         * Attention in using MSGPACK
         * *This is very essential that Messagepack lib depends on Simple-Json
         * *and javassist.jar, what's more, the version of javassist should be
         * *complied using jdk version that matchs with your eclipse jdk version
         */
//        byte[] bytes = mqttMessage.getPayload();
//        log(s);
//        JSONObject temp = null;
//        if (Configuration.pack_json_msgpack)
//        {
//            temp = (JSONObject) JSONValue.parse(new String(bytes));
//        }else {
//            Value byteStr = MessagePack.unpack(bytes);
//            temp = (JSONObject) JSONValue.parse(byteStr.toString());
//        }
//        //get the data
//        if (this.mData != null)//
//        {
//            synchronized (this.mData)
//            {
//                this.mData.add(temp);
//            }
//        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log("Delivery to " + brokerUrl + " completed!");
        log("Delivery complete callback: Publish Completed "
                + Arrays.toString(iMqttDeliveryToken.getTopics()));
    }

    @Override
    public void RunWork(JSONObject mParas) {
        //String url = "tcp://" + mEndpoint.url;
        if (null == mClient) {
            InitMQTT();
        }
        /**
         * added Feb 25, 2016  lin zhang
         * function: add the ability to choose which node is been retrieving
         * */
        if (mParas == null) return;

        try {
            // transfer to different type
            byte[] message = null;
            synchronized (mParas)
            {
                if (Configuration.pack_json_msgpack) { /**using JSON*/
                    String str = mParas.toString();
                    //String str = mParas.toJSONString();
                    message = str.getBytes();//"UTF-8"
                } else {/**using MESSAGE_PACK*/
                    MessagePack msg_pack = new MessagePack();
                    message = msg_pack.write(mParas);
                }
                //mParas = null;
            }
            publish(mEndpoint.topic, this.mQoS, message);
        } catch (IOException e) {
            log(e.toString());
        } catch (MqttException e) {
            log(e.toString());
        }

//        mParaModified = true;
    }
}
