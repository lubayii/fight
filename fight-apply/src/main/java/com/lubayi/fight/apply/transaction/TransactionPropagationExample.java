package com.lubayi.fight.apply.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: lubayi
 * Date: 2025/12/11
 * Time: 07:13
 */
@Service
@RequiredArgsConstructor
public class TransactionPropagationExample {

    private final User1Service user1Service;

    private final User2Service user2Service;

    // ===========================================REQUIRED====================================================
    // 通过这两个方法得知：
    // 在外层方法未开启事务的情况下，REQUIRED 修饰的内部方法会新开启自己的事务，且开启的事务相互独立，互不干扰。

    /**
     * 结果：张三、李四 均插入
     * 外层方法没有开启事务
     * 子方法 user1Service.addRequired(user1) 传播机制是 REQUIRED，新建事务 T1，当方法执行完后，事务提交，T1 关闭，此时无活跃事务
     * 子方法 user2Service.addRequired(user2) 传播机制是 REQUIRED，因此时无活跃事务，新建事务 T2，当方法执行完后，事务提交，T2 提交后关闭
     * 虽然外层方法出现异常，但内部子方法的事务都已提交，所以张三、李四都插入
     */
    public void notransaction_exception_required_required() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addRequired(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addRequired(user2);

        throw new RuntimeException();
    }

    /**
     * 结果：张三插入、李四未插入
     * 外层方法没有开启事务
     * 所以插入"李四"方法抛出异常只会回滚插入"李四"方法，插入"张三"方法不受影响
     */
    public void notransaction_required_required_exception() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addRequired(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addRequiredException(user2);
    }

    // 在外层方法开启事务的情况下，REQUIRED修饰的内部方法会加入到外层方法的事务中，
    // 所有REQUIRED修饰的内部方法和外层方法均属于同一事务，只要一个方法回滚，整个事务均回滚。

    /**
     * 结果：张三、李四均未插入。
     * 因为外层方法开启事务了，所以REQUIRED修饰的内部方法不会再新开启事务，而是加入外层方法的事务
     * 而外层方法中抛出异常了，事务会回滚，所以张三、李四均未插入
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void transaction_exception_required_required() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addRequired(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addRequired(user2);

        throw new RuntimeException();
    }

    /**
     * 结果：张三、李四均未插入。
     * 因为外层方法开启事务了，所以REQUIRED修饰的内部方法不会再新开启事务，而是加入外层方法的事务
     * 而内部方法addRequiredException抛出异常了，事务会回滚，因为它们是同一个事务，所以张三、李四均未插入
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void transaction_required_required_exception() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addRequired(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addRequiredException(user2);
    }

    /**
     * 结果：张三、李四均未插入。
     * 1、外层方法 transaction_required_required_exception_try 开启事务 T1
     * 2、子方法 user1Service.addRequired(user1) 传播机制是 REQUIRED，复用 T1；执行 user1Mapper.insert(user1)，仅写入事务缓存，未提交到数据库
     * 3、子方法 user2Service.addRequiredException(user2) 传播机制是 REQUIRED，复用 T1；执行 user2Mapper.insert(user2)，同样写入事务缓存，未提交
     * 4、addRequiredException 抛出 RuntimeException 异常，该异常是「触发回滚的异常」，Spring 会立即标记 T1 为「必须回滚（rollback-only）」
     * 5、外层代码捕获了异常，但事务的回滚标记已经生效，事务层面的标记不受业务代码 catch 影响
     * 6、外层方法执行完毕，准备提交 T1，Spring 检查 T1 状态：发现已被标记为「rollback-only」，拒绝提交，转而执行「回滚 T1」
     * 7、事务 T1 回滚，事务缓存中的两个插入操作（user1、user2）全部撤销，数据库无新数据写入
     */
    @Transactional
    public void transaction_required_required_exception_try() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addRequired(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        try {
            user2Service.addRequiredException(user2);
        } catch (Exception e) {
            System.out.println("方法回滚");
        }
    }

    // ===============================================REQUIRES_NEW================================================

