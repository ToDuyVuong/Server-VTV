package hcmute.kltn.vtv.model.extra;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Role {


    CUSTOMER(Collections.emptySet()),

    VENDOR(Collections.emptySet()),

    ADMIN(Collections.emptySet()),

    PROVIDER(Collections.emptySet()),

    DELIVER(Collections.emptySet()),

    DELIVER_MANAGER(Collections.emptySet()),

    MANAGER(Collections.emptySet()),

    MANAGERCUSTOMER(Collections.emptySet()),

    MANAGERVENDOR(Collections.emptySet()),

    MANAGERSHIPPING(Collections.emptySet());





    private final Set<Permission> permissions;


    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
