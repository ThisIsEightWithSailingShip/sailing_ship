package com.eight.sailingship.service.customer;


import com.eight.sailingship.dto.user.UserSignUpRequestDto;
import com.eight.sailingship.entity.User;
import com.eight.sailingship.entity.RoleEnum;
import com.eight.sailingship.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(UserSignUpRequestDto userSignUpRequestDto) {
        // 이메일과 비밀번호가 null 또는 빈 문자열인 경우 예외 발생

        System.err.println(userSignUpRequestDto.getEmail());

        if (userSignUpRequestDto.getEmail() == null || userSignUpRequestDto.getEmail().isEmpty() ||
                userSignUpRequestDto.getPassword() == null || userSignUpRequestDto.getPassword().isEmpty()) {
            throw new IllegalStateException("이메일과 패스워드가 빈 값이면 안 됨");
        }

        // 이메일이 이미 존재하는지 확인
        boolean isUser = userRepository.existsByEmail(userSignUpRequestDto.getEmail());
        if (isUser) {
            throw new IllegalStateException("이메일 이미 존재");
        }

        String nickname = userSignUpRequestDto.getNickname();
        if (nickname == null) {
            throw new IllegalStateException("닉네임 빈 값");
        }

        String encodedPassword = passwordEncoder.encode(userSignUpRequestDto.getPassword());

        RoleEnum role;
        if (userSignUpRequestDto.getRole().equals("사장님")) {
            role = RoleEnum.OWNER;
        } else {
            role = RoleEnum.CUSTOMER;
        }

        User user = new User(userSignUpRequestDto, encodedPassword, role);

        userRepository.save(user);
    }
}