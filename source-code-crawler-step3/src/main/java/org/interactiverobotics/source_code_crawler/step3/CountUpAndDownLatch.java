/*
 * CountUpAndDownLatch.java
 *
 * Copyright (C) 2016 Pavel Prokhorov (pavelvpster@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.interactiverobotics.source_code_crawler.step3;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This latch counts up and down in opposite to {@code CountDownLatch}.
 */
public final class CountUpAndDownLatch {

    private long count = 0;
    private final ReentrantLock countLock = new ReentrantLock();
    private final Condition countIsZero = countLock.newCondition();

    public void countUp() {
        countLock.lock();
        try {
            count++;
        } finally {
            countLock.unlock();
        }
    }

    public void countDown() {
        countLock.lock();
        try {
            if (count == 0) {
                return;
            }
            count--;
            if (count == 0) {
                countIsZero.signalAll();
            }
        } finally {
            countLock.unlock();
        }
    }

    public long getCount() {
        countLock.lock();
        try {
            return count;
        } finally {
            countLock.unlock();
        }
    }

    public void await() throws InterruptedException {
        countLock.lock();
        try {
            while (count > 0) {
                countIsZero.await();
            }
        } finally {
            countLock.unlock();
        }
    }

    public void await(final long timeout, final TimeUnit unit) throws InterruptedException {
        countLock.lock();
        try {
            while (count > 0) {
                countIsZero.await(timeout, unit);
            }
        } finally {
            countLock.unlock();
        }
    }
}
