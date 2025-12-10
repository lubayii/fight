package com.lubayi.fight.apply.transaction;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

/**
 * Author: lubayi
 * Date: 2025/12/11
 * Time: 07:17
 */
@RequiredArgsConstructor
public class TransactionPropagationExampleImplTest {

    private final TransactionPropagationExample transactionPropagationExample;

    @Test
    public void testNotransaction_exception_required_required() {
        transactionPropagationExample.notransaction_exception_required_required();
    }

    @Test
    public void testNotransaction_required_required_exception() {
        transactionPropagationExample.notransaction_required_required_exception();
    }

}
