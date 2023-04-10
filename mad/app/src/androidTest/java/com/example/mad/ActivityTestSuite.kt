package com.example.mad

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    ShowProfileActivityTest::class,
    EditProfileActivityTest::class,
    ShowProfileActivityTestUIAutomator::class
)
class ActivityTestSuite