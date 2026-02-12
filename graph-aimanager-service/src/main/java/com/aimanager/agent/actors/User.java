package com.aimanager.agent.actors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "aimanager", name = "users")
public class User {
  @Id
  private final String id;
  private final String name;
  private final String email;
  private final String phone;
 

  public User(String name, String email, String phone, String id) {
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  public String getId() {
    return id;
  }

}
