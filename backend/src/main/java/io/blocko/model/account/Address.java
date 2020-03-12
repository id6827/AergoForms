package io.blocko.model.account;

import coinstack.paper.model.UuidEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ADDRESS")
@NoArgsConstructor
@ToString(callSuper = false)
public class Address extends UuidEntity {

  @Getter
  @Setter
  @ManyToOne
  @JoinColumn(name = "USERNAME", nullable = false)
  protected User username;

  @Getter
  @Setter
  @Column(unique = true)
  protected String address;

  public Address(final User user, final String address) {
    this.username = user;
    this.address = address;
  }

}
