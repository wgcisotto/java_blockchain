package com.wgcisotto.blockchain.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Sha256HelperTest {

    @Test
    public void testHash(){
        Assertions.assertEquals("190eb3ebae2b41124493ac98ac49717fbd290156ca2e0de5f1d5c25a11e89120",
                SHA256Helper.hash("William"), "SHA256 of William not correct. ");
    }

}
