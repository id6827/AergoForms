package io.blocko.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Table
@Entity
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Survey {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String question;
  @OneToMany(cascade = CascadeType.REMOVE)
  @JoinColumn(name="ANSWER_ID")
  private List<Answer> answers = new ArrayList<>();
  private LocalDateTime startTime;
  private LocalDateTime endTime;

  public Survey(String question, LocalDateTime startTime, LocalDateTime endTime,
      Answer... answers) {
    this.question = question;
    this.answers.addAll(Arrays.asList(answers));
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public Answer max() {
    final Optional<Answer> max =
        answers.stream().reduce((a, b) -> a.getCount().compareTo(b.getCount()) > 0 ? a : b);
    log.info("MAX() - {}", max.get());
    return max.get();
  }

  public Answer min() {
    final Optional<Answer> min =
        answers.stream().reduce((a, b) -> a.getCount().compareTo(b.getCount()) < 0 ? a : b);
    log.info("MIN() - {}", min.get());
    return min.get();
  }

  public Answer aggregate() {
    final BigInteger total =
        answers.stream().map(Answer::getCount).reduce(BigInteger.ZERO, (a, b) -> a.add(b));
    final Answer aggregate = new Answer("TOTAL", total);
    log.info("AGGREGATE() - {}", aggregate);
    return aggregate;
  }
}
