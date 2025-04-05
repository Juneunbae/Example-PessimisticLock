# Example-PessimisticLock
비관적 락 테스트

## 프로젝트 구성
```
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com.example.locktest
│   │   │   │   ├── item
│   │   │   │   │   ├── Item.java
│   │   │   │   │   ├── ItemRepository.java
│   │   │   │   │   ├── ItemService.java
│   ├── test
│   │   ├── java
│   │   │   ├── com.example.locktest
│   │   │   │   ├── item
│   │   │   │   │   ├── ItemServiceTest.java
```

## PESSIMISTIC_WRITE
```java
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Item i WHERE i.id = :id")
    Item findByIdWithLock(Long id);
}
```


## 실행 결과
```shell
2025-04-05T16:16:43.013+09:00  INFO 15059 --- [lock-test] [    Test worker] c.example.locktest.item.ItemServiceTest  : 초기 아이템 데이터 설정
2025-04-05T16:16:43.031+09:00 DEBUG 15059 --- [lock-test] [    Test worker] org.hibernate.SQL                        : 
    insert 
    into
        item
        (name, quantity, id) 
    values
        (?, ?, default)
2025-04-05T16:16:43.035+09:00 TRACE 15059 --- [lock-test] [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter (1:VARCHAR) <- [Item 1]
2025-04-05T16:16:43.035+09:00 TRACE 15059 --- [lock-test] [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter (2:INTEGER) <- [10]
2025-04-05T16:16:43.051+09:00  INFO 15059 --- [lock-test] [       Thread-5] c.example.locktest.item.ItemServiceTest  : 스레드 2: 아이템 수량 업데이트 시도
2025-04-05T16:16:43.051+09:00  INFO 15059 --- [lock-test] [       Thread-4] c.example.locktest.item.ItemServiceTest  : 스레드 1: 아이템 수량 업데이트 시도
2025-04-05T16:16:43.081+09:00 DEBUG 15059 --- [lock-test] [       Thread-5] org.hibernate.SQL                        : 
    select
        i1_0.id,
        i1_0.name,
        i1_0.quantity 
    from
        item i1_0 
    where
        i1_0.id=? for update
2025-04-05T16:16:43.081+09:00 DEBUG 15059 --- [lock-test] [       Thread-4] org.hibernate.SQL                        : 
    select
        i1_0.id,
        i1_0.name,
        i1_0.quantity 
    from
        item i1_0 
    where
        i1_0.id=? for update
2025-04-05T16:16:43.082+09:00 TRACE 15059 --- [lock-test] [       Thread-5] org.hibernate.orm.jdbc.bind              : binding parameter (1:BIGINT) <- [1]
2025-04-05T16:16:43.082+09:00 TRACE 15059 --- [lock-test] [       Thread-4] org.hibernate.orm.jdbc.bind              : binding parameter (1:BIGINT) <- [1]
2025-04-05T16:16:43.091+09:00 DEBUG 15059 --- [lock-test] [       Thread-5] org.hibernate.SQL                        : 
    update
        item 
    set
        name=?,
        quantity=? 
    where
        id=?
2025-04-05T16:16:43.091+09:00 TRACE 15059 --- [lock-test] [       Thread-5] org.hibernate.orm.jdbc.bind              : binding parameter (1:VARCHAR) <- [Item 1]
2025-04-05T16:16:43.091+09:00 TRACE 15059 --- [lock-test] [       Thread-5] org.hibernate.orm.jdbc.bind              : binding parameter (2:INTEGER) <- [30]
2025-04-05T16:16:43.091+09:00 TRACE 15059 --- [lock-test] [       Thread-5] org.hibernate.orm.jdbc.bind              : binding parameter (3:BIGINT) <- [1]
2025-04-05T16:16:43.093+09:00  INFO 15059 --- [lock-test] [       Thread-5] c.example.locktest.item.ItemServiceTest  : 스레드 2: 아이템 수량 업데이트 완료
2025-04-05T16:16:43.093+09:00 DEBUG 15059 --- [lock-test] [       Thread-4] org.hibernate.SQL                        : 
    update
        item 
    set
        name=?,
        quantity=? 
    where
        id=?
2025-04-05T16:16:43.094+09:00 TRACE 15059 --- [lock-test] [       Thread-4] org.hibernate.orm.jdbc.bind              : binding parameter (1:VARCHAR) <- [Item 1]
2025-04-05T16:16:43.094+09:00 TRACE 15059 --- [lock-test] [       Thread-4] org.hibernate.orm.jdbc.bind              : binding parameter (2:INTEGER) <- [20]
2025-04-05T16:16:43.094+09:00 TRACE 15059 --- [lock-test] [       Thread-4] org.hibernate.orm.jdbc.bind              : binding parameter (3:BIGINT) <- [1]
2025-04-05T16:16:43.094+09:00  INFO 15059 --- [lock-test] [       Thread-4] c.example.locktest.item.ItemServiceTest  : 스레드 1: 아이템 수량 업데이트 완료
2025-04-05T16:16:43.098+09:00 DEBUG 15059 --- [lock-test] [    Test worker] org.hibernate.SQL                        : 
    select
        i1_0.id,
        i1_0.name,
        i1_0.quantity 
    from
        item i1_0 
    where
        i1_0.id=?
2025-04-05T16:16:43.098+09:00 TRACE 15059 --- [lock-test] [    Test worker] org.hibernate.orm.jdbc.bind              : binding parameter (1:BIGINT) <- [1]
2025-04-05T16:16:43.099+09:00  INFO 15059 --- [lock-test] [    Test worker] c.example.locktest.item.ItemServiceTest  : 최종 아이템 수량: 20
```
