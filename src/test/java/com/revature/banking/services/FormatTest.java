package com.revature.banking.services;

import com.revature.banking.services.Format;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FormatTest {
    @Test
    void f() {
        Assertions.assertEquals(Format.f(123.456789), "$123.46");
    }
}