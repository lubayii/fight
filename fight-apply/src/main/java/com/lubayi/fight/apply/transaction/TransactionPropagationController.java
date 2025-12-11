package com.lubayi.fight.apply.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lubayi
 * @date 2025/12/11
 */
@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionPropagationController {

    private final TransactionPropagationExample transactionPropagationExample;

    @GetMapping("/outNoAndException")
    public void testNotransaction_exception_required_required() {
        transactionPropagationExample.notransaction_exception_required_required();
    }

    @GetMapping("/outNoAndInsideException")
    public void testNotransaction_required_required_exception() {
        transactionPropagationExample.notransaction_required_required_exception();
    }

    @GetMapping("/outAndException")
    public void testTransaction_exception_required_required() {
        transactionPropagationExample.transaction_exception_required_required();
    }

    @GetMapping("/outAndInsideException")
    public void testTransaction_required_required_exception() {
        transactionPropagationExample.transaction_required_required_exception();
    }

    @GetMapping("/outAndInsideExceptionAndOutCatch")
    public void testTransaction_required_required_exception_try() {
        transactionPropagationExample.transaction_required_required_exception_try();
    }
}
