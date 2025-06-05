package service;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import dao.ItemDao;
import logic.Item;

@Service // @Component + Service : 객체화 + 서비스 기능
public class ShopService {
	@Autowired  // ItemDao 객체를 주입
	private ItemDao itemDao;
	
	public List<Item> itemList() {
		return itemDao.list();
	}
	
	public Item getItem(Integer id) {	
		return itemDao.select(id);
	}

	public void itemCreate(Item item, HttpServletRequest request) {
		// item.getPicture() : 업로드 된 파일이 존재. 파일의 내용 저장
		if(item.getPicture() !=null && !item.getPicture().isEmpty()) {
			// 업로드 폴더 지정
			String path = request.getServletContext().getRealPath("/")+"img/";
			uploadFileCreate(item.getPicture(),path);
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		int maxid = itemDao.maxId(); // db에서 id의 최대 값 조회
		item.setId(maxid +1); 
		itemDao.insert(item);
	}

	// 파일 업로드하기 
	private void uploadFileCreate(MultipartFile picture, String path) {
		String orgFile = picture.getOriginalFilename(); // 원본 파일의 이름
		File f = new File(path);
		if(!f.exists()) {
			f.mkdirs(); // 폴더가 없으면 생성
		}
		try {
			// picture : 파일의 내용
			// transferTo : picture 의 내용을 new File(path+orgFile)의 위치로 저장
			picture.transferTo(new File(path+orgFile));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void itemUpdate(Item item, HttpServletRequest request) {
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			String path = request.getServletContext().getRealPath("/")+"img/";
			uploadFileCreate(item.getPicture(),path);
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		itemDao.update(item);
	}

	public void itemDelete(Integer id) {
		itemDao.delete(id);		
	}
}
