package br.com.victorkk.restwithspringboot.integrationtests.vo;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Objects;

@XmlRootElement
public class AccountCredentialsTestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    public AccountCredentialsTestVO() {}

    public AccountCredentialsTestVO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountCredentialsTestVO that = (AccountCredentialsTestVO) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
