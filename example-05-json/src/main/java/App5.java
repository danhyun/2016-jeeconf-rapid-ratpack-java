import ratpack.exec.Promise;
import ratpack.guice.Guice;
import ratpack.hikari.HikariModule;
import ratpack.http.Response;
import ratpack.jackson.Jackson;
import ratpack.server.RatpackServer;

public class App5 {
  public static void main(String[] args) throws Exception {
    RatpackServer.start(serverSpec -> serverSpec
      .registry(Guice.registry(bindings -> bindings
        .module(HikariModule.class, config -> {
          config.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
          config.addDataSourceProperty("URL", "jdbc:h2:mem:todo;INIT=RUNSCRIPT FROM 'classpath:/init.sql'");
        })
        .module(TodoModule.class)
        .bindInstance(new CORSHandler()) // <1>
      ))
      .handlers(chain -> chain
          .all(CORSHandler.class) // <2>
          .path(ctx -> { // <3>
            TodoRepository repository = ctx.get(TodoRepository.class);
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
          })
      )
    );
  }
}
