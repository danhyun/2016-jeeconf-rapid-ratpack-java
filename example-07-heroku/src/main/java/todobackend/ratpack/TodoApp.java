package todobackend.ratpack;

import com.zaxxer.hikari.HikariConfig;
import ratpack.guice.Guice;
import ratpack.hikari.HikariModule;
import ratpack.server.BaseDir;
import ratpack.server.RatpackServer;

public class TodoApp {
  public static void main(String[] args) throws Exception {
    RatpackServer.start(ratpackServerSpec -> ratpackServerSpec
      .serverConfig(serverConfigBuilder -> serverConfigBuilder
        .baseDir(BaseDir.find())
        .yaml("db.yaml")
        .env()
        .sysProps()
        .require("/db", HikariConfig.class)
      )
      .registry(Guice.registry(bindingsSpec -> bindingsSpec
        .module(HikariModule.class)
        .bind(TodoRepository.class)
      ))
      .handlers(chain -> chain
        .prefix("todo", todoChain -> todoChain
          .all(new CORSHandler())
          .path(new TodoBaseHandler())
          .path(":id", new TodoIdHandler())
        )
      ));
  }
}
