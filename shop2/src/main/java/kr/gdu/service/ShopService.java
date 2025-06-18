package kr.gdu.service;

import java.io.File;

import java.util.Date;
import java.util.Iterator;
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

@Service
public class ShopService {

	@Autowired
	private ItemDao itemDao;
	
	@Autowired
	private SaleDao saleDao;
	
	@Autowired
	private SaleItemDao saleItemDao;

	public List<Item> itemList() {
		return itemDao.list();
	}

	public Item getItem(Integer id) {
		return itemDao.getItem(id);
	}

	public void itemCreate(Item item, HttpServletRequest request) {

		//업로드파일이 존재 시 
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			//프로젝트의 img폴더 하위에 저장
			String path =request.getServletContext().getRealPath("/")+"img/";
			uploadFileCreate(item.getPicture(),path);//폴더에업로드
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}

		int maxid = itemDao.maxId();
		item.setId(maxid+1); //현재 최대 id에 +1을 해준다.
		itemDao.insert(item);
	}


	private void uploadFileCreate(MultipartFile picture, String path) {
		String orgFile = picture.getOriginalFilename();//업로드된 파일명
		File f = new File(path);
		if(!f.exists()) {
			f.mkdirs();
		}		
		try {
			//transferTo 메서드를 호출하여 업로드된 파일을 지정된 경로에 저장
			picture.transferTo(new File(path+orgFile));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void itemUpdate(Item item, HttpServletRequest request) {
		//업로드파일이 존재 시 
		if(item.getPicture() != null && !item.getPicture().isEmpty()) {
			//프로젝트의 img폴더 하위에 저장
			String path =request.getServletContext().getRealPath("/")+"img/";
			uploadFileCreate(item.getPicture(),path);//폴더에업로드
			item.setPictureUrl(item.getPicture().getOriginalFilename());
		}
		
		itemDao.update(item);

	}

	public void deleteItem(Integer id) {
		itemDao.deleteItem(id);
		
	}

	public Sale checkend(User loginUser, Cart cart) {
		int maxSaleid = saleDao.getMaxSaleId(); //가장높은 주문번호 반환
		Sale sale = new Sale();
		sale.setSaleid(maxSaleid+1);
		sale.setUser(loginUser);
		sale.setUserid(loginUser.getUserid());  //세션정보(로그인)인 userid값 저장
		//sale.setSaledate(new Date()); insert시 now로 고정되어있음
		saleDao.insert(sale); //sale테이블에 해당객체의정보들 추가
		
		int seq = 0;
		//ItemSet : item 객체 , 수량이 존재
		List<ItemSet> itemSetList = cart.getItemSetList();
		for(ItemSet is : itemSetList) {
			//++seq : 2개이상 상품 동시 주문시 saleid가 같다. 
			//seq를 달리해 구분함
			SaleItem saleItem = new SaleItem(sale.getSaleid(), ++seq, is);
			sale.getItemList().add(saleItem);//sale의 list에 넣기
			saleItemDao.insert(saleItem);
		}
		return sale; //sale " 주문정보,고객정보,주문상품 등
	}

	public List<Sale> saleList(String userid) {
		List<Sale> list = saleDao.saleList(userid);//userid사용자의 주문정보목록
		System.out.println("list ::: "+list);
		for (Sale sa : list) {
			//saleItemList : 주문번호에맞는 주문상품목록
			List<SaleItem> saleItemList = saleItemDao.list(sa.getSaleid());
			System.out.println("saleItemList :::: "+saleItemList);
			for (SaleItem si : saleItemList) {
				Item item = itemDao.getItem(si.getItemid());
				si.setItem(item);
			}
			sa.setItemList(saleItemList);
		}
		return list;
	}

}
