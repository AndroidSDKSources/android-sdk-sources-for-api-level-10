/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.cts.tradefed.testtype;

import com.android.ddmlib.testrunner.TestIdentifier;
import com.android.tradefed.device.DeviceNotAvailableException;
import com.android.tradefed.result.ITestInvocationListener;

import org.easymock.EasyMock;

import java.net.URL;
import java.util.Collections;

import junit.framework.TestCase;

/**
 * Unit tests for {@link JarHostTest}.
 */
public class JarHostTestTest extends TestCase {

    private static final String RUN_NAME = "run";
    private JarHostTest mJarTest;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mJarTest = new JarHostTest() {
            // mock out the loading from jar
            @Override
            Class<?> loadClass(String className, URL[] urls) throws ClassNotFoundException {
                return MockTest.class;
            }
        };
    }

    public static class MockTest extends TestCase {
        public MockTest(String name) {
            super(name);
        }

        public void testFoo() {
        }
    }

    /**
     * Test normal case for
     * {@link JarHostTest#run(com.android.tradefed.result.ITestInvocationListener)}.
     */
    @SuppressWarnings("unchecked")
    public void testRun() throws DeviceNotAvailableException {
        ITestInvocationListener listener = EasyMock.createMock(ITestInvocationListener.class);
        TestIdentifier expectedTest = new TestIdentifier(MockTest.class.getName(), "testFoo");

        listener.testRunStarted(RUN_NAME, 1);
        listener.testStarted(expectedTest);
        listener.testEnded(expectedTest, Collections.EMPTY_MAP);
        listener.testRunEnded(EasyMock.anyLong(), EasyMock.eq(Collections.EMPTY_MAP));

        EasyMock.replay(listener);

        mJarTest.setRunName(RUN_NAME);
        mJarTest.run(listener);
    }

}
