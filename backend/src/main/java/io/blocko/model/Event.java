package io.blocko.model;

import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
public class Event {

  @Getter
  @Setter
  @JsonProperty("UDEvent")
  protected String event;

  @Getter
  @Setter
  protected String data;

  @JsonIgnore
  protected Map<String, String> attributes;

  /**
   * get attribute.
   * 
   * @param name attribute name
   * 
   * @return attribute value
   * @throws IOException on failure of parsing
   */
  @SuppressWarnings("unchecked")
  @JsonIgnore
  public String getAttribute(final String name) throws IOException {
    if (null == attributes) {
      attributes = new ObjectMapper().readValue(data, Map.class);
    }
    return ofNullable(attributes).map(m -> m.get(name)).orElse(null);
  }

}
