package todobackend.ratpack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Todo {
  public final Long id;
  public final String title;
  public final boolean completed;
  public final Integer order;
  private final String baseUrl;

  @JsonCreator
  public Todo(@JsonProperty("id") Long id,
              @JsonProperty("title") String title,
              @JsonProperty("completed") boolean completed,
              @JsonProperty("order") Integer order) {
    this(id, title, completed, order, null);
  }

  public Todo(Long id, String title, boolean completed, Integer order, String baseUrl) {
    this.id = id;
    this.title = title;
    this.completed = completed;
    this.order = order;
    this.baseUrl = baseUrl;
  }

  public Todo baseUrl(String baseUrl) {
    return new Todo(id, title, completed, order, baseUrl);
  }

  public String getUrl() {
    return baseUrl + "/" + id;
  }

}
