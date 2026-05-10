	/*
 * Licensed to The Apereo Foundation under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * The Apereo Foundation licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
*/
package org.unitime.timetable.model;

import jakarta.persistence.Transient;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import org.unitime.timetable.model.base.BaseStudentNote;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "student_note")
public class StudentNote extends BaseStudentNote implements Comparable<StudentNote> {
	private static final long serialVersionUID = 1L;

	public static final int MAX_NOTE_LENGTH = 1000;

	public StudentNote() {
		super();
	}

	public static StudentNote create(Student student, String text, String userId) {
		if (text == null || text.isBlank())
			throw new IllegalArgumentException("Student note text must not be blank.");
		if (text.length() > MAX_NOTE_LENGTH)
			throw new IllegalArgumentException(
					"Student note text exceeds the maximum allowed length of "
							+ MAX_NOTE_LENGTH + " characters.");
		StudentNote note = new StudentNote();
		note.setStudent(student);
		note.setTextNote(text.trim());
		note.setUserId(userId);
		note.setTimeStamp(new Date());
		return note;
	}

	@Transient
	public boolean isValid() {
		return getTextNote() != null
				&& !getTextNote().isBlank()
				&& getTextNote().length() <= MAX_NOTE_LENGTH;
	}

	@Override
	public int compareTo(StudentNote note) {
		int cmp = getTimeStamp().compareTo(note.getTimeStamp());
		if (cmp != 0) return cmp;
		return getUniqueId().compareTo(note.getUniqueId());
	}
}