    /**
     * 结果：张三、李四均插入
     * 外层方法未开启事务，
     * 子方法 user1Service.addRequiresNew(user1) 传播机制是 REQUIRES_NEW，新建事务 T1
     * 子方法 user2Service.addRequiresNew(user2) 传播机制是 REQUIRES_NEW，新建事务 T2
     * 虽然外层方法抛异常了，但不影响子方法的事务，在子方法执行完毕时，事务 T1、T2 就提交了
     */
    public void notransaction_exception_requiresNew_requiresNew() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addRequiresNew(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addRequiresNew(user2);

        throw new RuntimeException();
    }

    /**
     * 结果：张三插入、李四未插入
     * 外层方法未开启事务，
     * 子方法 user1Service.addRequiresNew(user1) 传播机制是 REQUIRES_NEW，新建事务 T1
     * 子方法 user2Service.addRequiresNewException(user2) 传播机制是 REQUIRES_NEW，新建事务 T2
     * 子方法 addRequiresNewException 抛异常，所以 T2 回滚，但不影响事务 T1，事务 T1 提交
     */
    public void notransaction_requiresNew_requiresNew_exception() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addRequiresNew(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addRequiresNewException(user2);
    }

    /**
     * 结果：张三未插入、李四插入、王五插入
     * 外层方法开启事务，创建事务 T1
     * 子方法 user1Service.addRequired(user1) 传播机制是 REQUIRED，复用 T1
     * 子方法 user2Service.addRequiresNew(user2) 传播机制是 REQUIRES_NEW，新建事务 T2
     * 子方法 user2Service.addRequiresNew(user3) 传播机制是 REQUIRES_NEW，新建事务 T3
     * 事务 T2、T3 正常提交，事务 T1 因外层方法抛异常，所以回滚，张三未插入到数据库
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void transaction_exception_required_requiresNew_requiresNew() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addRequired(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addRequiresNew(user2);

        User2 user3 = new User2();
        user3.setName("王五");
        user2Service.addRequiresNew(user3);

        throw new RuntimeException();
    }

    /**
     * 结果：张三未插入、李四插入、王五未插入
     * 外层方法开启事务，创建事务 T1
     * 子方法 user1Service.addRequired(user1) 传播机制是 REQUIRED，复用 T1
     * 子方法 user2Service.addRequiresNew(user2) 传播机制是 REQUIRES_NEW，新建事务 T2
     * 子方法 user2Service.addRequiresNew(user3) 传播机制是 REQUIRES_NEW，新建事务 T3
     * 事务 T2 正常提交；事务 T3 因方法抛异常，所以回滚；事务 T1 因内部方法抛异常，所以也回滚
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void transaction_required_requiresNew_requiresNew_exception() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addRequired(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addRequiresNew(user2);

        User2 user3 = new User2();
        user3.setName("王五");
        user2Service.addRequiresNewException(user3);
    }

    /**
     * 结果：张三插入、李四插入、王五未插入
     * 外层方法开启事务，创建事务 T1
     * 子方法 user1Service.addRequired(user1) 传播机制是 REQUIRED，复用 T1
     * 子方法 user2Service.addRequiresNew(user2) 传播机制是 REQUIRES_NEW，新建事务 T2
     * 子方法 user2Service.addRequiresNew(user3) 传播机制是 REQUIRES_NEW，新建事务 T3
     * 事务 T2 正常提交；事务 T3 因方法抛异常，所以回滚；事务 T1 因捕获了内部方法的异常，所以事务正常提交
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void transaction_required_requiresNew_requiresNew_exception_try() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addRequired(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addRequiresNew(user2);

        User2 user3 = new User2();
        user3.setName("王五");
        try {
            user2Service.addRequiresNewException(user3);
        } catch (Exception e) {
            System.out.println("回滚");
        }
    }

    // ===============================================NESTED================================================

    /**
     * 结果：张三、李四均插入
     * 外层方法没有开启事务，此时 NESTED 等同于 REQUIRED，所以虽然外层方法出现异常，但内部子方法的事务都已提交，所以张三、李四都插入
     */
    public void notransaction_exception_nested_nested() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addNested(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addNested(user2);

        throw new RuntimeException();
    }

    /**
     * 结果：张三插入、李四未插入
     * 外层方法没有开启事务，此时 NESTED 等同于 REQUIRED
     * 所以张三事务提交，成功插入；李四因异常回滚，未插入
     */
    public void notransaction_nested_nested_exception() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addNested(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addNestedException(user2);
    }

