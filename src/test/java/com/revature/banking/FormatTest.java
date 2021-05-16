package com.revature.banking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FormatTest {
    @Test
    void f() {
        Assertions.assertEquals(Format.f(123.456789), "$123.46");
    }
}