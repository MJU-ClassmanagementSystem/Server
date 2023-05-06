package mju.capstone.cms.domain.auth.service;

import lombok.RequiredArgsConstructor;
import mju.capstone.cms.domain.auth.jwt.provider.JwtProvider;
import mju.capstone.cms.domain.parent.entity.Parent;
import mju.capstone.cms.domain.parent.repository.ParentRepository;
import mju.capstone.cms.domain.teacher.Repository.TeacherRepository;
import mju.capstone.cms.domain.teacher.entity.Teacher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final ParentRepository parentRepository;
    private final TeacherRepository teacherRepository;

    //있는 repository에서 비밀번호 같으면 해당 정보로 토큰생성해서 반환.
    public String loginToken(String id, String password) {
        if (teacherRepository.existsById(id)) {
            Teacher teacher = teacherRepository.findById(id).get();
            if (!teacher.getPassword().equals(password)) {
                throw new IllegalArgumentException("교사 비번 틀림!");
            }
            return jwtProvider.createToken(id, "teacher");
        } else if (parentRepository.existsById(id)) {
            Parent parent = parentRepository.findById(id).get();
            if (!parent.getPassword().equals(password)) {
                throw new IllegalArgumentException("학부모 비번 틀림!");
            }
            return jwtProvider.createToken(id, "parent");
        } else {
            throw new IllegalArgumentException("회원가입된 id 없음");
        }
    }

    //토큰 extract 해보기.
    public String me(String bearerToken) {
        return jwtProvider.extractId(bearerToken) + jwtProvider.extractType(bearerToken);
    }
}
