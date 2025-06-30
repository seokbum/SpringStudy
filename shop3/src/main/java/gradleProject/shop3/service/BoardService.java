package gradleProject.shop3.service;

import gradleProject.shop3.domain.Board;
import gradleProject.shop3.domain.Comment;
import gradleProject.shop3.dto.BoardDto;
import gradleProject.shop3.mapper.BoardMapper;
import gradleProject.shop3.repository.BoardRepository;
import gradleProject.shop3.repository.CommRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommRepository commRepository;
    private final BoardMapper boardMapper;

    @Value("${summernote.imgupload}")
    private String UPLOAD_IMAGE_DIR;

    public Page<BoardDto> boardlist(Integer pageNum, int limit, String boardid, String searchtype, String searchcontent) {
        Pageable pageable = PageRequest.of(pageNum - 1, limit,
                Sort.by(Sort.Order.desc("grp"), Sort.Order.asc("grpstep"), Sort.Order.desc("regdate")));
        Specification<Board> spec = search(boardid, searchtype, searchcontent);
        Page<Board> entityPage = boardRepository.findAll(spec, pageable);
        List<BoardDto> dtoList = entityPage.getContent().stream()
                .map(boardMapper::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
    }

    public void boardWrite(BoardDto dto) {
        Board board = boardMapper.toEntity(dto);
        int maxNum = boardRepository.maxNum();
        board.setNum(maxNum + 1);
        board.setGrp(maxNum + 1);
        board.setRegdate(new Date());
        if (dto.getFile1() != null && !dto.getFile1().isEmpty()) {
            board.setFileurl(uploadFileCreate(dto.getFile1()));
        }
        boardRepository.save(board);
    }

    public Board getBoard(int num) {
        return boardRepository.findById(num).orElse(null);
    }

    public void addReadcnt(int num) {
        boardRepository.addReadcnt(num);
    }

    public void boardUpdate(BoardDto dto) {
        Board board = boardRepository.findById(dto.getNum()).orElseThrow(() -> new IllegalArgumentException("수정할 게시글이 없습니다."));
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setRegdate(new Date());
        if (dto.getFile1() != null && !dto.getFile1().isEmpty()) {
            board.setFileurl(uploadFileCreate(dto.getFile1()));
        }
    }

    public void boardDelete(int num) {
        boardRepository.deleteById(num);
    }

    public void boardReply(BoardDto dto) {
        boardRepository.grpStepAdd(dto.getGrp(), dto.getGrpstep());
        Board reply = boardMapper.toEntity(dto);
        int maxNum = boardRepository.maxNum();
        reply.setNum(maxNum + 1);
        reply.setGrplevel(dto.getGrplevel() + 1);
        reply.setGrpstep(dto.getGrpstep() + 1);
        reply.setRegdate(new Date());
        if (dto.getFile1() != null && !dto.getFile1().isEmpty()) {
            reply.setFileurl(uploadFileCreate(dto.getFile1()));
        }
        boardRepository.save(reply);
    }

    public List<Comment> commentList(int num) {
        return commRepository.findByNumOrderBySeqDesc(num);
    }

    public void comInsert(Comment comm) {
        int seq = commRepository.maxSeq(comm.getNum());
        comm.setSeq(seq + 1);
        comm.setRegdate(new Date());
        commRepository.save(comm);
    }

    @Transactional(readOnly = true)
    public Comment commSelectOne(int num, int seq) {
        return commRepository.findByNumAndSeq(num, seq).orElse(null);
    }

    public void commDel(int num, int seq) {
        commRepository.deleteByNumAndSeq(num, seq);
    }

    private String uploadFileCreate(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename)) return null;
        String filename = System.currentTimeMillis() + "_" + originalFilename;
        File targetFile = new File(UPLOAD_IMAGE_DIR  + "board/file/", filename);
        targetFile.getParentFile().mkdirs();
        try {
            file.transferTo(targetFile);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    private Specification<Board> search(String boardid, String searchtype, String searchcontent) {
        Specification<Board> spec = (root, query, builder) -> builder.equal(root.get("boardid"), boardid);
        if (StringUtils.hasText(searchtype) && StringUtils.hasText(searchcontent)) {
            spec = spec.and((root, query, builder) -> builder.like(root.get(searchtype), "%" + searchcontent + "%"));
        }
        return spec;
    }

//    public String sidoSelect1(String si, String gu) {
//        BufferedReader fr = null;
//        String path = "C:/upload/data/sido.txt";
//
//        try {
//            fr = new BufferedReader(new FileReader(path));
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//
//        Set<String> set = new LinkedHashSet<>();
//        String data = null;
//
//        if (si==null && gu==null) {
//            try {
//                while ((data=fr.readLine()) != null) {
//                    String[] arr = data.split("\\s+");
//                    if (arr.length >= 3) {
//                        set.add(arr[0].trim());
//                    }
//                }
//            } catch(IOException e) {
//                e.printStackTrace();
//            }
//        }
//        List<String> list = new ArrayList<String>(set);
//
//        return list.toString();
//    }
//
//    public List<String> sidoSelect2(String si, String gu) {
//        BufferedReader fr = null;
//        String path = "C:/upload/data/sido.txt";
//
//        try {
//            fr = new BufferedReader(new FileReader(path));
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//
//        Set<String> set = new LinkedHashSet<>();
//        String data = null;
//
//        if (si==null && gu==null) {
//            try {
//                while ((data=fr.readLine()) != null) {
//                    String[] arr = data.split("\\s+");
//                    if (arr.length >= 3) {
//                        set.add(arr[0].trim());
//                    }
//                }
//            } catch(IOException e) {
//                e.printStackTrace();
//            }
//        } else if (gu == null) {
//            si = si.trim();
//            try {
//                while ((data=fr.readLine()) != null) {
//                    String[] arr = data.split("\\s+");
//                    if (arr.length >= 3 && arr[0].equals(si) && !arr[1].contains(arr[0])) {
//                        set.add(arr[1].trim());
//                    }
//                }
//            } catch(IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            si = si.trim();
//            gu = gu.trim();
//            try {
//                while ((data=fr.readLine()) != null) {
//                    String[] arr = data.split("\\s+");
//                    if (arr.length >= 3 && arr[0].equals(si) && arr[1].equals(gu) && !arr[0].equals(arr[1]) && !arr[2].contains(arr[1])) {
//                        if (arr.length > 3) {
//                            if (arr[3].contains(arr[1])) {
//                                continue;
//                            }
//                        }
//                        set.add(arr[2].trim());
//                    }
//                }
//            } catch(IOException e) {
//                e.printStackTrace();
//            }
//        }
//        List<String> list = new ArrayList<String>(set);
//
//        return list;
//    }
//
//    public String exchange1() {
//        Document doc = null;
//        List<List<String>> trlist = new ArrayList<List<String>>();
//        String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01";
//        String exdate = null;
//
//        try {
//            doc = Jsoup.connect(url).get();
//            Elements trs = doc.select("tr");
//            exdate = doc.select("p.table-unit").html();
//
//            for (Element tr : trs) {
//                List<String> tdlist = new ArrayList<String>();
//                Elements tds = tr.select("td");
//                for (Element td : tds) {
//                    tdlist.add(td.html());
//                }
//                if (tdlist.size() > 0) {
//                    if (tdlist.get(0).equals("USD") || tdlist.get(0).equals("CNH") ||
//                            tdlist.get(0).equals("JPY(100)") || tdlist.get(0).equals("EUR")) {
//                        trlist.add(tdlist);
//                    }
//                }
//            }
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("<h4 class='title'> 수출입은행<br>" + exdate + "</h4>");
//        sb.append("<table class='table'>");
//        sb.append(
//                "<tr>"
//                        + "<th>통화</th>"
//                        + "<th>기준율</th>"
//                        + "<th>받으실때</th>"
//                        + "<th>보내실때</th>"
//                        + "</tr>"
//        );
//
//        for (List<String> tds : trlist) {
//            sb.append(
//                    "<tr> "
//                            + "<td>" + tds.get(0) + "<br>" + tds.get(1) + "</td>"
//                            + "<td>" + tds.get(4) + "</td>");
//            sb.append(
//                    "<td>" + tds.get(2) + "</td>"
//                            + "<td>" + tds.get(3) + "</td>"
//            );
//        }
//        sb.append("</table>");
//
//        return sb.toString();
//    }
//
//    public Map<String, Object> exchange2() {
//        Document doc = null;
//        List<List<String>> trlist = new ArrayList<List<String>>();
//        String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01";
//        String exdate = null;
//
//        try {
//            doc = Jsoup.connect(url).get();
//            Elements trs = doc.select("tr");
//            exdate = doc.select("p.table-unit").html();
//
//            for (Element tr : trs) {
//                List<String> tdlist = new ArrayList<String>();
//                Elements tds = tr.select("td");
//                for (Element td : tds) {
//                    tdlist.add(td.html());
//                }
//                if (tdlist.size() > 0) {
//                    if (tdlist.get(0).equals("USD") || tdlist.get(0).equals("CNH") ||
//                            tdlist.get(0).equals("JPY(100)") || tdlist.get(0).equals("EUR")) {
//                        trlist.add(tdlist);
//                    }
//                }
//            }
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("exdate", exdate);
//        map.put("trlist", trlist);
//
//        return map;
//    }
//
//    public Map<String, Integer> graph1(String id) {
//        // list : [{writer="김석범", cnt:10}, {writer="유동곤", cnt:7}...]
//        List<Map<String, Object>> list = boardDao.graph1(id);
//        Map<String, Integer> map = new HashMap<>();
//
//        for (Map<String, Object> m : list) {
//            String writer = (String) m.get("writer");
//            long cnt = (Long) m.get("cnt");
//            map.put(writer, (int) cnt);
//        }
//        return map;
//    }
//
//    public Map<String, Object> getLogo() {
//        Document doc = null;
//        String url = "https://gudi.kr";
//        String src = null;
//
//        try {
//            doc = Jsoup.connect(url).get();
//            Elements imgElements = doc.select("img.normal_logo._front_img");
//
//            // 선택된 요소가 있는지 확인
//            if (!imgElements.isEmpty()) {
//                Element img = imgElements.first();
//                src = img.attr("src");
//            }
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("logo", src);
//
//        return map;
//    }
//
//    public Map<String, Integer> graph2(String id) {
//
//        // list : [{regdate="2025-03-01", cnt:10}, {regdate="2025-05-01", cnt:5}...]
//        List<Map<String, Object>> list = boardDao.graph2(id);
//        Map<String, Integer> map = new HashMap<>();
//
//        for (Map<String, Object> m : list) {
//            String regdate = m.get("regdate").toString();
//            long cnt = (Long) m.get("cnt");
//            map.put(regdate, (int) cnt);
//        }
//        return map;
//    }
}