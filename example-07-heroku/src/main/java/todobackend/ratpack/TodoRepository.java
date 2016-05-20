package todobackend.ratpack;

import jooq.tables.records.TodoRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import ratpack.exec.Blocking;
import ratpack.exec.Operation;
import ratpack.exec.Promise;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import static jooq.tables.Todo.TODO;

@Singleton
public class TodoRepository {

  final DSLContext context;

  @Inject
  public TodoRepository(DataSource ds) {
    this.context = DSL.using(new DefaultConfiguration().derive(ds));
  }

  public Promise<List<Todo>> getAll() {
    SelectJoinStep all = context.select().from(TODO);
    return Blocking.get(() -> all.fetchInto(Todo.class));
  }

  public Promise<Todo> getById(Long id) {
    SelectConditionStep where = context.select().from(TODO).where(TODO.ID.equal(id));
    return Blocking.get(() -> where.fetchOne().into(Todo.class));
  }

  public Promise<Todo> add(Todo todo) {
    TodoRecord todoRecord = context.newRecord(TODO, todo);
    return Blocking.op(todoRecord::store)
      .next(Blocking.op(todoRecord::refresh))
      .map(() -> todoRecord.into(Todo.class));
  }

  public Promise<Todo> update(Map<String, Object> todo) {
    TodoRecord record = context.newRecord(TODO, todo);

    return Blocking.get(() -> context.executeUpdate(record))
      .blockingOp(count -> record.refresh())
      .map(i -> record.into(Todo.class));
  }

  public Operation delete(Long id) {
    DeleteConditionStep<TodoRecord> deleteWhereId = context.deleteFrom(TODO).where(TODO.ID.equal(id));
    return Blocking.op(deleteWhereId::execute);
  }

  public Operation deleteAll() {
    DeleteWhereStep<TodoRecord> delete = context.deleteFrom(TODO);
    return Blocking.op(delete::execute);
  }
}
