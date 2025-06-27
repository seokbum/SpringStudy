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

    @Value("${upload.path:C:/spring/upload/}")
    private String uploadPath;

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
        File targetFile = new File(uploadPath + "board/file/", filename);
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
}