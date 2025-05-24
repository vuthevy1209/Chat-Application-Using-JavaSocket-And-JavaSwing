package services;

import models.Chat;
import models.Message;
import models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FakeDataService {
    private static FakeDataService instance;
    private Map<String, User> users;
    private Map<String, Chat> chats;
    private User currentUser;
    
    private FakeDataService() {
        users = new HashMap<>();
        chats = new HashMap<>();
        initializeData();
        
        // Set default current user for testing if needed
        if (currentUser == null) {
            System.out.println("Setting default user (David Moore) for testing");
            currentUser = users.get("1"); // Use David Moore as default user
        }
    }
    
    public static FakeDataService getInstance() {
        if (instance == null) {
            instance = new FakeDataService();
        }
        return instance;
    }
    
    private void initializeData() {
        // Create users
        User user1 = new User("1", "David Moore", "david@example.com", "password");
        User user2 = new User("2", "Jessica Drew", "jessica@example.com", "password");
        User user3 = new User("3", "Robert Chen", "robert@example.com", "password");
        User user4 = new User("4", "Anna Smith", "anna@example.com", "password");
        User user5 = new User("5", "Chatgram", "chatgram@example.com", "password");
        User user6 = new User("6", "Announcements", "announcements@example.com", "password");
        
        users.put(user1.getId(), user1);
        users.put(user2.getId(), user2);
        users.put(user3.getId(), user3);
        users.put(user4.getId(), user4);
        users.put(user5.getId(), user5);
        users.put(user6.getId(), user6);
        
        // Set users online
        user1.setOnline(true);
        user2.setOnline(true);
        user3.setOnline(true);
        user4.setOnline(true);
        user5.setOnline(true);
        user6.setOnline(true);
        
        // Create direct chats
        Chat chat1 = new Chat("1", "David Moore", false);
        chat1.addParticipant("1");
        chat1.addParticipant("2");
        
        Chat chat2 = new Chat("2", "Robert Chen", false);
        chat2.addParticipant("1");
        chat2.addParticipant("3");
        
        Chat chat3 = new Chat("3", "Anna Smith", false);
        chat3.addParticipant("1");
        chat3.addParticipant("4");
        
        // Create group chats
        Chat chatGroup1 = new Chat("4", "Chatgram", true);
        chatGroup1.addParticipant("1");
        chatGroup1.addParticipant("2");
        chatGroup1.addParticipant("3");
        chatGroup1.addParticipant("5");
        
        Chat chatGroup2 = new Chat("5", "Announcements", true);
        chatGroup2.addParticipant("1");
        chatGroup2.addParticipant("2");
        chatGroup2.addParticipant("3");
        chatGroup2.addParticipant("4");
        chatGroup2.addParticipant("6");
        
        // Add chats to the map
        chats.put(chat1.getId(), chat1);
        chats.put(chat2.getId(), chat2);
        chats.put(chat3.getId(), chat3);
        chats.put(chatGroup1.getId(), chatGroup1);
        chats.put(chatGroup2.getId(), chatGroup2);
        
        // Generate timestamps
        LocalDateTime now = LocalDateTime.now();
        
        // Conversation with Jessica Drew
        addConversationWithJessica(chat1, now);
        
        // Conversation with Robert Chen
        addConversationWithRobert(chat2, now);
        
        // Conversation with Anna Smith
        addConversationWithAnna(chat3, now);
        
        // Group conversation in Chatgram
        addGroupConversationInChatgram(chatGroup1, now);
        
        // Group conversation in Announcements
        addGroupConversationInAnnouncements(chatGroup2, now);
    }
    
    private void addConversationWithJessica(Chat chat, LocalDateTime now) {
        // Messages from a week ago
        LocalDateTime weekAgo = now.minusDays(7);
        
        Message msg1 = createMessage("2", "1", "Hey David, have you started on the new project requirements?", weekAgo.withHour(10).withMinute(15));
        Message msg2 = createMessage("1", "2", "Not yet, I was planning to review them this afternoon", weekAgo.withHour(10).withMinute(20));
        Message msg3 = createMessage("2", "1", "Cool. Let's sync up tomorrow to discuss our approach?", weekAgo.withHour(10).withMinute(25));
        Message msg4 = createMessage("1", "2", "Sounds good 👍 I'll have some ideas ready by then", weekAgo.withHour(10).withMinute(30));
        
        // Day before yesterday messages
        LocalDateTime twoDaysAgo = now.minusDays(2);
        
        Message msg5 = createMessage("2", "1", "Hey David, are you coming to the meeting tomorrow?", twoDaysAgo.withHour(9).withMinute(15));
        Message msg6 = createMessage("1", "2", "Yes, I'll be there. What time is it again?", twoDaysAgo.withHour(9).withMinute(20));
        Message msg7 = createMessage("2", "1", "10:30 AM in the main conference room", twoDaysAgo.withHour(9).withMinute(22));
        Message msg8 = createMessage("1", "2", "Great, thanks for the reminder!", twoDaysAgo.withHour(9).withMinute(25));
        Message msg9 = createMessage("2", "1", "No problem. Don't forget to bring your laptop for the demo", twoDaysAgo.withHour(9).withMinute(30));
        Message msg10 = createMessage("1", "2", "Will do. I'll make sure it's all set up and ready", twoDaysAgo.withHour(9).withMinute(35));
        
        // Yesterday messages
        LocalDateTime yesterday = now.minusDays(1);
        
        Message msg11 = createMessage("2", "1", "Hey David, did you bring the project report to the meeting?", yesterday.withHour(11).withMinute(5));
        Message msg12 = createMessage("1", "2", "Yes, I shared it during the presentation", yesterday.withHour(11).withMinute(8));
        Message msg13 = createMessage("2", "1", "Great! I missed that part. Could you email it to me?", yesterday.withHour(11).withMinute(10));
        Message msg14 = createMessage("1", "2", "Just sent it 👍", yesterday.withHour(11).withMinute(15));
        Message msg15 = createMessage("2", "1", "Got it, thanks!", yesterday.withHour(11).withMinute(16));
        Message msg16 = createMessage("2", "1", "BTW, the client was really impressed with your presentation", yesterday.withHour(14).withMinute(30));
        Message msg17 = createMessage("1", "2", "That's great to hear! I was nervous about it", yesterday.withHour(14).withMinute(45));
        Message msg18 = createMessage("2", "1", "You didn't show it at all. Very professional 👏", yesterday.withHour(14).withMinute(50));
        
        // Today messages - morning
        Message msg19 = createMessage("1", "2", "Morning Jessica! Ready for the team outing tonight?", now.withHour(9).withMinute(12));
        Message msg20 = createMessage("2", "1", "Absolutely! I've been looking forward to it all week 🎉", now.withHour(9).withMinute(15));
        Message msg21 = createMessage("1", "2", "Same here! It's been a stressful project", now.withHour(9).withMinute(18));
        Message msg22 = createMessage("2", "1", "For sure. We deserve to celebrate after that successful launch", now.withHour(9).withMinute(20));
        Message msg23 = createMessage("1", "2", "Definitely! Where are we meeting again?", now.withHour(9).withMinute(25));
        Message msg24 = createMessage("2", "1", "The Rooftop Bar at 7pm. I'll send you the address", now.withHour(9).withMinute(28));
        Message msg25 = createMessage("2", "1", "123 Downtown Avenue, 15th floor", now.withHour(9).withMinute(29));
        Message msg26 = createMessage("1", "2", "Perfect, got it!", now.withHour(9).withMinute(32));
        
        // Today messages - evening
        Message msg27 = createMessage("2", "1", "OMG 😊 do you remember what you did last night at the work night out?", now.withHour(18).withMinute(12));
        Message msg28 = createMessage("1", "2", "no haha", now.withHour(18).withMinute(16));
        Message msg29 = createMessage("1", "2", "i don't remember anything 😂", now.withHour(18).withMinute(18));
        Message msg30 = createMessage("2", "1", "You gave the most hilarious karaoke performance ever 🎤", now.withHour(18).withMinute(20));
        Message msg31 = createMessage("1", "2", "Oh no... what song did I sing?", now.withHour(18).withMinute(22));
        Message msg32 = createMessage("2", "1", "Don't Stop Believin' - complete with air guitar solo", now.withHour(18).withMinute(25));
        Message msg33 = createMessage("2", "1", "I have video evidence 📱", now.withHour(18).withMinute(26));
        Message msg34 = createMessage("1", "2", "Please don't share that with anyone! 🙈", now.withHour(18).withMinute(30));
        Message msg35 = createMessage("2", "1", "Too late, already posted in the team chat 😈", now.withHour(18).withMinute(32));
        Message msg36 = createMessage("1", "2", "You're kidding right? RIGHT?? 😱", now.withHour(18).withMinute(33));
        
        // Set read status
        msg1.setRead(true);
        msg2.setRead(true);
        msg3.setRead(true);
        msg4.setRead(true);
        msg5.setRead(true);
        msg6.setRead(true);
        msg7.setRead(true);
        msg8.setRead(true);
        msg9.setRead(true);
        msg10.setRead(true);
        msg11.setRead(true);
        msg12.setRead(true);
        msg13.setRead(true);
        msg14.setRead(true);
        msg15.setRead(true);
        msg16.setRead(true);
        msg17.setRead(true);
        msg18.setRead(true);
        msg19.setRead(true);
        msg20.setRead(true);
        msg21.setRead(true);
        msg22.setRead(true);
        msg23.setRead(true);
        msg24.setRead(true);
        msg25.setRead(true);
        msg26.setRead(true);
        msg27.setRead(true);
        msg28.setRead(true);
        msg29.setRead(true);
        msg30.setRead(true);
        msg31.setRead(true);
        msg32.setRead(true);
        msg33.setRead(true);
        msg34.setRead(true);
        msg35.setRead(true);
        msg36.setRead(false); // Last message unread
        
        // Add messages to chat
        chat.addMessage(msg1);
        chat.addMessage(msg2);
        chat.addMessage(msg3);
        chat.addMessage(msg4);
        chat.addMessage(msg5);
        chat.addMessage(msg6);
        chat.addMessage(msg7);
        chat.addMessage(msg8);
        chat.addMessage(msg9);
        chat.addMessage(msg10);
        chat.addMessage(msg11);
        chat.addMessage(msg12);
        chat.addMessage(msg13);
        chat.addMessage(msg14);
        chat.addMessage(msg15);
        chat.addMessage(msg16);
        chat.addMessage(msg17);
        chat.addMessage(msg18);
        chat.addMessage(msg19);
        chat.addMessage(msg20);
        chat.addMessage(msg21);
        chat.addMessage(msg22);
        chat.addMessage(msg23);
        chat.addMessage(msg24);
        chat.addMessage(msg25);
        chat.addMessage(msg26);
        chat.addMessage(msg27);
        chat.addMessage(msg28);
        chat.addMessage(msg29);
        chat.addMessage(msg30);
        chat.addMessage(msg31);
        chat.addMessage(msg32);
        chat.addMessage(msg33);
        chat.addMessage(msg34);
        chat.addMessage(msg35);
        chat.addMessage(msg36);
    }
    
    private void addConversationWithRobert(Chat chat, LocalDateTime now) {
        // Messages from a few days ago
        LocalDateTime threeDaysAgo = now.minusDays(3);
        
        Message msg1 = createMessage("3", "1", "Hey David, just wanted to let you know we got that new client project", threeDaysAgo.withHour(11).withMinute(30));
        Message msg2 = createMessage("1", "3", "That's great news Robert! What's the timeline?", threeDaysAgo.withHour(11).withMinute(42));
        Message msg3 = createMessage("3", "1", "We need to deliver the first phase in 3 weeks", threeDaysAgo.withHour(11).withMinute(45));
        Message msg4 = createMessage("1", "3", "Alright, I'll make sure my team is prepared", threeDaysAgo.withHour(11).withMinute(50));
        Message msg5 = createMessage("3", "1", "Perfect. We'll have a kickoff meeting tomorrow at 2pm", threeDaysAgo.withHour(12).withMinute(0));
        Message msg6 = createMessage("1", "3", "Added to my calendar 📆", threeDaysAgo.withHour(12).withMinute(5));
        
        // Yesterday messages
        LocalDateTime yesterday = now.minusDays(1);
        
        Message msg7 = createMessage("3", "1", "Hi David, can you help me with the frontend issue we're facing?", yesterday.withHour(14).withMinute(30));
        Message msg8 = createMessage("1", "3", "Sure, what's the problem?", yesterday.withHour(14).withMinute(45));
        Message msg9 = createMessage("3", "1", "The new navigation menu isn't working on mobile devices", yesterday.withHour(14).withMinute(50));
        Message msg10 = createMessage("1", "3", "Let me check that. Which browser are you testing on?", yesterday.withHour(14).withMinute(55));
        Message msg11 = createMessage("3", "1", "Chrome and Safari on iOS", yesterday.withHour(15).withMinute(0));
        Message msg12 = createMessage("3", "1", "It's really urgent as the client demo is tomorrow 😬", yesterday.withHour(15).withMinute(5));
        Message msg13 = createMessage("1", "3", "I understand, I'll prioritize it right now", yesterday.withHour(15).withMinute(10));
        Message msg14 = createMessage("3", "1", "Thanks, I owe you one!", yesterday.withHour(15).withMinute(12));
        Message msg15 = createMessage("1", "3", "Working on it... looks like a CSS specificity issue", yesterday.withHour(16).withMinute(30));
        Message msg16 = createMessage("3", "1", "That makes sense. Our stylesheets have gotten quite complex", yesterday.withHour(16).withMinute(35));
        Message msg17 = createMessage("1", "3", "We really should refactor them soon. I'll make a note of that", yesterday.withHour(16).withMinute(40));
        
        // Today messages - morning
        Message msg18 = createMessage("1", "3", "Good morning! I found the issue. There's a media query that's not working correctly", now.withHour(9).withMinute(10));
        Message msg19 = createMessage("1", "3", "I'll push a fix to the repository right now", now.withHour(9).withMinute(12));
        Message msg20 = createMessage("3", "1", "Awesome, thanks David! That was fast", now.withHour(9).withMinute(15));
        Message msg21 = createMessage("1", "3", "No problem. Let me know if you encounter any other issues", now.withHour(9).withMinute(20));
        
        // Today messages - afternoon
        Message msg22 = createMessage("3", "1", "Fix is working perfectly on my end! Client will be happy", now.withHour(14).withMinute(5));
        Message msg23 = createMessage("1", "3", "Great! Glad it's working now 😊", now.withHour(14).withMinute(10));
        Message msg24 = createMessage("3", "1", "By the way, are you joining us for the client presentation tomorrow?", now.withHour(14).withMinute(15));
        Message msg25 = createMessage("1", "3", "Yes, I'll be there to support you", now.withHour(14).withMinute(20));
        Message msg26 = createMessage("3", "1", "Perfect! I was thinking we could meet 30 minutes before to prep?", now.withHour(14).withMinute(25));
        
        // Set read status
        msg1.setRead(true);
        msg2.setRead(true);
        msg3.setRead(true);
        msg4.setRead(true);
        msg5.setRead(true);
        msg6.setRead(true);
        msg7.setRead(true);
        msg8.setRead(true);
        msg9.setRead(true);
        msg10.setRead(true);
        msg11.setRead(true);
        msg12.setRead(true);
        msg13.setRead(true);
        msg14.setRead(true);
        msg15.setRead(true);
        msg16.setRead(true);
        msg17.setRead(true);
        msg18.setRead(true);
        msg19.setRead(true);
        msg20.setRead(true);
        msg21.setRead(true);
        msg22.setRead(true);
        msg23.setRead(true);
        msg24.setRead(true);
        msg25.setRead(true);
        msg26.setRead(false); // Last message unread
        
        // Add messages to chat
        chat.addMessage(msg1);
        chat.addMessage(msg2);
        chat.addMessage(msg3);
        chat.addMessage(msg4);
        chat.addMessage(msg5);
        chat.addMessage(msg6);
        chat.addMessage(msg7);
        chat.addMessage(msg8);
        chat.addMessage(msg9);
        chat.addMessage(msg10);
        chat.addMessage(msg11);
        chat.addMessage(msg12);
        chat.addMessage(msg13);
        chat.addMessage(msg14);
        chat.addMessage(msg15);
        chat.addMessage(msg16);
        chat.addMessage(msg17);
        chat.addMessage(msg18);
        chat.addMessage(msg19);
        chat.addMessage(msg20);
        chat.addMessage(msg21);
        chat.addMessage(msg22);
        chat.addMessage(msg23);
        chat.addMessage(msg24);
        chat.addMessage(msg25);
        chat.addMessage(msg26);
    }
    
    private void addConversationWithAnna(Chat chat, LocalDateTime now) {
        // Messages from last month
        LocalDateTime lastMonth = now.minusDays(30);
        
        Message msg1 = createMessage("4", "1", "Hi David, welcome to the marketing team! I'm Anna, the Marketing Director", lastMonth.withHour(9).withMinute(0));
        Message msg2 = createMessage("1", "4", "Hi Anna, thanks for the warm welcome! Looking forward to working with you", lastMonth.withHour(9).withMinute(5));
        Message msg3 = createMessage("4", "1", "Great to have you onboard! Let me know if you have any questions", lastMonth.withHour(9).withMinute(8));
        Message msg4 = createMessage("1", "4", "Will do, thanks!", lastMonth.withHour(9).withMinute(10));
        
        // Messages from two weeks ago
        LocalDateTime twoWeeksAgo = now.minusDays(14);
        
        Message msg5 = createMessage("1", "4", "Anna, I've finished analyzing the Q1 campaign data", twoWeeksAgo.withHour(15).withMinute(30));
        Message msg6 = createMessage("4", "1", "That was quick! What are the key findings?", twoWeeksAgo.withHour(15).withMinute(35));
        Message msg7 = createMessage("1", "4", "Social media campaigns performed 30% better than projected", twoWeeksAgo.withHour(15).withMinute(40));
        Message msg8 = createMessage("1", "4", "But email open rates were down 5% compared to last quarter", twoWeeksAgo.withHour(15).withMinute(41));
        Message msg9 = createMessage("4", "1", "Interesting... I wonder what caused the email decline", twoWeeksAgo.withHour(15).withMinute(45));
        Message msg10 = createMessage("1", "4", "I think our subject lines have become less effective. I can run an A/B test next month", twoWeeksAgo.withHour(15).withMinute(50));
        Message msg11 = createMessage("4", "1", "That's a great idea. Let's discuss it in the team meeting tomorrow", twoWeeksAgo.withHour(15).withMinute(55));
        
        // Yesterday messages
        LocalDateTime yesterday = now.minusDays(1);
        
        Message msg12 = createMessage("4", "1", "Hey David, I need your input on the new landing page design", yesterday.withHour(13).withMinute(10));
        Message msg13 = createMessage("1", "4", "Sure, what aspects do you want me to focus on?", yesterday.withHour(13).withMinute(15));
        Message msg14 = createMessage("4", "1", "Mainly conversion optimization and call-to-action placement", yesterday.withHour(13).withMinute(20));
        Message msg15 = createMessage("1", "4", "Got it. I'll review it this afternoon and get back to you", yesterday.withHour(13).withMinute(25));
        Message msg16 = createMessage("4", "1", "Perfect, thank you!", yesterday.withHour(13).withMinute(28));
        Message msg17 = createMessage("1", "4", "Just sent you my feedback via email. Overall looks great!", yesterday.withHour(16).withMinute(45));
        Message msg18 = createMessage("4", "1", "That was thorough, thanks! I'll implement your suggestions", yesterday.withHour(17).withMinute(10));
        
        // Today messages - morning
        Message msg19 = createMessage("4", "1", "Hi David, I'm working on the project presentation for tomorrow", now.withHour(10).withMinute(5));
        Message msg20 = createMessage("4", "1", "Could you send me the latest sales figures?", now.withHour(10).withMinute(6));
        Message msg21 = createMessage("1", "4", "Hi Anna, sure thing! I'll get them to you in an hour", now.withHour(10).withMinute(15));
        Message msg22 = createMessage("4", "1", "Perfect, thank you!", now.withHour(10).withMinute(17));
        Message msg23 = createMessage("1", "4", "Just sent the report to your email", now.withHour(11).withMinute(10));
        Message msg24 = createMessage("4", "1", "Got it, these numbers look really good for Q2!", now.withHour(11).withMinute(25));
        Message msg25 = createMessage("1", "4", "Yes, we've exceeded our targets by 15% 🎯", now.withHour(11).withMinute(28));
        Message msg26 = createMessage("4", "1", "That's going to make for a great presentation 😊", now.withHour(11).withMinute(30));
        
        // Today messages - afternoon
        Message msg27 = createMessage("4", "1", "David, the CEO just loved our presentation! Great job on those visuals", now.withHour(15).withMinute(5));
        Message msg28 = createMessage("1", "4", "That's fantastic news! Glad all our hard work paid off", now.withHour(15).withMinute(15));
        Message msg29 = createMessage("4", "1", "He wants us to present to the board next week", now.withHour(15).withMinute(17));
        Message msg30 = createMessage("1", "4", "Wow, that's a big opportunity! When exactly?", now.withHour(15).withMinute(20));
        Message msg31 = createMessage("4", "1", "Next Thursday at 2pm. Can you make it?", now.withHour(15).withMinute(22));
        
        // Set read status for all messages
        for (int i = 1; i <= 30; i++) {
            try {
                java.lang.reflect.Field msgField = getClass().getDeclaredField("msg" + i);
                msgField.setAccessible(true);
                Message message = (Message) msgField.get(this);
                message.setRead(true);
            } catch (Exception e) {
                // Handle exceptions silently
            }
        }
        
        // Set last message as unread
        try {
            msg31.setRead(false);
        } catch (Exception e) {
            // Handle exception silently
        }
        
        // Add all messages to chat
        for (int i = 1; i <= 31; i++) {
            try {
                java.lang.reflect.Field msgField = getClass().getDeclaredField("msg" + i);
                msgField.setAccessible(true);
                Message message = (Message) msgField.get(this);
                chat.addMessage(message);
            } catch (Exception e) {
                // Handle exceptions silently
            }
        }
    }
    
    private void addGroupConversationInChatgram(Chat chat, LocalDateTime now) {
        // Messages from last week
        LocalDateTime lastWeek = now.minusDays(7);
        
        Message msg1 = createMessage("5", null, "Welcome to the Chatgram group! This is a space for team discussions.", lastWeek.withHour(9).withMinute(0));
        Message msg2 = createMessage("1", null, "Thanks for setting this up!", lastWeek.withHour(9).withMinute(5));
        Message msg3 = createMessage("2", null, "Great to have everyone here!", lastWeek.withHour(9).withMinute(10));
        Message msg4 = createMessage("3", null, "Looking forward to our collaboration", lastWeek.withHour(9).withMinute(15));
        Message msg5 = createMessage("5", null, "Let's use this space to share updates, ideas, and coordinate on our projects", lastWeek.withHour(9).withMinute(20));
        
        // Messages from three days ago
        LocalDateTime threeDaysAgo = now.minusDays(3);
        
        Message msg6 = createMessage("3", null, "Hey team, I'm facing an issue with the authentication module in the app", threeDaysAgo.withHour(11).withMinute(20));
        Message msg7 = createMessage("3", null, "Users are getting logged out randomly after about 30 minutes", threeDaysAgo.withHour(11).withMinute(21));
        Message msg8 = createMessage("1", null, "That sounds like a token expiration issue. Have you checked the JWT timeout settings?", threeDaysAgo.withHour(11).withMinute(25));
        Message msg9 = createMessage("3", null, "Good point David, let me check that", threeDaysAgo.withHour(11).withMinute(30));
        Message msg10 = createMessage("2", null, "Also make sure the client is properly refreshing tokens before they expire", threeDaysAgo.withHour(11).withMinute(35));
        Message msg11 = createMessage("3", null, "You were right! The token refresh logic was missing. Fixed now, thanks everyone 🙏", threeDaysAgo.withHour(14).withMinute(15));
        
        // Yesterday messages
        LocalDateTime yesterday = now.minusDays(1);
        
        Message msg12 = createMessage("2", null, "Has anyone reviewed the new design mockups from the UI team?", yesterday.withHour(10).withMinute(5));
        Message msg13 = createMessage("1", null, "I went through them yesterday. They look really clean and modern!", yesterday.withHour(10).withMinute(15));
        Message msg14 = createMessage("3", null, "Yes, but I have some concerns about the mobile responsiveness", yesterday.withHour(10).withMinute(25));
        Message msg15 = createMessage("1", null, "What specifically are you concerned about?", yesterday.withHour(10).withMinute(30));
        Message msg16 = createMessage("3", null, "The navigation menu seems too complex for smaller screens", yesterday.withHour(10).withMinute(35));
        Message msg17 = createMessage("2", null, "I agree with Robert. We should simplify the menu for mobile", yesterday.withHour(10).withMinute(40));
        Message msg18 = createMessage("1", null, "I can work on an alternative design for mobile. Will share it by EOD", yesterday.withHour(10).withMinute(45));
        Message msg19 = createMessage("5", null, "Great discussion team! Let's finalize this in our design review tomorrow", yesterday.withHour(11).withMinute(0));
        
        // Today messages - morning
        Message msg20 = createMessage("5", null, "Just a reminder: We have a team meeting at 3PM today", now.withHour(8).withMinute(30));
        Message msg21 = createMessage("1", null, "I'll prepare the product roadmap update", now.withHour(8).withMinute(45));
        Message msg22 = createMessage("2", null, "And I'll cover the marketing strategy", now.withHour(9).withMinute(0));
        Message msg23 = createMessage("3", null, "I've shared the Q2 report in our shared folder for everyone to review before the meeting", now.withHour(10).withMinute(15));
        
        // Today messages - afternoon
        Message msg24 = createMessage("1", null, "Just pushed the mobile menu redesign to the dev branch if anyone wants to take a look", now.withHour(14).withMinute(5));
        Message msg25 = createMessage("3", null, "Checking it out now...", now.withHour(14).withMinute(10));
        Message msg26 = createMessage("3", null, "This looks much better David! The hamburger menu with categories is perfect 👌", now.withHour(14).withMinute(20));
        Message msg27 = createMessage("2", null, "Great work! This should solve the usability issues we were concerned about", now.withHour(14).withMinute(25));
        Message msg28 = createMessage("5", null, "Excellent progress team! See you all at the meeting in 30 minutes", now.withHour(14).withMinute(30));
        
        // Set read status for all messages
        for (int i = 1; i <= 27; i++) {
            try {
                java.lang.reflect.Field msgField = getClass().getDeclaredField("msg" + i);
                msgField.setAccessible(true);
                Message message = (Message) msgField.get(this);
                message.setRead(true);
            } catch (Exception e) {
                // Handle exceptions silently
            }
        }
        
        // Set last message as unread
        try {
            msg28.setRead(false);
        } catch (Exception e) {
            // Handle exception silently
        }
        
        // Add messages to chat
        for (int i = 1; i <= 28; i++) {
            try {
                java.lang.reflect.Field msgField = getClass().getDeclaredField("msg" + i);
                msgField.setAccessible(true);
                Message message = (Message) msgField.get(this);
                chat.addMessage(message);
            } catch (Exception e) {
                // Handle exceptions silently
            }
        }
    }
    
    private void addGroupConversationInAnnouncements(Chat chat, LocalDateTime now) {
        // Messages from a month ago
        LocalDateTime oneMonthAgo = now.minusDays(30);
        
        Message msg1 = createMessage("6", null, "🎉 Welcome to the company-wide Announcements channel!", oneMonthAgo.withHour(9).withMinute(0));
        Message msg2 = createMessage("6", null, "📱 IT Update: New mobile device policy is now available on the intranet", oneMonthAgo.withHour(13).withMinute(0));
        Message msg3 = createMessage("1", null, "Thanks for the update. Just reviewed the policy!", oneMonthAgo.withHour(13).withMinute(30));
        Message msg4 = createMessage("6", null, "✨ New Hire: Please welcome Alex Rodriguez who will be joining our Design team next Monday", oneMonthAgo.withHour(15).withMinute(0));
        
        // Messages from two weeks ago
        LocalDateTime twoWeeksAgo = now.minusDays(14);
        
        Message msg5 = createMessage("6", null, "🏢 Office Update: The renovation of the 3rd floor is now complete. New meeting rooms are available for booking", twoWeeksAgo.withHour(10).withMinute(0));
        Message msg6 = createMessage("2", null, "The new meeting rooms look fantastic!", twoWeeksAgo.withHour(10).withMinute(15));
        Message msg7 = createMessage("3", null, "Especially love the new tech setup in the conference room 👍", twoWeeksAgo.withHour(10).withMinute(25));
        Message msg8 = createMessage("6", null, "📆 Mark your calendars: Company-wide quarterly review next Tuesday at 10AM", twoWeeksAgo.withHour(13).withMinute(0));
        
        // Messages from last week
        LocalDateTime lastWeek = now.minusWeeks(1);
        
        Message msg9 = createMessage("6", null, "⚠️ Security Alert: Please complete the mandatory security training by the end of this week", lastWeek.withHour(9).withMinute(0));
        Message msg10 = createMessage("6", null, "🎓 Learning Opportunity: New courses on AWS certification have been added to our learning platform", lastWeek.withHour(14).withMinute(0));
        Message msg11 = createMessage("4", null, "I highly recommend the AWS Solutions Architect course - just completed it last month", lastWeek.withHour(14).withMinute(10));
        Message msg12 = createMessage("1", null, "Thanks for the recommendation Anna! I'll check it out", lastWeek.withHour(14).withMinute(15));
        
        // Yesterday
        LocalDateTime yesterday = now.minusDays(1);
        
        Message msg13 = createMessage("6", null, "📢 REMINDER: The office will be closed this Friday for building maintenance", yesterday.withHour(14).withMinute(0));
        Message msg14 = createMessage("6", null, "🏃 Wellness Program: Sign-ups for the company marathon team are now open!", yesterday.withHour(15).withMinute(30));
        Message msg15 = createMessage("2", null, "Just signed up! Who else is joining? 🏃‍♀️", yesterday.withHour(15).withMinute(45));
        Message msg16 = createMessage("3", null, "Count me in! Training starts tomorrow 💪", yesterday.withHour(16).withMinute(0));
        
        // Today - morning
        Message msg17 = createMessage("6", null, "☕ Reminder: New coffee machines have been installed in all break rooms. Training session today at noon!", now.withHour(8).withMinute(30));
        Message msg18 = createMessage("6", null, "🚀 New Product Launch: We're excited to announce that Project Phoenix will be launching next month!", now.withHour(10).withMinute(0));
        Message msg19 = createMessage("6", null, "📊 Q2 Financial Results: We've exceeded our targets by 12% this quarter! Great work everyone!", now.withHour(10).withMinute(45));
        
        // Today - afternoon
        Message msg20 = createMessage("6", null, "🏆 Congratulations to the Development team for completing the backend migration ahead of schedule!", now.withHour(11).withMinute(30));
        Message msg21 = createMessage("4", null, "Thanks for the recognition! It was a team effort 💪", now.withHour(11).withMinute(35));
        Message msg22 = createMessage("1", null, "Great job everyone!", now.withHour(11).withMinute(40));
        Message msg23 = createMessage("6", null, "📅 Save the Date: Company summer picnic will be held on July 15th at Riverside Park", now.withHour(15).withMinute(0));
        Message msg24 = createMessage("6", null, "👀 Sneak Peek: Marketing team just shared the new brand refresh mock-ups - check them out on the shared drive!", now.withHour(16).withMinute(15));
        
        // Set read status for all messages except the last one
        for (int i = 1; i <= 23; i++) {
            try {
                java.lang.reflect.Field msgField = getClass().getDeclaredField("msg" + i);
                msgField.setAccessible(true);
                Message message = (Message) msgField.get(this);
                message.setRead(true);
            } catch (Exception e) {
                // Handle exceptions silently
            }
        }
        
        // Set last message as unread
        try {
            msg24.setRead(false);
        } catch (Exception e) {
            // Handle exception silently
        }
        
        // Add messages to chat
        for (int i = 1; i <= 24; i++) {
            try {
                java.lang.reflect.Field msgField = getClass().getDeclaredField("msg" + i);
                msgField.setAccessible(true);
                Message message = (Message) msgField.get(this);
                chat.addMessage(message);
            } catch (Exception e) {
                // Handle exceptions silently
            }
        }
    }
    
    private Message createMessage(String senderId, String receiverId, String content, LocalDateTime timestamp) {
        Message message = new Message(UUID.randomUUID().toString(), senderId, receiverId, content);
        
        try {
            java.lang.reflect.Field timestampField = Message.class.getDeclaredField("timestamp");
            timestampField.setAccessible(true);
            timestampField.set(message, timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return message;
    }
    
    public boolean login(String username, String password) {
        // First, set the default user for testing if no matches are found
        boolean found = false;
        
        for (User user : users.values()) {
            if ((user.getUsername().equals(username) || user.getEmail().equals(username)) 
                    && user.getPassword().equals(password)) {
                currentUser = user;
                currentUser.setOnline(true);
                found = true;
                break;
            }
        }
        
        // If no user found but we're using a test username, log in as David for demo purposes
        if (!found && username.equals("test")) {
            currentUser = users.get("1"); // David Moore
            if (currentUser != null) {
                currentUser.setOnline(true);
                found = true;
            }
        }
        
        return found;
    }
    
    public boolean register(String email, String username, String password) {
        // Check if username or email already exists
        for (User user : users.values()) {
            if (user.getUsername().equals(username) || user.getEmail().equals(email)) {
                return false;
            }
        }
        
        // Create new user
        String id = UUID.randomUUID().toString();
        User newUser = new User(id, username, email, password);
        users.put(id, newUser);
        currentUser = newUser;
        currentUser.setOnline(true);
        return true;
    }
    
    public void logout() {
        if (currentUser != null) {
            currentUser.setOnline(false);
            currentUser = null;
        }
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public List<Chat> getUserChats() {
        List<Chat> userChats = new ArrayList<>();
        if (currentUser == null) {
            return userChats;
        }
        
        for (Chat chat : chats.values()) {
            if (chat.getParticipantIds().contains(currentUser.getId())) {
                userChats.add(chat);
            }
        }
        return userChats;
    }
    
    public List<User> getOnlineUsers() {
        List<User> onlineUsers = new ArrayList<>();
        for (User user : users.values()) {
            if (user.isOnline() && !user.getId().equals(currentUser.getId())) {
                onlineUsers.add(user);
            }
        }
        return onlineUsers;
    }
    
    public User getUserById(String id) {
        return users.get(id);
    }
    
    public Chat getChatById(String id) {
        return chats.get(id);
    }
    
    public void sendMessage(String chatId, String content) {
        if (currentUser == null || content.trim().isEmpty()) {
            return;
        }
        
        Chat chat = chats.get(chatId);
        if (chat == null) {
            return;
        }
        
        Message message = new Message(
            UUID.randomUUID().toString(),
            currentUser.getId(),
            chat.isGroup() ? null : chat.getParticipantIds().stream()
                .filter(id -> !id.equals(currentUser.getId()))
                .findFirst().orElse(null),
            content
        );
        
        chat.addMessage(message);
    }
    
    public void addChat(Chat chat) {
        if (chat != null && !chats.containsKey(chat.getId())) {
            chats.put(chat.getId(), chat);
        }
    }
}
