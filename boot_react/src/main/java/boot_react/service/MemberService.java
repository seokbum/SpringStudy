package boot_react.service;

import boot_react.entity.MemberEntity;
import boot_react.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    MemberRepository repository;

    public void memberInsert(MemberEntity memberEntity) {
        repository.save(memberEntity);
    }

    public MemberEntity getMember(String id) {
        return repository.findById(id).orElseGet(null);
    }
}
