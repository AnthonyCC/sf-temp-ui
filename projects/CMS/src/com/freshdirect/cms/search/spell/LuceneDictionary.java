package com.freshdirect.cms.search.spell;

import java.io.IOException;
import java.util.Iterator;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.util.StringHelper;

/**
 * Lucene Dictionary: terms taken from the given field of a Lucene index.
 * 
 * When using IndexReader.terms(Term) the code must not call next() on TermEnum as the first call to TermEnum, see:
 * http://issues.apache.org/jira/browse/LUCENE-6
 * 
 * 
 * 
 */
public class LuceneDictionary implements Dictionary {
	private IndexReader reader;
	private String field;

	public LuceneDictionary(IndexReader reader, String field) {
		this.reader = reader;
		this.field = StringHelper.intern(field);
	}

	public final Iterator<DictionaryItem> getWordsIterator() {
		try {
			return new LuceneIterator(reader.terms(new Term(field)), field);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected final static class LuceneIterator implements Iterator<DictionaryItem> {
		private String field;
		private TermEnum termEnum;
		private Term actualTerm;
		private boolean hasNextCalled;

		LuceneIterator(TermEnum termEnum, String field) {
			this.termEnum = termEnum;
			this.field = field;
		}

		public DictionaryItem next() {
			if (!hasNextCalled) {
				hasNext();
			}
			hasNextCalled = false;

			try {
				termEnum.next();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			return (actualTerm != null) ? new DictionaryItem(actualTerm.text()) : null;
		}

		public boolean hasNext() {
			if (hasNextCalled) {
				return actualTerm != null;
			}
			hasNextCalled = true;

			actualTerm = termEnum.term();

			// if there are no words return false
			if (actualTerm == null) {
				return false;
			}

			String currentField = actualTerm.field();

			// if the next word doesn't have the same field return false
			if (currentField != field) {
				actualTerm = null;
				return false;
			}

			return true;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
