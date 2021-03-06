/* Copyright (c) 2008-2018, Nathan Sweet
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of Esoteric Software nor the names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.esotericsoftware.kryo;

import org.junit.Test;

import java.io.Serializable;

public class GenericTest extends KryoTestCase {

	static class TestGenerics1<T> {
		private T val;

		public T getVal() {
			return val;
		}

		public void setVal(T val) {
			this.val = val;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			TestGenerics1<?> that = (TestGenerics1<?>) o;

			return val != null ? val.equals(that.val) : that.val == null;
		}

		@Override
		public int hashCode() {
			return val != null ? val.hashCode() : 0;
		}
	}

	static class TestGenerics2<BT extends Object & Serializable, OT> extends TestGenerics1<OT> {
		private BT value;

		public BT getValue() {
			return value;
		}

		public void setValue(BT value) {
			this.value = value;
		}

		public Class<? extends BT> getType() {
			return (Class<BT>) value.getClass();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			if (!super.equals(o)) return false;

			TestGenerics2<?, ?> that = (TestGenerics2<?, ?>) o;

			return value != null ? value.equals(that.value) : that.value == null;
		}

		@Override
		public int hashCode() {
			int result = super.hashCode();
			result = 31 * result + (value != null ? value.hashCode() : 0);
			return result;
		}
	}

	@Test
	public void testGenerics () {
		kryo.register(TestGenerics1.class);
		kryo.register(TestGenerics2.class);
		TestGenerics2<String, Integer> test = new TestGenerics2<>();
		test.setVal(2);
		test.setValue("this is a test");

		roundTrip(Integer.MIN_VALUE, test);
	}
}
