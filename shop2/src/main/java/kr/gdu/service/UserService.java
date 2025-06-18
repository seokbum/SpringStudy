package kr.gdu.service;

import java.io.File;
import java.io.FileInputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import kr.gdu.dao.UserDao;
import kr.gdu.logic.Mail;
import kr.gdu.logic.User;


@Service
public class UserService {

    private final SimpleMappingExceptionResolver exceptionHandler;
	
	@Autowired
	private UserDao dao;
	
	@Value("${resourse.dir}")
	private String RESOURCE_DIR;

    UserService(SimpleMappingExceptionResolver exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

	public void userInsert(User  user) {
		dao.insert(user);
	}

	/*public boolean selectUser(User user, HttpSession session) {
		String login = dao.selectUser(user);
		//로그인정보가없다면
		if(login==null) {
			return false;
		}
		else {
			session.setAttribute("loginUser", login);
			return true;
		}
		
	}*/
	
	public User selectUser(String userid) {
		return dao.selectOne(userid);
	}

	public void userUpdate( User user) {
		 dao.update(user);
	}

	public void userDelete(String userid) {
		dao.delete(userid);
	}

	public void changePw(User loginUser) {
		dao.changepw(loginUser);
		
	}

	public String getSearch(User user) {
		return dao.search(user);
	}

	public List<User> selectList() {
		return dao.list();
	}

	public List<User> getUserList(String[] idchks) {		
		return dao.list(idchks);
	}

	
	
	//메일전송을위한 내부클래스
	//Gmail 계정 인증을 위해 jakarta.mail.Authenticator를 상속받아 구현.
	private final class MyAuthenticator extends jakarta.mail.Authenticator{
		private String id;
		private String pw;
		
		public MyAuthenticator(String id, String pw) {
			this.id = id;
			this.pw = pw;
		}

		@Override
		protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
			return new jakarta.mail.PasswordAuthentication(id, pw);
		}		
	}
	
	public boolean mailSend( Mail mail) {
		String sender = mail.getGoogleid()+"@gmail.com";//자신의 gmail주소
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(RESOURCE_DIR+"mail.properties");
			prop.load(fis);
			prop.put("mail.smtp.user", sender);//보내는사람의 mail
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		//구글메일, 구글 앱 비밀번호 담기
		MyAuthenticator auth = new MyAuthenticator(sender, mail.getGooglepw());
		
		//session : 메일서버 접속객체
		Session session = Session.getInstance(prop,auth);
		//MimeMessage : SMTP 세션 객체를 받아 메시지 생성.
		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(sender)); //발신자 설정
			ArrayList<InternetAddress> addrs = new ArrayList<InternetAddress>();
			String[] emails = mail.getRecipient().split(",");
			for(String email:emails) {
				addrs.add(new InternetAddress(email));
			}
			//arr : 수신이메일목록
			InternetAddress[] arr = new InternetAddress[emails.length];
			for (int i = 0; i < addrs.size(); i++) {
				arr[i] = addrs.get(i);			
			}			
			msg.setRecipients(Message.RecipientType.TO, arr); //수신자설정
		
			msg.setSentDate(new Date());//날짜설정
			msg.setSubject(mail.getTitle());//
			MimeMultipart multipart = new MimeMultipart();
			MimeBodyPart message = new MimeBodyPart();
			message.setContent(mail.getContents(),mail.getMtype());
			multipart.addBodyPart(message);
			
			//첨부파일설정
			for(MultipartFile mf : mail.getFile1()) {
				if((mf!=null)&&(!mf.isEmpty())) {
					//첨부파일 추가
					multipart.addBodyPart(bodyPart(mf));
				}
			}
			msg.setContent(multipart);
			Transport.send(msg); //메일전송
			return true;
		}
		
		catch (MessagingException me) {
			me.printStackTrace();
		} 
		return false;
	}
	
	//첨부파일을 처리해 MimeBodyPart로 변환.
	private BodyPart bodyPart(MultipartFile mf) {		
		MimeBodyPart body = new MimeBodyPart();
		String orgFile = mf.getOriginalFilename();
		String path = RESOURCE_DIR+"mailupload/"; //업로드되는 폴더
		File f1 = new File(path);
		if(!f1.exists()) {
			f1.mkdir();
		}
		File f2 = new File(path+orgFile);
		try {
			mf.transferTo(f2);
			
			body.attachFile(f2);
			body.setFileName(orgFile);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return body;
	}

	public void mailfileDelete(Mail mail) {
		String path = RESOURCE_DIR+"mailupload/"; //업로드되는 폴더
		ArrayList<String> fileNames = new ArrayList<>();
		for (MultipartFile mf : mail.getFile1()) {
			fileNames.add(mf.getOriginalFilename());
		}	
		for(String f : fileNames) {
			File file = new File(path,f);
			System.out.println(file.toString()+"삭제 성공");
			file.delete();
		}
		
	}

	

	
	


}
