package io.blocko.model;

import coinstack.paper.model.UuidEntity;
import io.blocko.model.account.User;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Table(name = "SURVEY")
@Entity
@ToString(callSuper = true)
@NoArgsConstructor
public class Survey extends UuidEntity {

  @Getter
  @Setter
  private String question;

  private byte type;

  @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  @JoinColumn(name = "ANSWER_UUID", referencedColumnName = "UUID")
  @Getter
  @Setter
  private List<Answer> answers = new ArrayList<>();

  @OneToOne
  @JoinColumn(name = "OWNER_UUID", referencedColumnName = "UUID")
  @Getter
  @Setter
  private User owner;

  @Getter
  @Setter
  private LocalDateTime startTime;

  @Getter
  @Setter
  private LocalDateTime endTime;

  public SurveyType getType() {
    return SurveyType.fromCode(type);
  }

  public void setType(final SurveyType surveyType) {
    this.type = surveyType.code();
  }

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
