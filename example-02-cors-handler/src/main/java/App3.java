import ratpack.server.RatpackServer;

public class App3 {
  public static void main(String[] args) throws Exception {
    RatpackServer.start(serverSpec -> serverSpec
      .handlers(chain -> chain
        .all(new CORSHandler()) // <1>
        .get(ctx -> ctx.render("JEEConf 2016"))
      )
    );
  }
}
