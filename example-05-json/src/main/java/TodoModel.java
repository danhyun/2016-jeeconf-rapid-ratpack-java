import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TodoModel {
  public final Long id;
  public final String title;
  public final boolean completed;
  public final Integer order;
  private final String baseUrl;

  @JsonCreator
  public TodoModel(@JsonProperty("id") Long id,
              @JsonProperty("title") String title,
              @JsonProperty("completed") boolean completed,
              @JsonProperty("order") Integer order) {
    this(id, title, completed, order, null);
  }

  public TodoModel(Long id, String title, boolean completed, Integer order, String baseUrl) {
    this.id = id;
    this.title = title;
    this.completed = completed;
    this.order = order;
    this.baseUrl = baseUrl;
  }

  public TodoModel baseUrl(String baseUrl) {
    return new TodoModel(id, title, completed, order, baseUrl);
  }

  public String getUrl() {
    return baseUrl + "/" + id;
  }

}

