package br.com.victorkk.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User implements UserDetails, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", unique = true)
    private String userName;

    @Column(name = "full_name")
    private String fullName;

    @Column     // parametro name é opcional caso o nome da coluna for igual
    private String password;

    @Column(name = "account_non_expired")
    private Boolean accountNonExpired;

    @Column(name = "account_non_locked")
    private Boolean accountNonLocked;

    @Column(name = "credentials_non_expired")
    private Boolean credentialsNonExpired;

    @Column
    private Boolean enabled;

    /**** MAPEAMENTO TABELA user_permission
     * - caso o relacionamento fosse mais complexo, seria necessário
     * outra classe para mapear, porém, como é simples, é possível
     * mapear o relacionamento na entidade mais forte da relação ****/

    @ManyToMany(fetch = FetchType.EAGER)
    // EAGER (Guloso): carregamento de permissões ocorre junto ao carregar um user
    // LAZY: ir carregando permissões aos poucos posteriormente
    @JoinTable(name = "user_permission", joinColumns = {@JoinColumn(name = "id_user")},
        inverseJoinColumns = {@JoinColumn(name = "id_permission")})
    private List<Permission> permissions;

    public User() {}

    public List<String> getRoles() {
        // Quebra de convenção do JPA, mas necessário para implementar User Details

        List<String> roles = new ArrayList<>();
        for (Permission permission : permissions) {
            roles.add(permission.getDescription());
        }
        return roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.permissions;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public Boolean getAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(userName, user.userName) && Objects.equals(fullName, user.fullName) && Objects.equals(password, user.password) && Objects.equals(accountNonExpired, user.accountNonExpired) && Objects.equals(accountNonLocked, user.accountNonLocked) && Objects.equals(credentialsNonExpired, user.credentialsNonExpired) && Objects.equals(enabled, user.enabled) && Objects.equals(permissions, user.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, fullName, password, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled, permissions);
    }
}
