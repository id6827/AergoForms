package io.blocko.model.account;

import static java.util.Optional.ofNullable;

import coinstack.paper.model.UuidEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "USER")
@NoArgsConstructor
@ToString(exclude = {"password"}, callSuper = true)
public class User extends UuidEntity implements Serializable {

  @Getter
  @Setter
  protected String username;

  @JsonIgnore
  @Getter
  @Setter
  protected String password;

  @Getter
  @Setter
  @Column(length = 4)
  protected Short birtyear;

  protected Byte status;

  protected Byte authority;

  protected Byte gender;

  public User(final String username, final String password) {
    this.username = username;
    this.password = password;
  }

  public AccountStatus getStatus() {
    return ofNullable(status).map(AccountStatus::fromCode).orElse(null);
  }

  public void setStatus(final AccountStatus status) {
    this.status = status.code();
  }

  public AuthorityCode getAuthority() {
    return ofNullable(authority).map(AuthorityCode::fromCode).orElse(null);
  }

  public void setAuthority(final AuthorityCode authority) {
    this.authority = authority.code();
  }

  public Gender getGender() {
    return ofNullable(gender).map(Gender::fromCode).orElse(null);
  }

  public void setGender(final Gender gender) {
    this.gender = gender.code();
  }
}

