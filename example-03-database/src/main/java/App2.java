import jooq.tables.Todo;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import ratpack.guice.Guice;
import ratpack.hikari.HikariModule;
import ratpack.jackson.Jackson;
import ratpack.server.RatpackServer;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class App2 {
  public static void main(String[] args) throws Exception {
    RatpackServer.start(serverSpec -> serverSpec
      .registry(Guice.registry(bindings -> bindings
        .module(HikariModule.class, config -> {
          config.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
          config.addDataSourceProperty("URL", "jdbc:h2:mem:todo;INIT=RUNSCRIPT FROM 'classpath:/init.sql'");
        })
      ))
      .handlers(chain -> chain
          // tag::jooq[]
          .get(ctx -> {
            DataSource ds = ctx.get(DataSource.class); // <1>
            DSLContext create = DSL.using(ds, SQLDialect.H2); // <2>
            List<Map<String, Object>> maps = create.select().from(Todo.TODO).fetch().intoMaps(); // <3>
            ctx.render(Jackson.json(maps)); // <4>
          })
        // end::jooq[]
      )
    );
  }
}
