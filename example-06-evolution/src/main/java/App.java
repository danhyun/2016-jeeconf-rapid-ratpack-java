import ratpack.guice.Guice;
import ratpack.hikari.HikariModule;
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
        .bindInstance(new CORSHandler())
        .bindInstance(new TodoBaseHandler()) // <1>
        .bindInstance(new TodoChain()) // <2>
      ))
      .handlers(chain -> chain
          .all(CORSHandler.class)
          .insert(TodoChain.class) // <3>
      )
    );
  }
}
