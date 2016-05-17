import jooq.tables.records.TodoRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import ratpack.exec.Blocking;
import ratpack.exec.Operation;
import ratpack.exec.Promise;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import static jooq.tables.Todo.TODO;

public class TodoRepository {
  // tag::getAll[]
  private final DSLContext create;

  public TodoRepository(DataSource ds) {
    this.create = DSL.using(ds, SQLDialect.H2);
  }

  public Promise<List<TodoModel>> getAll() {
    SelectJoinStep all = create.select().from(TODO);
    return Blocking.get(() -> all.fetchInto(TodoModel.class));
  }
  // end::getAll[]

  public Promise<TodoModel> getById(Long id) {
    SelectConditionStep where = create.select().from(TODO).where(TODO.ID.equal(id));
    return Blocking.get(() -> where.fetchOne().into(TodoModel.class));
  }

  public Promise<TodoModel> add(TodoModel TodoModel) {
    TodoRecord todoRecord = create.newRecord(TODO, TodoModel);
    return Operation.of(todoRecord::store)
      .next(todoRecord::refresh)
      .map(() -> todoRecord.into(TodoModel.class));
  }

  public Promise<TodoModel> update(Map<String, Object> TodoModel) {
    TodoRecord record = create.newRecord(TODO, TodoModel);

    return Blocking
      .get(() -> create.executeUpdate(record))
      .blockingOp(count -> record.refresh())
      .map(i -> record.into(TodoModel.class));
  }

  public Operation delete(Long id) {
    DeleteConditionStep<TodoRecord> deleteWhereId = create.deleteFrom(TODO).where(TODO.ID.equal(id));
    return Blocking.op(deleteWhereId::execute);
  }

  public Operation deleteAll() {
    DeleteWhereStep<TodoRecord> delete = create.deleteFrom(TODO);
    return Blocking.op(delete::execute);
  }
}
