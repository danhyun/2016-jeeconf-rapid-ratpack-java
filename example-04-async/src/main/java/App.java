import jooq.tables.Todo;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;
import ratpack.exec.Blocking;
import ratpack.exec.Promise;
import ratpack.guice.Guice;
import ratpack.hikari.HikariModule;
import ratpack.jackson.Jackson;
import ratpack.server.RatpackServer;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class App {
  public static void main(String[] args) throws Exception {
    RatpackServer.start(serverSpec -> serverSpec
      .registry(Guice.registry(bindings -> bindings
        .module(HikariModule.class, config -> {
          config.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
          config.addDataSourceProperty("URL", "jdbc:h2:mem:todo;INIT=RUNSCRIPT FROM 'classpath:/init.sql'");
        })
        // tag::todo-module[]
        .module(TodoModule.class)
        // end::todo-module[]
      ))
      .handlers(chain -> chain
          // tag::jooq[]
          .get(ctx -> {
            DataSource ds = ctx.get(DataSource.class);
            DSLContext create = DSL.using(ds, SQLDialect.H2);
            SelectJoinStep<Record> from = create.select().from(Todo.TODO);
            Promise<List<Map<String, Object>>> promise = Blocking.get(() -> from.fetch().intoMaps()); // <1>
            promise.then(maps -> ctx.render(Jackson.json(maps))); // <2>
          })
        // end::jooq[]
      )
    );
  }
}
