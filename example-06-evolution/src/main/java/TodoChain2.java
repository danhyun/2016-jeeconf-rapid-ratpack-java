import ratpack.func.Action;
import ratpack.handling.Chain;

public class TodoChain2 implements Action<Chain> {
  @Override
  public void execute(Chain chain) throws Exception {
    chain
      .path(TodoBaseHandler2.class)
      .path(":id", TodoHandler.class); // <1>
  }
}
