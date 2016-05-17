import ratpack.http.MutableHeaders;
import ratpack.server.RatpackServer;

public class App {
  public static void main(String[] args) throws Exception {
    RatpackServer.start(serverSpec -> serverSpec
      .handlers(chain -> chain
        .get(ctx -> {
          MutableHeaders headers = ctx.getResponse().getHeaders(); // <1>
          headers.set("Access-Control-Allow-Origin", "*"); // <2>
          headers.set("Access-Control-Allow-Headers", "x-requested-with, origin, content-type, accept"); // <2>
          ctx.getResponse().send(); // <3>
        })
      )
    );
  }
}
