package io.blocko.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

//DTO
@ToString
@NoArgsConstructor
public class Form {

  @Getter
  @Setter
  private String question;

  @Getter
  @Setter
  private byte type;

  @Getter
  @Setter
  private List<Item> items = new ArrayList<>();

  @Getter
  @Setter
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime startTime;

  @Getter
  @Setter
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private LocalDateTime endTime;

}
