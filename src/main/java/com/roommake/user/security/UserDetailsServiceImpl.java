package com.roommake.user.security;

import com.roommake.user.mapper.UserMapper;
import com.roommake.user.mapper.UserRoleMapper;
import com.roommake.user.vo.User;
import com.roommake.user.vo.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 사용자 정보를 조회한다.
        User user = userMapper.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }

        // 사용자 정보가 존재하면 해당 사용자의 권한정보를 조회한다.
        List<UserRole> userRoles = userRoleMapper.getUserRolesByUserId(user.getId());
        List<SimpleGrantedAuthority> authorities = getAuthorities(userRoles);

        // 조회된 사용자 정보로 UserDetails 구현 객체를 생성한다.
        return new UserDetailsImpl(user.getEmail(), user.getPassword(), authorities);
    }

    private List<SimpleGrantedAuthority> getAuthorities(List<UserRole> userRoles) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (UserRole userRole : userRoles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRole.getName());
            authorities.add(authority);
        }
        return authorities;
    }
}
