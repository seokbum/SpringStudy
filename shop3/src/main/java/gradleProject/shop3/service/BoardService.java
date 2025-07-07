package gradleProject.shop3.service;

import gradleProject.shop3.domain.Board;
import gradleProject.shop3.domain.Comment;
import gradleProject.shop3.domain.CommentId;
import gradleProject.shop3.dto.BoardDto;
import gradleProject.shop3.mapper.BoardMapper;
import gradleProject.shop3.repository.BoardRepository;
import gradleProject.shop3.repository.CommRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final CommRepository commRepository;
    private final BoardMapper boardMapper;

    @Value("${upload.path}")
    private String uploadPath;

    public Page<BoardDto> boardlist(Integer pageNum, int limit, String boardid, String searchtype, String searchcontent) {
        Pageable pageable = PageRequest.of(pageNum - 1, limit,
                Sort.by(Sort.Order.desc("grp"), Sort.Order.asc("grpstep")));
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
        if (dto.getFile1() != null && !dto.getFile1().isEmpty()) {
            reply.setFileurl(uploadFileCreate(dto.getFile1()));
        }
        boardRepository.save(reply);
    }

    public List<Comment> commentList(int num) {
        return commRepository.findByNum(num);
    }

    public void comInsert(Comment comm) {
        int seq = commRepository.maxSeq(comm.getNum());
        comm.setSeq(seq + 1);
        commRepository.save(comm);
    }

    public Comment commSelectOne(int num, int seq) {
        CommentId id = new CommentId(num, seq);
        return commRepository.findById(id).orElse(null);
    }

    public void commDel(int num, int seq) {
        CommentId id = new CommentId(num, seq);
        commRepository.deleteById(id);
    }

    private String uploadFileCreate(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename)) return null;
        String filename = System.currentTimeMillis() + "_" + originalFilename;
        File targetFile = new File(uploadPath, "board/file/" + filename);
        targetFile.getParentFile().mkdirs();
        try {
            file.transferTo(targetFile);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    private Specification<Board> search(String boardid, String searchtype, String searchcontent) {
        return (root, query, builder) -> {
            if (StringUtils.hasText(searchtype) && StringUtils.hasText(searchcontent)) {
                return builder.and(
                        builder.equal(root.get("boardid"), boardid),
                        builder.like(root.get(searchtype), "%" + searchcontent + "%")
                );
            } else {
                return builder.equal(root.get("boardid"), boardid);
            }
        };
    }

    private List<String> readSidoFile(String si, String gu) {
        ClassPathResource resource = new ClassPathResource("data/sido.txt");
        Set<String> set = new LinkedHashSet<>();
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader fr = new BufferedReader(new InputStreamReader(inputStream))) {
            String data;
            while ((data = fr.readLine()) != null) {
                String[] arr = data.split("\\s+");
                if (arr.length < 2) continue;

                if (si == null && gu == null) {
                    if (arr.length >= 1) set.add(arr[0].trim());
                } else if (gu == null) {
                    if (arr.length >= 2 && arr[0].equals(si.trim()) && !arr[1].contains(arr[0])) {
                        set.add(arr[1].trim());
                    }
                } else {
                    if (arr.length >= 3 && arr[0].equals(si.trim()) && arr[1].equals(gu.trim()) && !arr[2].contains(arr[1])) {
                        if (arr.length > 3 && arr[3].contains(arr[1])) continue;
                        set.add(arr[2].trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return new ArrayList<>(set);
    }

    public List<String> sidoSelect1() {
        return readSidoFile(null, null);
    }

    public List<String> sidoSelect2(String si, String gu) {
        return readSidoFile(si, gu);
    }

    public String exchange1() {
        Document doc;
        List<List<String>> trlist = new ArrayList<>();
        String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01";
        String exdate = "";

        try {
            doc = Jsoup.connect(url).get();
            exdate = doc.select("p.table-unit").html();
            for (Element tr : doc.select("tr")) {
                List<String> tdlist = new ArrayList<>();
                for (Element td : tr.select("td")) {
                    tdlist.add(td.html());
                }
                if (tdlist.size() > 0) {
                    if ("USD".equals(tdlist.get(0)) || "CNH".equals(tdlist.get(0)) ||
                            "JPY(100)".equals(tdlist.get(0)) || "EUR".equals(tdlist.get(0))) {
                        trlist.add(tdlist);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<h4 class='title'>수출입은행<br>").append(exdate).append("</h4>");
        sb.append("<table class='table'><tr><th>통화</th><th>기준율</th><th>받으실때</th><th>보내실때</th></tr>");
        for (List<String> tds : trlist) {
            sb.append("<tr><td>").append(tds.get(0)).append("<br>").append(tds.get(1)).append("</td>")
                    .append("<td>").append(tds.get(4)).append("</td>")
                    .append("<td>").append(tds.get(2)).append("</td>")
                    .append("<td>").append(tds.get(3)).append("</td></tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }

    public Map<String, Object> exchange2() {
        Map<String, Object> map = new HashMap<>();
        List<List<String>> trlist = new ArrayList<>();
        String url = "https://www.koreaexim.go.kr/wg/HPHKWG057M01";

        try {
            Document doc = Jsoup.connect(url).get();

            String exdate = doc.select("p.date em").text();
            map.put("exdate", exdate);

            Elements trs = doc.select("div.table_wrap tbody tr");

            for (Element tr : trs) {
                List<String> tdlist = new ArrayList<>();
                for (Element td : tr.select("td")) {
                    tdlist.add(td.text());
                }
                if (tdlist.size() > 0) {
                    String currencyCode = tdlist.get(0).split("\\s+")[0];
                    if ("USD".equals(currencyCode) || "CNH".equals(currencyCode) ||
                            "JPY(100)".equals(currencyCode) || "EUR".equals(currencyCode)) {
                        trlist.add(tdlist);
                    }
                }
            }
            map.put("trlist", trlist);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map<String, Integer> graph1(String id) {
        List<Map<String, Object>> list = boardRepository.graph1(id);
        Map<String, Integer> map = new LinkedHashMap<>(); // 순서 보장을 위해 LinkedHashMap 사용
        for (Map<String, Object> m : list) {
            map.put((String) m.get("writer"), ((Number) m.get("cnt")).intValue());
        }
        return map;
    }

    public Map<String, Integer> graph2(String boardid) {
        // 1. 날짜 범위 계산
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime sevenDaysAgo = today.minusDays(7).with(LocalTime.MIN); // 7일 전 00:00:00

        // 2. Repository의 새로운 메서드 호출
        List<Map<String, Object>> list = boardRepository.graph2(boardid, sevenDaysAgo, today);

        // 3. 결과를 순서대로 담기 위해 LinkedHashMap 사용
        Map<String, Integer> map = new LinkedHashMap<>();
        for (Map<String, Object> m : list) {
            String regdate = (String) m.get("day");
            long cnt = (Long) m.get("cnt"); // Repository에서 Long 타입으로 반환될 수 있음
            map.put(regdate, (int) cnt);
        }
        return map;
    }

    public Map<String, Object> getLogo() {
        Map<String, Object> map = new HashMap<>();
        String url = "https://gudi.kr";
        try {
            Document doc = Jsoup.connect(url).get();
            Element logoElement = doc.select("img.scroll_logo").first();
            if (logoElement != null) {
                map.put("img", logoElement.attr("src"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}