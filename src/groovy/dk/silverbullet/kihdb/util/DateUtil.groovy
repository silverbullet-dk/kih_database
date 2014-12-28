/**
 * Copyright 2014 Stiftelsen for Software-baserede Sundhedsservices - 4S
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.silverbullet.kihdb.util

import javax.xml.datatype.DatatypeConfigurationException
import javax.xml.datatype.DatatypeFactory
import javax.xml.datatype.XMLGregorianCalendar

/**
 * Commonly used date manipulation methods
 */
public final class DateUtil {

    private static final String XML_GREGORIAN_CALENDAR_ZULU_POSTFIX = "Z";

    private DateUtil() {
        // do not instantiate
    }

    /**
     * Creates a date object which is set on midnight at the supplied date.<br/>
     * Note that month is 0-based.
     */
    public static Date createAlignedDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    /**
     * Aligns the supplied date to midnight. I.e. all hours, minutes, seconds and milliseconds are set to 0.
     * 21/4-12 13:21:14 124 => 21/4-12 00:00:00 000
     */
    public static Date alignToDay(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        return calendar.getTime();
    }

    /**
     * Aligns the supplied date to seconds. I.e. milliseconds are set to 0.
     * 21/4-12 13:21:14 124 => 21/4-12 13:21:14 000
     */
    public static Date alignToSeconds(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * Adds the specified number of days to the supplied date object.
     */
    public static Date addDays(Date date, int days) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, days);

        return calendar.getTime();
    }

    /**
     * Creates a new date object, which is set the supplied number of days from today. <br>
     * The new date object is automatically aligned to midnight (see {@link #alignToDay(Date)})
     */
    public static Date daysFromTodayAligned(int days) {
        return alignToDay(addDays(new Date(), days));
    }

    /**
     * Creates a new date object, which is set the supplied number of days from now.
     */
    public static Date daysFromNow(int days) {
        return addDays(new Date(), days);
    }

    /**
     * Adds the specified number of years to the supplied date object.
     */
    public static Date addYears(Date date, int years) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.add(Calendar.YEAR, years);

        return calendar.getTime();
    }

    /**
     * Creates a new date object, which is set the supplied number of years from today. <br>
     * The new date object is automatically aligned to midnight (see {@link #alignToDay(Date)})
     */
    public static Date yearsFromTodayAligned(int years) {
        return alignToDay(addYears(new Date(), years));
    }

    /**
     * Creates a copy of the supplied date object.
     */
    public static Date copyDate(Date d) {
        return d == null ? null : new Date(d.getTime());
    }

    /**
     * Converts a date to an {@link XMLGregorianCalendar}
     */
    public static XMLGregorianCalendar dateToXmlGregorianCalendar(Date date) throws DatatypeConfigurationException {
        if (date == null) {
            return null;
        }
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(date.getTime());
        XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        return xmlGregorianCalendar.normalize();
    }

    /**
     * Converts an {@link XMLGregorianCalendar} to a date
     */
    public static Date xmlGregorianCalendarToDate(XMLGregorianCalendar calendar) {
        if (calendar == null) {
            return null;
        }
        return calendar.toGregorianCalendar().getTime();
    }

    /**
     * @return True if the calendar's time is expressed in Zulu time (ending with a Z)
     */
    public static boolean isXmlGregorianCalendarInZulu(XMLGregorianCalendar calendar) {
        if (calendar == null) {
            return false;
        }
        return calendar.toString().endsWith(XML_GREGORIAN_CALENDAR_ZULU_POSTFIX);
    }
}
