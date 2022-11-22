package com.zlr.seat.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.vo
 * @Description
 * @create 2022-09-17-下午11:06
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentUserDetails extends UserVo implements UserDetails {

    /**
     * 鉴权的是从这里来的
     * @return
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (!CollectionUtils.isEmpty(permissionCode)) {
            return permissionCode.stream().map(this::createAuthority).collect(Collectors.toSet());
        }
        return Collections.emptyList();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return getNickname();
    }

    private GrantedAuthority createAuthority(String authority) {
        return (()->authority);
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return !getStatus().equals(3);
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !getStatus().equals(1);
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return !getStatus().equals(2);
    }
}

