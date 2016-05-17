import ratpack.func.Action;
import ratpack.handling.Chain;

public class TodoChain implements Action<Chain> {
  @Override
  public void execute(Chain chain) throws Exception {
    chain.path(TodoBaseHandler.class);
  }
}
