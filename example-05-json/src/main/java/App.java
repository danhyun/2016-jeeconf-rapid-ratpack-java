import ratpack.exec.Promise;
import ratpack.guice.Guice;
import ratpack.hikari.HikariModule;
import ratpack.jackson.Jackson;
import ratpack.server.RatpackServer;

public class App {
  public static void main(String[] args) throws Exception {
    RatpackServer.start(serverSpec -> serverSpec
      .registry(Guice.registry(bindings -> bindings
        .module(HikariModule.class, config -> {
          config.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
          config.addDataSourceProperty("URL", "jdbc:h2:mem:todo;INIT=RUNSCRIPT FROM 'classpath:/init.sql'");
        })
        .module(TodoModule.class)
      ))
      .handlers(chain -> chain
        // tag::parse[]
          .post(ctx -> {
            Promise<TodoModel> todo = ctx.parse(Jackson.fromJson(TodoModel.class)); // <1>
            todo.then(t -> ctx.render(t.title)); // <2>
          })
        // end::parse[]
      )
    );
  }
}
