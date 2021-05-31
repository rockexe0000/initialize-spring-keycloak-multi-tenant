package com.example.entity;


import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "city")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class City implements Serializable {
  private static final long serialVersionUID = -4551953276601557391L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long cid;

  private String name;

  public Long getCid() {
    return cid;
  }

  public void setId(Long cid) {
    this.cid = cid;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("City{");
    sb.append("cid=").append(cid);
    sb.append(", name='").append(name).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
