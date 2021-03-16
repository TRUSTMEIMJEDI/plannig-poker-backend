//package com.marcinfriedrich.planningpoker.cache;
//
//import com.marcinfriedrich.planningpoker.model.Room;
//import com.marcinfriedrich.planningpoker.payload.UserAnswerResponse;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Controller
//public class VotingController {
//
//    private RoomCacheManager roomCacheManager;
//    private final SimpMessagingTemplate template;
//
//    public VotingController(SimpMessagingTemplate template) {
//        this.template = template;
//        this.roomCacheManager = RoomCacheManager.getInstance();
//    }
//
//    @MessageMapping("/send/answer")
//    public void sendMessage(String answer){
//        System.out.println(answer);
//        this.template.convertAndSend("/answer",  answer);
//    }
//
//    @MessageMapping("/send/room")
//    public void sendPassport(String room){
//        System.out.println(room);
//        this.template.convertAndSend("/room",  room);
//    }
//
//}
