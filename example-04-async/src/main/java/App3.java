import ratpack.guice.Guice;
import ratpack.hikari.HikariModule;
import ratpack.jackson.Jackson;
import ratpack.server.RatpackServer;

public class App3 {
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
        // tag::handler[]
          .get(ctx -> {
            TodoRepository repository = ctx.get(TodoRepository.class);
            repository.getAll()
              .map(Jackson::json)
              .then(ctx::render);
          })
        // end::handler[]
      )
    );
  }
}
