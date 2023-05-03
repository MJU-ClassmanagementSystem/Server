package mju.capstone.cms.domain.parent.service;

import lombok.RequiredArgsConstructor;
import mju.capstone.cms.domain.parent.entity.Parent;
import mju.capstone.cms.domain.parent.repository.ParentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ParentService {

    private final ParentRepository parentRepository;

    //회원가입
    public String signup(String id, String password) {

        //이미 있는 아이디 -> 예외
        parentRepository.findById(id)
            .ifPresent(parent -> {
                throw new IllegalStateException("parent already exists");
            });

        Parent parent = Parent.builder()
            .id(id)
            .password(password)
            .build();

        Parent save = parentRepository.save(parent);
        return save.getId();
    }
}
