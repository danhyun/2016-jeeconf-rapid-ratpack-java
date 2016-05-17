package todobackend.ratpack;

import ratpack.handling.Context;
import ratpack.handling.InjectionHandler;
import ratpack.http.Response;
import ratpack.jackson.Jackson;

import java.util.stream.Collectors;

public class TodoBaseHandler extends InjectionHandler {
  public void handle(Context ctx, TodoRepository repo, String base) throws Exception {
    Response response = ctx.getResponse();

    ctx.byMethod(byMethodSpec -> byMethodSpec
      .options(() -> {
        response.getHeaders().set("Access-Control-Allow-Methods", "OPTIONS, GET, POST, DELETE");
        response.send();
      })
      .get(() ->
        repo.getAll()
          .map(todos -> todos.stream()
            .map(todo -> todo.baseUrl(base))
            .collect(Collectors.toList()))
          .map(Jackson::json)
          .then(ctx::render)
      )
      .post(() ->
        ctx.parse(Jackson.fromJson(Todo.class))
          .flatMap(repo::add)
          .map(todo -> todo.baseUrl(base))
          .map(Jackson::json)
          .then(ctx::render)
      )
      .delete(() -> repo.deleteAll().then(response::send))
    );
  }
}