    /**
     * 结果：张三、李四 均未插入
     * 外层方法开启事务 T1
     * 子方法 user1Service.addNested 传播机制是 NESTED，创建嵌套事务 T2
     * 子方法 user2Service.addNested 传播机制是 NESTED，创建嵌套事务 T3
     * 父事务 T1，因抛异常回滚，所以嵌套事务 T2、T3 也都回滚，数据未插入
     */
    @Transactional
    public void transaction_exception_nested_nested() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addNested(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addNested(user2);

        throw new RuntimeException();
    }

    /**
     * 结果：张三、李四均未插入
     * 外层方法开启事务 T1
     * 子方法 user1Service.addNested 传播机制是 NESTED，创建嵌套事务 T2
     * 子方法 user2Service.addNestedException 传播机制是 NESTED，创建嵌套事务 T3
     * 嵌套事务 T3 因异常回滚，所以李四未插入；
     * 虽然嵌套事务回滚不影响父事务，但因子方法异常导致外层方法也异常，所以父事务回滚，父事务回滚导致嵌套事务 T2 也回滚，所以张三未插入
     */
    @Transactional
    public void transaction_nested_nested_exception() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addNested(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addNestedException(user2);
    }

    /**
     * 结果：张三插入、李四未插入
     * 外层方法开启事务 T1
     * 子方法 user1Service.addNested 传播机制是 NESTED，创建嵌套事务 T2
     * 子方法 user2Service.addNestedException 传播机制是 NESTED，创建嵌套事务 T3
     * 嵌套事务 T3 因异常回滚，所以李四未插入；
     * 但嵌套事务回滚不影响父事务，且在父事务中异常被捕获，所以父事务正常提交，T2也正常提交，所以张三插入
     */
    @Transactional
    public void transaction_nested_nested_exception_try() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addNested(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        try {
            user2Service.addNestedException(user2);
        } catch (Exception e) {
            System.out.println("方法回滚");
        }
    }

    // ===============================================SUPPORTS================================================

    /**
     * 结果：张三、李四均插入
     * 外层方法没有开启事务
     * 子方法的传播机制是 SUPPORTS，以非事务的方式执行，SQL执行完就会提交到数据库
     * 所以外层方法抛异常，也影响不到子方法
     */
    public void notransaction_exception_supports_supports() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addSupports(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addSupports(user2);

        throw new RuntimeException();
    }

    /**
     * 结果：张三、李四均插入
     * 外层方法没有开启事务
     * 子方法的传播机制是 SUPPORTS，以非事务的方式执行，SQL执行完就会提交到数据库
     * 所以即使子方法addSupportsException抛异常，但是在SQL执行后抛的异常，SQL执行完就提交到了数据库，所以李四也成功插入
     */
    public void notransaction_supports_supports_exception() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addSupports(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addSupportsException(user2);
    }

