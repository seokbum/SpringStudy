package kr.gdu.service;

import java.io.File;
import java.util.List;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import kr.gdu.dao.ItemDao;
import kr.gdu.dao.SaleDao;
import kr.gdu.dao.SaleItemDao;
import kr.gdu.logic.Cart;
import kr.gdu.logic.Item;
import kr.gdu.logic.ItemSet;
import kr.gdu.logic.Sale;
import kr.gdu.logic.SaleItem;
import kr.gdu.logic.User;

@Service // @Component + Service : 객체화 + 서비스 기능
public class ShopService {
	
	@Autowired  // ItemDao 객체를 주입
	private ItemDao itemDao;
	
	@Autowired
	private SaleDao saleDao;
	
	@Autowired
	private SaleItemDao saleItemDao;
	
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

	public Sale checkend(User loginUser, Cart cart) {
		int maxsaleid = saleDao.getMaxSaleId(); // 최종 주문 번호 조회
		Sale sale = new Sale();
		sale.setSaleid(maxsaleid+1); //최종 주문번호 + 1
		sale.setUser(loginUser); // 로그인 정보
		sale.setUserid(loginUser.getUserid()); // db의 userid 값으로 저장
		saleDao.insert(sale); // sale 테이블에 추가
		int seq = 0;
		// ItemSet : Item 객체,수량
		for(ItemSet is : cart.getItemSetList()) {
			//sale.getSaleid() : 주문 번호
			//++seq : 주문 상품 번호
			SaleItem saleItem = new SaleItem(sale.getSaleid(),++seq,is);
			sale.getItemList().add(saleItem);
			saleItemDao.insert(saleItem);
		}
		return sale; // 주문 정보, 고객 정보, 주문 상품
	}

	public List<Sale> saleList(String userid) {
		// list : Sale 목록, db 정보만 저장
		List<Sale> list = saleDao.list(userid); // userid 사용자가 주문한 정보 목록 조회
		for(Sale sa : list) {
			//	saleItemList : 주문번호에 맞는 주문 상품 목록. db 정보만 조회
			List<SaleItem> saleItemList = saleItemDao.list(sa.getSaleid());
			for(SaleItem si : saleItemList) {
				Item item = itemDao.select(si.getItemid());// 상품 번호에 해당하는 상품 조회
				si.setItem(item); // 주문상품(SaleItem)에 상품 정보 저장.
			}
			sa.setItemList(saleItemList); // 주문 정보(Sale)에 주문 상품 저장
		}
		return list;
	}
}
