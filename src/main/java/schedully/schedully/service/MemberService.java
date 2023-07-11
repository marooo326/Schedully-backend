package schedully.schedully.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import schedully.schedully.domain.Member;
import schedully.schedully.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public List<Member> findAll(){
        return memberRepository.findAll();
    }

}
