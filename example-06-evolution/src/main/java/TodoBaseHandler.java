import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.InjectionHandler;
import ratpack.http.Response;
import ratpack.jackson.Jackson;

public class TodoBaseHandler extends InjectionHandler { // <1>
  public void handle (Context ctx, TodoRepository repository) throws Exception { // <2>
    Response response = ctx.getResponse();
    ctx.byMethod(method -> method
      .options(() -> {
        response.getHeaders().set("Access-Control-Allow-Methods", "OPTIONS, GET, POST, DELETE");
        response.send();
      })
      .get(() ->
        repository.getAll()
          .map(Jackson::json)
          .then(ctx::render)
      )
      .post(() -> {
        Promise<TodoModel> todo = ctx.parse(Jackson.fromJson(TodoModel.class));
        todo
          .flatMap(repository::add)
          .map(Jackson::json)
          .then(ctx::render);
      })
      .delete(() -> repository.deleteAll().then(response::send))
    );
  }
}
