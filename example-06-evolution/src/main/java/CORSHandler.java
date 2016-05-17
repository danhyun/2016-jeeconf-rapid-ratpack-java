import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.MutableHeaders;
import ratpack.registry.Registry;

public class CORSHandler implements Handler {
  @Override
  public void handle(Context ctx) throws Exception {
    MutableHeaders headers = ctx.getResponse().getHeaders();
    headers.set("Access-Control-Allow-Origin", "*");
    headers.set("Access-Control-Allow-Headers", "x-requested-with, origin, content-type, accept");
    String baseUrl = "http://" + ctx.getRequest().getHeaders().get("Host"); // <1>
    ctx.next(Registry.single(String.class, baseUrl)); // <2>
  }
}