    /**
     * 结果：张三、李四均未插入
     * 外层方法开启事务 T1
     * 子方法的传播机制都是 SUPPORTS，所以都加入事务 T1
     * 外层方法抛出异常，事务 T1 回滚，所以张三、李四均未插入
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void transaction_exception_supports_supports() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addSupports(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addSupports(user2);

        throw new RuntimeException();
    }

    /**
     * 结果：张三、李四均未插入
     * 外层方法开启事务 T1
     * 子方法的传播机制都是 SUPPORTS，所以都加入事务 T1
     * 子方法抛出异常，所以事务 T1 回滚，所以张三、李四均未插入
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void transaction_supports_supports_exception() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addSupports(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addSupportsException(user2);
    }

    // ===============================================MANDATORY================================================

    /**
     * 结果：张三未插入
     * 外层方法没有开启事务，子方法的传播机制是 MANDATORY
     * 因为外层方法没有开启事务，所以直接抛出异常
     */
    public void notransaction_mandatory() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addMandatory(user1);
    }

    /**
     * 结果：张三、李四均未插入
     * 外层方法开启事务 T1，子方法的传播机制是 MANDATORY，所以子方法也使用事务 T1
     * 在外层方法中抛异常了，所以事务 T1 回滚，张三、李四均未成功插入
     */
    @Transactional
    public void transaction_exception_mandatory_mandatory() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addMandatory(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addMandatory(user2);

        throw new RuntimeException();
    }

    /**
     * 结果：张三、李四均未插入
     * 外层方法开启事务 T1，子方法的传播机制是 MANDATORY，所以子方法也使用事务 T1
     * 子方法抛异常了，所以事务 T1 回滚，张三、李四均未成功插入
     */
    @Transactional
    public void transaction_mandatory_mandatory_exception() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addMandatory(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addMandatoryException(user2);
    }

    // ===============================================NOT_SUPPORTED================================================

    /**
     * 结果：张三、李四均插入
     * 外层方法没有开启事务
     * 子方法 user1Service.addRequired(user1) 的传播机制是 REQUIRED，新建事务 T1，方法执行完事务提交，并关闭事务
     * 子方法 user2Service.addNotSupported(user2) 的传播机制是 NOT_SUPPORTED，以非事务方式执行，SQL执行完就提交到了数据库
     */
    public void notransaction_exception_required_notSupported() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addRequired(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addNotSupported(user2);

        throw new RuntimeException();
    }

    /**
     * 结果：张三、李四均插入
     * 外层方法没有开启事务
     * 子方法 user1Service.addRequired(user1) 的传播机制是 REQUIRED，新建事务 T1，方法执行完事务提交，并关闭事务
     * 子方法 user2Service.addNotSupported(user2) 的传播机制是 NOT_SUPPORTED，以非事务方式执行，SQL执行完就提交到了数据库，所以方法中抛异常也没有影响
     */
    public void notransaction_required_notSupported_exception() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addRequired(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addNotSupportedException(user2);
    }

    /**
     * 结果：张三未插入、李四插入
     * 外层方法开启事务 T1
     * 子方法 user1Service.addRequired(user1) 的传播机制是 REQUIRED，所以加入事务 T1，而外层方法有异常，事务 T1 回滚，所以张三未插入
     * 子方法 user2Service.addNotSupported(user2) 的传播机制是 NOT_SUPPORTED，以非事务方式执行，并且暂停事务T1
     * SQL执行完就提交到了数据库，所以外层方法抛异常事务 T1 回滚，也不影响李四成功插入
     */
    @Transactional
    public void transaction_exception_required_notSupported() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addRequired(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addNotSupported(user2);

        throw new RuntimeException();
    }

    /**
     * 结果：张三未插入、李四插入
     * 外层方法开启事务 T1
     * 子方法 user1Service.addRequired(user1) 的传播机制是 REQUIRED，所以加入事务 T1，
     * 而子方法 addNotSupportedException 抛异常，外层方法感知到异常，事务 T1 回滚，所以张三未插入
     * 子方法 user2Service.addNotSupported(user2) 的传播机制是 NOT_SUPPORTED，以非事务方式执行，并且暂停事务T1
     * SQL执行完就提交到了数据库，所以在SQL执行完再抛异常，也不影响数据成功插入
     */
    @Transactional
    public void transaction_required_notSupported_exception() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addRequired(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addNotSupportedException(user2);
    }


    // ===============================================NEVER================================================

    /**
     * 结果：张三、李四均插入
     * 外层方法没有开启事务，子方法的传播机制都是 NEVER，以非事务方式执行，SQL执行完就提交到数据库，所以数据成功插入
     */
    public void notransaction_exception_never_never() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addNever(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addNever(user2);

        throw new RuntimeException();
    }

    /**
     * 结果：张三、李四均插入
     * 外层方法没有开启事务，子方法的传播机制都是 NEVER，以非事务方式执行，SQL执行完就提交到数据库
     * addNeverException 在SQL执行完抛异常，也不影响数据成功插入
     */
    public void notransaction_never_never_exception() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addNever(user1);

        User2 user2 = new User2();
        user2.setName("李四");
        user2Service.addNeverException(user2);
    }

    /**
     * 结果：张三未插入
     * 外层方法开启事务 T1，子方法的传播机制是 NEVER，所以直接抛出异常
     */
    @Transactional
    public void transaction_never() {
        User1 user1 = new User1();
        user1.setName("张三");
        user1Service.addNever(user1);
    }

}
