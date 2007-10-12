/*
 * OCKHAM P2PREGISTRY Copyright 2006 Oregon State University
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
package org.osuosl.srw.sort;

/**
 * Thrown by SortTool when a missing value is encountered and missingValue == Omit.
 *
 * @author peter
 *         Date: Oct 24, 2005
 *         Time: 10:18:12 AM
 * @see SortTool
 */
public class AbortMissingValueException extends Exception {

    public AbortMissingValueException() {
    }

    public AbortMissingValueException(String message) {
        super(message);
    }

    public AbortMissingValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbortMissingValueException(Throwable cause) {
        super(cause);
    }

}
