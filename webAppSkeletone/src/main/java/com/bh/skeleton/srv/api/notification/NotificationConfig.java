
package com.hs.gms.srv.api.notification;

/**
 * NotificationConfig.java
 * 
 * @author BH Jun
 */

//@Controller
//public class NotificationConfig {
//
//    //    @Qualifier("rabbitConnectionFactory")
//    //    private CachingConnectionFactory connectionFactory;
//    //    @Autowired
//    //    private RabbitAdmin rabbitAdmin;
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//    //    @Autowired
//    //    private DirectExchange directExchange;
//
//    //    @PostConstruct
//    //    public void init() {
//    //        //        this.createQueue();
//    //        this.queueTest();
//    //
//    //    }
//
//    @RequestMapping(name = "/queue/test", method = RequestMethod.GET)
//    public void queueTest() {
//        int index = 10;
//
//        while(index > 0) {
//            rabbitTemplate.convertAndSend("issueReqEvent", "{\"test" + index + "\": \"" + index + "_test입니다.\"}");
//            index--;
//        }
//    }

//    private void createQueue() {
//        String[] testQueueArr = {"10001.web.noti", "10002.web.noti"};
//
//        for(String testQueueName : testQueueArr) {
//            Queue tmpQueue = new Queue(testQueueName);
//
//            rabbitAdmin.declareQueue(tmpQueue);
//            rabbitAdmin.declareBinding(BindingBuilder.bind(tmpQueue).to(directExchange).with(testQueueName));
//            rabbitAdmin.declareBinding(BindingBuilder.bind(tmpQueue).to(directExchange).with("all.web.noti"));
//        }
//    }

//    public void setTemplate(){
//        rabbitTemplate.setExchange(exchange);
//    }
//}
