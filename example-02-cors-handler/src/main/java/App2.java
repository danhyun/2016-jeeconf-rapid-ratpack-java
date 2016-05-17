import ratpack.http.MutableHeaders;
import ratpack.server.RatpackServer;

public class App2 {
  public static void main(String[] args) throws Exception {
    RatpackServer.start(serverSpec -> serverSpec
      .handlers(chain -> chain
        .all(ctx -> { // <1>
          MutableHeaders headers = ctx.getResponse().getHeaders();
          headers.set("Access-Control-Allow-Origin", "*");
          headers.set("Access-Control-Allow-Headers", "x-requested-with, origin, content-type, accept");
          ctx.next(); // <2>
        })
        .get(ctx -> ctx.render("JEEConf 2016"))
        .get("foo", ctx -> ctx.render("foo"))
      )
    );
  }
}
