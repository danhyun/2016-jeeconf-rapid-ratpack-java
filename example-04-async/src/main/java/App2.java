import ratpack.exec.Promise;
import ratpack.guice.Guice;
import ratpack.hikari.HikariModule;
import ratpack.jackson.Jackson;
import ratpack.server.RatpackServer;

import java.util.List;

public class App2 {
  public static void main(String[] args) throws Exception {
    RatpackServer.start(serverSpec -> serverSpec
      .registry(Guice.registry(bindings -> bindings
        .module(HikariModule.class, config -> {
          config.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
          config.addDataSourceProperty("URL", "jdbc:h2:mem:todo;INIT=RUNSCRIPT FROM 'classpath:/init.sql'"); // <3>
        })
        .module(TodoModule.class)
      ))
      .handlers(chain -> chain
        // tag::handler[]
          .get(ctx -> {
            TodoRepository repository = ctx.get(TodoRepository.class); // <1>
            Promise<List<TodoModel>> todos = repository.getAll(); // <2>
            todos.then(t -> ctx.render(Jackson.json(t))); // <3>
          })
        // end::handler[]
      )
    );
  }
}
