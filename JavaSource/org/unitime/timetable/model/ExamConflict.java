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


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


import org.unitime.timetable.model.base.BaseExamConflict;

/**
 * @author Tomas Muller
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "xconflict")
public class ExamConflict extends BaseExamConflict implements Comparable<ExamConflict> {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ExamConflict () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ExamConflict (java.lang.Long uniqueId) {
		super(uniqueId);
	}
	
/*[CONSTRUCTOR MARKER END]*/

    public enum ConflictType {
        DIRECT(0,               "Distance"),
        MORE_THAN_TWO_A_DAY(1,  ">2 A Day"),
        BACK_TO_BACK_DIST(2,    "Distance Back-To-Back"),
        BACK_TO_BACK(3,         "Back-To-Back");

        private final int code;
        private final String label;

        ConflictType(int code, String label) {
            this.code  = code;
            this.label = label;
        }

        public int    getCode()  { return code; }
        public String getLabel() { return label; }

        public static ConflictType fromCode(int code) {
            for (ConflictType t : values())
                if (t.code == code) return t;
            throw new IllegalArgumentException("Unknown conflict type code: " + code);
        }
    }

    @Deprecated public static final int sConflictTypeDirect          = 0;
    @Deprecated public static final int sConflictTypeMoreThanTwoADay = 1;
    @Deprecated public static final int sConflictTypeBackToBackDist  = 2;
    @Deprecated public static final int sConflictTypeBackToBack      = 3;
    @Deprecated public static String[] sConflictTypes =
            new String[] {"Distance", ">2 A Day", "Distance Back-To-Back", "Back-To-Back"};

    @Transient
    public String getConflictTypeName() {
        return ConflictType.fromCode(getConflictType()).getLabel();
    }

    @Transient
    public ConflictType getConflictTypeEnum() {
        return ConflictType.fromCode(getConflictType());
    }

    @Transient
    public boolean isDirectConflict() {
        return ConflictType.DIRECT.getCode() == getConflictType();
    }

    @Transient
    public boolean isMoreThanTwoADayConflict() {
        return ConflictType.MORE_THAN_TWO_A_DAY.getCode() == getConflictType();
    }

    @Transient
    public boolean isBackToBackConflict() {
        return getConflictType() == ConflictType.BACK_TO_BACK.getCode()
                || getConflictType() == ConflictType.BACK_TO_BACK_DIST.getCode();
    }

    @Transient
    public boolean isDistanceBackToBackConflict() {
        return ConflictType.BACK_TO_BACK_DIST.getCode() == getConflictType();
    }
    
    public int compareTo(ExamConflict conflict) {
        int cmp = getConflictType().compareTo(conflict.getConflictType());
        if (cmp!=0) return cmp;
        cmp = getNrStudents().compareTo(conflict.getNrStudents());
        if (cmp!=0) return cmp;
        return (getUniqueId() == null ? Long.valueOf(-1) : getUniqueId()).compareTo(conflict.getUniqueId() == null ? -1 : conflict.getUniqueId());
    }
}
