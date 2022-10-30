package com.backpapp.details.test

import io.kotest.core.Tuple2
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.mockk.clearAllMocks

abstract class TestBehaviorSpec(body: BehaviorSpec.() -> Unit = {}) : BehaviorSpec(body) {

    override fun afterTest(f: suspend (Tuple2<TestCase, TestResult>) -> Unit) {
        super.afterTest(f)
        clearAllMocks()
    }
}