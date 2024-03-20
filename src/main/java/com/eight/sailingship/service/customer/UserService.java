package com.eight.sailingship.service.customer;


import com.eight.sailingship.dto.user.UserSignUpRequestDto;
import com.eight.sailingship.entity.User;
import com.eight.sailingship.entity.RoleEnum;
import com.eight.sailingship.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(UserSignUpRequestDto userSignUpRequestDto) {
// 이메일과 비밀번호가 null 또는 빈 문자열인 경우 예외 발생
        if (userSignUpRequestDto.getEmail() == null || userSignUpRequestDto.getEmail().isEmpty() ||
                userSignUpRequestDto.getPassword() == null || userSignUpRequestDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Email and password cannot be null or empty");
        }

        // 이메일이 이미 존재하는지 확인
        boolean isUser = userRepository.existsByEmail(userSignUpRequestDto.getEmail());
        if (isUser) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        User data = new User();

        data.setEmail(userSignUpRequestDto.getEmail());
        data.setPassword(passwordEncoder.encode(userSignUpRequestDto.getPassword()));
        data.setNickname(userSignUpRequestDto.getNickname());
        data.setAddress(userSignUpRequestDto.getAddress());
        data.setPhone(userSignUpRequestDto.getPhone());

        RoleEnum role;
        if (userSignUpRequestDto.getRole().equals("사장님")) {
            role = RoleEnum.OWNER;
            data.setAccount(0);
        } else {
            role = RoleEnum.CUSTOMER;
            data.setAccount(1000000);
        }
        data.setRole(role);

        userRepository.save(data);
    }
}