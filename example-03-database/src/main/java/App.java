import ratpack.guice.Guice;
import ratpack.hikari.HikariModule;
import ratpack.server.RatpackServer;

public class App {
  public static void main(String[] args) throws Exception {
    RatpackServer.start(serverSpec -> serverSpec
      .registry(Guice.registry(bindings -> bindings // <1>
        .module(HikariModule.class, config -> { // <2>
          config.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource"); // <3>
          config.addDataSourceProperty("URL", "jdbc:h2:mem:todo;INIT=RUNSCRIPT FROM 'classpath:/init.sql'"); // <3>
        })
      ))
      .handlers(chain -> chain
        .get(ctx -> ctx.render("JEEConf 2016"))
      )
    );
  }
}
