package com.eight.sailingship.entity;

import com.eight.sailingship.dto.user.UserSignUpRequestDto;
import com.eight.sailingship.error.order.BalanceInsufficientException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
//    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "account")
    private Long account;

//    @Column(name = "is_owner", nullable = true)
//    private Boolean isOwner;


    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<Order> orderList;

    public User(UserSignUpRequestDto requestDto, String encodedPassword, RoleEnum role) {
        this.email = requestDto.getEmail();
        this.password = encodedPassword;
        this.role = role;
        this.nickname = requestDto.getNickname();
        this.address = requestDto.getAddress();
        this.phone = requestDto.getPhone();
        this.account = role == RoleEnum.CUSTOMER ? 1000000L : 0;
    }

    public void addOrderList(Order order) {
        this.orderList.add(order);
        order.setUser(this);
    }

    public void execPay(Long totalPrice) {
        if(account>=totalPrice){
            account -= totalPrice;
            return;
        }
        throw new BalanceInsufficientException("계좌의 잔액이 부족합니다");
    }
}

