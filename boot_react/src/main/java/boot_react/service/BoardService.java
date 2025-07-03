package boot_react.service;

import boot_react.entity.BoardEntity;
import boot_react.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    public int boardCount(String boardid) {
        Specification<BoardEntity> spec =
                (root, query, cri) -> cri.equal(root.get("boardid"), boardid);
        return (int) boardRepository.count(spec);
    }

    public Page<BoardEntity> boardList(Integer pageInt, int limit, String boardid) {
        Specification<BoardEntity> spec =
                (root, query, cri) -> cri.equal(root.get("boardid"), boardid);
        Pageable pageable = PageRequest.of(pageInt - 1, limit,
                Sort.by(Sort.Order.desc("num")));
        return  boardRepository.findAll(spec,pageable);
    }
}
