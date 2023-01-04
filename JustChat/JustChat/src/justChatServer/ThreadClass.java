package justChatServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ThreadClass extends Thread{

		Socket socket;
		DataInputStream in;
		DataOutputStream out;
		String nickname, rcvRoomName, roomChat;
		
		public ThreadClass(Socket socket) throws IOException {
			this.socket = socket;
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		}
		
		@Override
		public void run() { // ========== 클라에서 넘어오는 내용을 받아 다시 전달 ==========
			nickname = null;
			rcvRoomName = null;
			roomChat = null;
			try {
				while(in != null) {
					String sayClientMsgs = in.readUTF();
					// **************** 상황 모니터 console : 서버가 받는 값을 알려줌
					System.out.println("sayClient : "+sayClientMsgs);
					int port = socket.getPort();
					String[] sayClientMsg = sayClientMsgs.split("\\|"); 
					String key = sayClientMsg[0];
					switch(key) {
					case "fileSend":
						for (Entry<ThreadClass, String> entrySet2 : ServerMain.roomList2.entrySet()) {
							if(entrySet2.getValue().equals(rcvRoomName)) { // 룸이 같을 때
								if(entrySet2.getKey().nickname.equals(nickname)) {
									
									String[] fileSend = sayClientMsg[1].split("\\/");
									rcvRoomName = fileSend[0];
									nickname = fileSend[1];
									String filePath = fileSend[2];
									String sender = fileSend[3];
									System.out.println("File:" + filePath);
									
									File file = new File(filePath);
							        if (!file.exists()) {
							            System.out.println("File not Exist.");
//							            System.exit(0);
							            entrySet2.getKey().out.writeUTF("fileReceive|empty"); 
							        }
							        long fileSize = file.length();
							        long totalReadBytes = 0;
							        byte[] buffer = new byte[9999];
							        int readBytes;
							        
							        try {
										Socket fSocket = new Socket("127.0.0.1", 9999);
										FileInputStream fInStream  = new FileInputStream(filePath);
							        	OutputStream os = fSocket.getOutputStream();
							        	while ((readBytes = fInStream.read(buffer)) > 0) {
							        		os.write(buffer, 0, readBytes);
							        		totalReadBytes += readBytes;
							        		System.out.println("In progress: " + totalReadBytes + "/"
							        				+ fileSize + " Byte(s) ("
							        				+ (totalReadBytes * 100 / fileSize) + " %)");
						        		}
							        	
							        	filePath = filePath.replace(".", "_"+sender+".");

							        	// 서버에 읽어둔 파일을 저장. 사용자는 파일 이름을 통해 접근.
				    					entrySet2.getKey().out.writeUTF("fileReceive|"+filePath);
				    					System.out.println("File transfer completed.");
				    					
				    					String content = rcvRoomName+"/"+nickname+"/[ " + sender+ "(파일) ] " +filePath; 
				    					entrySet2.getKey().out.writeUTF("chatting|"+content); 
				    					fInStream.close();
				    					os.close();
				    					fSocket.close(); //************************************
							        } catch (Exception e) {}
								}
							}
				        }
						break;
					case "portCheck": // 새로운 socket 생성으로 인한 ThreadClass 생성시 새로 기록 
						// 닉네임/방이름
						for (Entry<ThreadClass, Integer> entrySet : ServerMain.threadList.entrySet()) { 
							if(entrySet.getValue()==port) { // 포트번호 같을 때 지우기
								ServerMain.threadList.remove(entrySet.getKey());
								ServerMain.roomList2.remove(entrySet.getKey());
							}
				        }
						break;
					case "nameReset":
						for (Entry<ThreadClass, String> entrySet2 : ServerMain.roomList2.entrySet()) {
							if(entrySet2.getValue().equals("현재 접속자수")) { 
								entrySet2.getKey().out.writeUTF("nameReset|");
							}
				        }
						break;
					case "allWaitName": 
						for(Entry<ThreadClass, String> entrySet : ServerMain.roomList2.entrySet()) {
							// 스레드 리스트
							if(entrySet.getValue().equals("현재 접속자수")) { 
								for(int i=0; i<ServerMain.nameList.size(); i++) {
									entrySet.getKey().out.writeUTF
									("allWaitName|"+ServerMain.nameList.get(i)); 
								}
							}
						}
						break;
					case "setNickname": // 닉네임-방으로 초기치 설정
						nickname = sayClientMsg[1];
						ServerMain.nameList.add(nickname);
						break;
					case "deleteWaitName":
						nickname = sayClientMsg[1]; // 닉네임
						ServerMain.nameList.remove(nickname);
						break;
					case "saveRoom": // 로그인시 :"대기실"
						rcvRoomName = sayClientMsg[1]; // 초기치 : 대기실
						for (Entry<ThreadClass, Integer> entrySet : 
									ServerMain.threadList.entrySet()) {
							if(entrySet.getValue()==port) { // 포트가 같으면
								ServerMain.roomList2.put(entrySet.getKey(), rcvRoomName);
								break; // 스레드/포트 list
							}
				        }
						break;
					case "setRoom": // 로그인시 
						Map<String,Integer> room = new HashMap<String, Integer>();
						for (Entry<ThreadClass, String> entrySet : ServerMain.roomList2.entrySet()) {
							String roomName = entrySet.getValue();
							if(room.containsKey(roomName)) {
								int number = room.get(roomName)+1;
								room.put(roomName, number);
							}
							else  // 방이 없을 때 
								room.put(roomName, 1);
				        }
						for (Entry<String, Integer> entrySet : room.entrySet()) {
							String roomName = entrySet.getKey();
							int count = entrySet.getValue();
							for (Entry<ThreadClass, Integer> entrySet2 : 
										ServerMain.threadList.entrySet()) {
								if(entrySet2.getValue()==port) {
									entrySet2.getKey().out.writeUTF
									("setRoom|"+roomName+"/"+count);
								}
					        }
				        }
						break;
					case "roomUpdateList":
						Map<String,Integer> room1 = new HashMap<String, Integer>();
						// roomList2 : 스레드/방이름
						for (Entry<ThreadClass, String> entrySet : ServerMain.roomList2.entrySet()) {
							String roomName = entrySet.getValue(); // 룸 설정
							if(room1.containsKey(roomName)) {
								int number = room1.get(roomName)+1;
								room1.put(roomName, number);
							}
							else  // 방이 없을 때 
								room1.put(roomName, 1);
				        }
						for (Entry<String, Integer> entrySet : room1.entrySet()) {
							String roomName = entrySet.getKey();
							int count = entrySet.getValue();
							for (Entry<ThreadClass, String> entrySet2 : 
										ServerMain.roomList2.entrySet()) {
								if(entrySet2.getValue().equals("현재 접속자수")) 
									entrySet2.getKey().out.writeUTF
									("roomUpdateList|"+roomName+"/"+count);
					        }
				        }
						break;
					case "roomResetList":
						for (Entry<ThreadClass, String> entrySet2 : ServerMain.roomList2.entrySet()) {
							if(entrySet2.getValue().equals("현재 접속자수"))  // 룸 이름이 같을 때 : 대기실 
								entrySet2.getKey().out.writeUTF("roomResetList|");
				        }
						break;
					case "chatting": // rcv방/내용
						String[] chat = sayClientMsg[1].split("\\/");
						for (Entry<ThreadClass, String> entrySet2 : 
										ServerMain.roomList2.entrySet()) {
							if(entrySet2.getValue().equals(chat[0]))  // 룸 이름이 같을 때 
								entrySet2.getKey().out.writeUTF("chatting|"+chat[1]); 
				        }
						break;
					case "secretChat": // rcv 방이름/귓속말받을자/내용
						String[] secretChat = sayClientMsg[1].split("\\/");
						rcvRoomName = secretChat[0];
						nickname = secretChat[1];
						String content = secretChat[2];
						for (Entry<ThreadClass, String> entrySet2 : 
									ServerMain.roomList2.entrySet()) {
							if(entrySet2.getValue().equals(rcvRoomName)) { // 룸이 같을 때
								if(entrySet2.getKey().nickname.equals(nickname)) 
									entrySet2.getKey().out.writeUTF("chatting|"+content); 
							}
				        }
						break;
					case "setChatList": // rcv닉네임/이름 -> inRoom put
						String[] setChatName = sayClientMsg[1].split("\\/");
						nickname = setChatName[0];
						rcvRoomName = setChatName[1];
						ServerMain.inRoomList.put(nickname, rcvRoomName);
						break;
					case "chatNameReset": // rcv방 이름
						// inRoomList : 닉넴/방이름 Hash
						for (Entry<ThreadClass, String> entrySet2 : 
									ServerMain.roomList2.entrySet()) { 
							if(entrySet2.getValue().equals(sayClientMsg[1]))  // 방 이름 같으면 
								entrySet2.getKey().out.writeUTF("chatNameReset|");
				        }
						break;
					case "allChatName": // 방/닉네임 -> 해당 방 명단 전달
						String[] chatNameMsg = sayClientMsg[1].split("\\/");
						rcvRoomName = chatNameMsg[0];
						nickname = chatNameMsg[1];
						for(Entry<String, String> entrySet : 
									ServerMain.inRoomList.entrySet()) { //닉넴/방
							if(entrySet.getValue().equals(rcvRoomName)) { // 방 이름 같을 때 
								for(Entry<ThreadClass, String> entryset2 :
													ServerMain.roomList2.entrySet()) {
									entryset2.getKey().out.writeUTF("allChatName|"+
											entrySet.getKey());
								}
							}
						}
						break;
					case "reChatName": // rcv닉네임
						nickname = sayClientMsg[1];
						ServerMain.inRoomList.remove(nickname);
						break;
					}
				}
			}catch(Exception e) {
			}finally {
				for (Entry<ThreadClass, Integer> entrySet : ServerMain.threadList.entrySet()) {
					if(entrySet.getValue()==socket.getPort()) {
						ServerMain.threadList.remove(entrySet.getKey());
						ServerMain.roomList2.remove(entrySet.getKey()); // 스레드/룸
						ServerMain.nameList.remove(nickname);
						for(Entry<ThreadClass, String> entrySet2 : ServerMain.roomList2.entrySet()) {
							if(entrySet2.getValue().equals("현재 접속자수")) { // 대기실에 있으면
								try {
									entrySet2.getKey().out.writeUTF("nameReset|"); // 리셋 날리고
								} catch (IOException e1) {}
								for(int i=0; i<ServerMain.nameList.size(); i++) { // 네임 리스트 돌면서
									try {// 각자에게 네임 리스트 출력
										entrySet2.getKey().out.writeUTF
										("allWaitName|"+ServerMain.nameList.get(i));
									} catch (IOException e) {}
								}
							}
						}
						try {
							for (Entry<ThreadClass, String> entrySet2 : 
											ServerMain.roomList2.entrySet()) {
								if(entrySet2.getValue().equals("현재 접속자수")) { // 룸 이름이 같을 때 : 대기실 
									entrySet2.getKey().out.writeUTF
									("chatting|----- [ "+nickname+" ] ----- 님 퇴장");
								}
					        }
						}catch (IOException e) {}
					}
		        }
				System.out.println("접속자 수 : "+ServerMain.threadList.size()+" 명\n");
		}
	}
}
