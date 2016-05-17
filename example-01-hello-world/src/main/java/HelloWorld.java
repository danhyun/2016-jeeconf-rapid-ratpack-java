import ratpack.server.RatpackServer;

public class HelloWorld {
  public static void main(String[] args) throws Exception {
    RatpackServer.start(serverSpec -> serverSpec // <1>
      .handlers(chain -> chain // <2>
        .get(ctx -> ctx.render("Привіт Світ")) // <3>
      )
    );
  }
}
