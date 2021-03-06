package com.temenos.interaction.core.hypermedia;

/*
 * #%L
 * interaction-core
 * %%
 * Copyright (C) 2012 - 2013 Temenos Holdings N.V.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TestTransition {

	@Test
	public void testEquality() {
		ResourceState begin = new ResourceState("entity", "", new ArrayList<Action>(), "/");
		ResourceState begin2 = new ResourceState("entity", "", new ArrayList<Action>(), "/");

		Transition.Builder tb = new Transition.Builder();
		tb.source(begin)
			.target(begin2)
			.method("PUT")
			.path("stuff")
			.flags(Transition.FOR_EACH);
		Transition t = tb.build();
		Transition.Builder tb2 = new Transition.Builder();
		tb2.source(begin)
			.target(begin2)
			.method("PUT")
			.path("stuff")
			.flags(Transition.FOR_EACH);
		Transition t2 = tb2.build();
		assertEquals(t, t2);
		assertEquals(t.hashCode(), t2.hashCode());
	}
	
	@Test
	public void testEqualityNullSource() {
		ResourceState begin2 = new ResourceState("entity", "", new ArrayList<Action>(), "/");
		Transition.Builder tb = new Transition.Builder();
		tb.source(null)
			.target(begin2)
			.method("PUT")
			.path("stuff")
			.flags(Transition.FOR_EACH);
		Transition t = tb.build();
		Transition.Builder tb2 = new Transition.Builder();
		tb2.source(null)
			.target(begin2)
			.method("PUT")
			.path("stuff")
			.flags(Transition.FOR_EACH);
		Transition t2 = tb2.build();
		assertEquals(t, t2);
		assertEquals(t.hashCode(), t2.hashCode());
	}

	@Test 
	public void testInequality() {
		ResourceState begin = new ResourceState("entity", "begin", new ArrayList<Action>(), "/");
		ResourceState exists = new ResourceState("entity", "exists", new ArrayList<Action>(), "{id}");
		ResourceState end = new ResourceState("entity", "end", new ArrayList<Action>(), "/");

		Transition.Builder tb = new Transition.Builder();
		tb.source(begin)
			.target(end)
			.method("PUT")
			.path("stuff")
			.flags(Transition.FOR_EACH);
		Transition t = tb.build();
		Transition.Builder tb2 = new Transition.Builder();
		tb2.source(begin)
			.target(exists)
			.method("PUT")
			.path("stuff")
			.flags(Transition.FOR_EACH);
		Transition t2 = tb2.build();
		assertFalse(t.equals(t2));
		assertFalse(t.hashCode() == t2.hashCode());
		
		Transition.Builder tb3 = new Transition.Builder();
		tb3.source(begin)
			.target(end)
			.method("PUT")
			.path("stuff")
			.flags(Transition.FOR_EACH);
		Transition t3 = tb3.build();
		Transition.Builder tb4 = new Transition.Builder();
		tb4.source(begin)
			.target(end)
			.path("stuffed")
			.flags(Transition.AUTO);
		Transition t4 = tb2.build();
		assertFalse(t3.equals(t4));
		assertFalse(t3.hashCode() == t4.hashCode());

	}

	@Test 
	public void testInequalityUriParameters() {
		ResourceState begin = new ResourceState("entity", "collection", new ArrayList<Action>(), "/");
		ResourceState exists = new ResourceState("entity", "onetype", new ArrayList<Action>(), "{id}");

		Map<String, String> uriParameters = new HashMap<String, String>();
		uriParameters.put("id", "abc");
		Transition t = new Transition.Builder()
				.source(begin)
				.method("PUT")
				.path("stuff")
				.flags(Transition.FOR_EACH)
				.uriParameters(uriParameters)
				.target(exists)
				.build();
		
		uriParameters.clear();
		uriParameters.put("id", "xyz");
		Transition t2 = new Transition.Builder()
				.source(begin)
				.method("PUT")
				.path("stuff")
				.flags(Transition.FOR_EACH)
				.uriParameters(uriParameters)
				.target(exists)
				.build();
		assertFalse(t.equals(t2));
		assertFalse(t.hashCode() == t2.hashCode());
	}

	@Test 
	public void testInequalityLabel() {
		ResourceState begin = new ResourceState("entity", "collection", new ArrayList<Action>(), "/");
		ResourceState exists = new ResourceState("entity", "onetype", new ArrayList<Action>(), "{id}");

		Transition t = new Transition.Builder()
				.source(begin)
				.method("PUT")
				.path("stuff")
				.flags(Transition.FOR_EACH)
				.target(exists)
				.label("label1")
				.build();
		Transition t2 = new Transition.Builder()
				.source(begin)
				.method("PUT")
				.path("stuff")
				.flags(Transition.FOR_EACH)
				.target(exists)
				.label("differentlabel")
				.build();
		assertFalse(t.equals(t2));
		assertFalse(t.hashCode() == t2.hashCode());
	}
	
	@Test
	public void testGetId() {
		ResourceState begin = new ResourceState("entity", "begin", new ArrayList<Action>(), "{id}");
		ResourceState end = new ResourceState("entity", "end", new ArrayList<Action>(), "{id}");

		Transition t = new Transition.Builder()
				.source(begin)
				.method("PUT")
				.path("stuff")
				.target(end)
				.build();
		assertEquals("entity.begin>PUT>entity.end", t.getId());
	}

	@Test
	public void testToString() {
		ResourceState begin = new ResourceState("entity", "begin", new ArrayList<Action>(), "/begin");
		ResourceState end = new ResourceState("entity", "end", new ArrayList<Action>(), "/end");

		Transition.Builder tb = new Transition.Builder();
		tb.source(begin)
			.target(end)
			.method("PUT")
			.path("stuff");
		Transition t = tb.build();
		assertEquals("entity.begin>PUT>entity.end", t.toString());
	}

	@Test
	public void testGetLabel() {
		ResourceState begin = new ResourceState("entity", "begin", new ArrayList<Action>(), "{id}");
		ResourceState end = new ResourceState("entity", "end", new ArrayList<Action>(), "{id}");

		Transition.Builder tba = new Transition.Builder();
		tba.source(begin)
			.target(end)
			.method("GET")
			.path("stuff")
			.label("A");
		Transition ta = tba.build();
		assertEquals("A", ta.getLabel());
		Transition.Builder tbb = new Transition.Builder();
		tbb.source(begin)
			.target(end)
			.method("GET")
			.path("stuff")
			.label("B");
		Transition tb = tbb.build();
		assertEquals("B", tb.getLabel());
	}

	@Test
	public void testIdMultiTransitions() {
		ResourceState begin = new ResourceState("entity", "begin", new ArrayList<Action>(), "{id}");
		ResourceState end = new ResourceState("entity", "end", new ArrayList<Action>(), "{id}");

		Transition ta = new Transition.Builder()
				.source(begin)
				.method("GET")
				.path("stuff")
				.target(end)
				.label("A")
				.build();
		Transition taPut = new Transition.Builder()
				.source(begin)
				.method("PUT")
				.path("stuff")
				.target(end)
				.label("A")
				.build();
		assertEquals("entity.begin>GET(A)>entity.end", ta.getId());
		assertEquals("entity.begin>PUT(A)>entity.end", taPut.getId());
		Transition tb = new Transition.Builder()
				.source(begin)
				.method("GET")
				.path("stuff")
				.target(end)
				.label("B")
				.build();
		assertEquals("entity.begin>GET(B)>entity.end", tb.getId());
	}

	@Test
	public void testCheckTransitionFromCollectionToEntityResource() {
		ResourceState begin = new ResourceState("entity", "begin", new ArrayList<Action>(), "{id}");
		ResourceState end = new ResourceState("entity", "end", new ArrayList<Action>(), "{id}");
		Transition t = new Transition.Builder()
				.source(begin).method("GET").path("stuff").target(end).build();
		assertFalse(t.isGetFromCollectionToEntityResource());

		begin = new ResourceState("entity", "begin", new ArrayList<Action>(), "{id}");
		end = new CollectionResourceState("entity", "end", new ArrayList<Action>(), "{id}");
		t = new Transition.Builder()
				.source(begin).method("GET").path("stuff").target(end).build();
		assertFalse(t.isGetFromCollectionToEntityResource());

		begin = new CollectionResourceState("entity", "begin", new ArrayList<Action>(), "{id}");
		end = new ResourceState("entity", "end", new ArrayList<Action>(), "{id}");
		t = new Transition.Builder()
				.source(begin).method("GET").path("stuff").target(end).build();
		assertTrue(t.isGetFromCollectionToEntityResource());

		begin = new CollectionResourceState("entity", "begin", new ArrayList<Action>(), "{id}");
		end = new ResourceState("otherEntity", "end", new ArrayList<Action>(), "{id}");
		t = new Transition.Builder()
				.source(begin).method("GET").path("stuff").target(end).build();
		assertFalse(t.isGetFromCollectionToEntityResource());

		begin = new CollectionResourceState("otherEntity", "begin", new ArrayList<Action>(), "{id}");
		end = new ResourceState("entity", "end", new ArrayList<Action>(), "{id}");
		t = new Transition.Builder()
				.source(begin).method("GET").path("stuff").target(end).build();
		assertFalse(t.isGetFromCollectionToEntityResource());
	}

	@Test
	public void testIdMultiTransitionsWithParametersNoLabel() {
		ResourceState begin = new ResourceState("entity", "begin", new ArrayList<Action>(), "{id}");
		ResourceState end = new ResourceState("entity", "end", new ArrayList<Action>(), "{id}");

		Map<String, String> params = new HashMap<String, String>();
		params.put("paramA", "hello A");
		Transition ta = new Transition.Builder()
				.source(begin)
				.method("GET")
				.path("stuff")
				.uriParameters(params)
				.target(end)
				.build();
		Transition taPut = new Transition.Builder()
				.source(begin)
				.method("PUT")
				.path("stuff")
				.target(end)
				.build();
		assertEquals("entity.begin>GET>entity.end", ta.getId());
		assertEquals("entity.begin>PUT>entity.end", taPut.getId());
		params = new HashMap<String, String>();
		params.put("paramB", "hello B");
		Transition tb = new Transition.Builder()
				.source(begin)
				.method("GET")
				.path("stuff")
				.uriParameters(params)
				.target(end)
				.build();
		assertEquals("entity.begin>GET>entity.end", tb.getId());
	}
}
