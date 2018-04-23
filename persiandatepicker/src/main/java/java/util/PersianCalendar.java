package java.util;


/**
	PersianCalendar.java
	2003-09-24 14:56:36
	Copyright (c) Ghasem Kiani <ghasemkiani@yahoo.com>

	Bugs Corrected by MHM

	This program is free software; you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.

	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

/*
	HISTORY:
	Version 2.1 2005-03-18:
		A bug was corrected. Calculation of Julian days was being done on
		the timezone-dependent time, not on the universal time. Now, this
		is corrected.
	Version 2.0 2005-02-21:
		Some of the functionality was exported to other classes to be
		usable by the com.ghasemkiani.util.icu.PersianCalendar class
		which is based on IBM's ICU4J.
		Calculation algorithm was rewritten and some bugs were fixed.
		Specifically, two functions for modulo and quotient were
		introduced that solve some of the problems that arose in years
		before 475 A.H.
		Added a new read/write property: julianDay.
		Characters ARABIC LETTER YEH and ARABIC LETTER KAF were replaced
		by ARABIC LETTER FARSI YEH and ARABIC LETTER KEHEH, respectively,
		in the Persian names of months and week days. This was done in
		accordance with the CLDR locale for the Persian language, though I
		don't like this personally, because the characters for Arabic and
		Persian scripts had better be identical as much as possible.
		So that we can use Cp1256, Internet searches in Persian are more
		effective, and there is much less confusion in general.
		Changed Amordad to Mordad, since the former, though more correct,
		is seldom used today.
	Version 1.3 2003-12-12:
		Added accessor get methods for "properties" persianMonthName
		and persianWeekDayName. Corrected some errors in the
		documentation.
	Version 1.2 2003-11-17:
		Converted Persian literals to Unicode escape sequences.
	Version 1.1 2003-10-23:
		Added Persian names for months and days of the week.
		Added Javadoc documentation for the API.
	Version 1.0 2003-09-25:
		Started the project.
*/

import java.util.persian.PersianCalendarConstants;
import java.util.persian.PersianCalendarUtils;

/**
 * <strong> Persian(Shamsi) calendar </strong>
 * <p>
 * </p>
 * <p>
 * The calendar consists of 12 months, the first six of which are 31 days, the
 * next five 30 days, and the final month 29 days in a normal year and 30 days
 * in a leap year.
 * </p>
 * <p>
 * As one of the few calendars designed in the era of accurate positional
 * astronomy, the Persian calendar uses a very complex leap year structure which
 * makes it the most accurate solar calendar in use today. Years are grouped
 * into cycles which begin with four normal years after which every fourth
 * subsequent year in the cycle is a leap year. Cycles are grouped into grand
 * cycles of either 128 years (composed of cycles of 29, 33, 33, and 33 years)
 * or 132 years, containing cycles of of 29, 33, 33, and 37 years. A great grand
 * cycle is composed of 21 consecutive 128 year grand cycles and a final 132
 * grand cycle, for a total of 2820 years. The pattern of normal and leap years
 * which began in 1925 will not repeat until the year 4745!
 * </p>
 * </p> Each 2820 year great grand cycle contains 2137 normal years of 365 days
 * and 683 leap years of 366 days, with the average year length over the great
 * grand cycle of 365.24219852. So close is this to the actual solar tropical
 * year of 365.24219878 days that the Persian calendar accumulates an error of
 * one day only every 3.8 million years. As a purely solar calendar, months are
 * not synchronized with the phases of the Moon. </p>
 *
 * This class is a subclass of <code>java.util.GregorianCalendar</code>,
 * with the added functionality that it can set/get date in the Persian
 * calendar system.
 *
 * The algorithms for conversion between Persian and Gregorian calendar systems
 * are placed in this class.
 *
 *
 * <p>
 * <strong>PersianCalendar</strong> by extending Default GregorianCalendar
 * provides capabilities such as:
 * </p>
 *
 * <li>you can set the date in Persian by setPersianDate(persianYear,
 * persianMonth, persianDay) and get the Gregorian date or vice versa</li>
 * <li>determine is the current date is Leap year in persian calendar or not by
 * IsPersianLeapYear()</li>
 * <li>getPersian short and long Date String getPersianShortDate() and
 * getPersianLongDate you also can set delimiter to assign delimiter of returned
 * dateString</li>
 * <li>Parse string based on assigned delimiter</li>
 * <p></p>
 * <p>
 * <strong> Example </strong>
 * <pre>
 * {@code
 * PersianCalendar persianCal = new PersianCalendar();
 *    System.out.println(persianCal.getPersianShortDate());
 *
 *    persianCal.set(1982, Calendar.MAY, 22);
 *    System.out.println(persianCal.getPersianShortDate());
 *
 *    persianCal.setDelimiter(" , ");
 *    persianCal.parse("1361 , 03 , 01");
 *    System.out.println(persianCal.getPersianShortDate());
 *
 *    persianCal.setPersianDate(1361, 3, 1);
 *    System.out.println(persianCal.getPersianLongDate());
 *    System.out.println(persianCal.getTime());
 *
 *    persianCal.addPersianDate(Calendar.MONTH, 33);
 *    persianCal.addPersianDate(Calendar.YEAR, 5);
 *    persianCal.addPersianDate(Calendar.DATE, 50);
 *
 * }
 *
 * </pre>
 * </p>
 * @see java.util.Calendar
 * @see java.util.GregorianCalendar

 * @author MHM by combining works of <a href="mailto:ghasemkiani@yahoo.com">Ghasem Kiani</a> and <a href="mailto:Mortezaadi@gmail.com">Morteza</a>
 * @version 1.0
 *
 */

public class PersianCalendar extends GregorianCalendar {

    /**
     *
     */
    private static final long serialVersionUID = 1116789216454548732L;
    // Julian day 0, 00:00:00 hours (midnight); milliseconds since 1970-01-01 00:00:00 UTC (Gregorian Calendar)
    private static final long JULIAN_EPOCH_MILLIS = -210866803200000L;
    private static final long ONE_DAY_MILLIS = 24L * 60L * 60L * 1000L;

    protected int persianYear;
    protected int persianMonth;
    protected int persianDay;

    // use to seperate PersianDate's field and also Parse the DateString based
    // on this delimiter
    private String delimiter = "/";

    /**
     Julian day corresponding to 1 Farvardin 1 A.H., corresponding to
     March 19, 622 A.D. by the Julian version of the Gregorian calendar.
     */
    public static final long EPOCH = 1948321L;

    public PersianCalendar() {}

    public PersianCalendar(long millis) {
        setTimeInMillis(millis);
    }

    /**
     A modulo function suitable for our purpose.

     @param a the dividend.
     @param b the divisor.
     @return the remainder of integer division.
     */
    public static long mod(double a, double b)
    {
        return (long)(a - b * Math.floor(a / b));
    }
    /**
     An integer division function suitable for our purpose.

     @param a the dividend.
     @param b the divisor.
     @return the quotient of integer division.
     */
    public static long div(double a, double b)
    {
        return (long)Math.floor(a / b);
    }
    /**
     Extracts the year from a packed long value.

     @param r the packed long value.
     @return the year part of date.
     */
    public static long y(long r)
    {
        return r >> 16;
    }
    /**
     Extracts the month from a packed long value.

     @param r the packed long value
     .
     @return the month part of date.
     */
    public static int m(long r)
    {
        return (int)(r & 0xff00) >> 8;
    }
    /**
     Extracts the day from a packed long value.

     @param r the packed long value.
     @return the day part of date.
     */
    public static int d(long r)
    {
        return (int)(r & 0xff);
    }

    /**
     Determines if the specified year is a leap year in the Persian calendar.

     @param year the "Persian" year.
     @return <code>true</code> if <code>year</code> is a leap year, <code>false</code> otherwise.
     */
    public static boolean isLeapYear(long year)
    {
        long a = year - 474L;
        long b = mod(a, 2820L) + 474L;
        return mod((b + 38D) * 682D, 2816D) < 682L;
    }
    /**
     Returns the Julian day corresponding to the specified date in the Persian calendar.

     @param y the Persian year.
     @param m the Persian month.
     @param d the Persian day.
     @return the Julian day corresponding to the specified date in the Persian calendar.
     */
    public static long pj(long y, int m, int d)
    {
        long a = y - 474L;
        long b = mod(a, 2820D) + 474L;
        return (EPOCH - 1L) + 1029983L * div(a, 2820D) + 365L * (b - 1L) + div(682L * b - 110L, 2816D) + (long)(m > 6? 30 * m + 6: 31 * m) + (long)d;
    }
    /**
     Returns the date in the Persian calendar corresponding to the specified Julian day.
     The date fields (year, month, and day) are packed into a long value.

     @param j the Julian day.
     @return a packed long value containing the corresponding Persian year, month, and day.
     */
    public static long jp(long j)
    {
        long a = j - pj(475L, 0, 1);
        long b = div(a, 1029983D);
        long c = mod(a, 1029983D);
        long d = c != 1029982L? div(2816D * (double)c + 1031337D, 1028522D): 2820L;
        long year = 474L + 2820L * b + d;
        long f = (1L + j) - pj(year, 0, 1);
        int month = (int)(f > 186L? Math.ceil((double)(f - 6L) / 30D) - 1: Math.ceil((double)f / 31D) - 1);
        int day = (int)(j - (pj(year, month, 1) - 1L));
        return (year << 16) | (month << 8) | day;
    }

    /**
     Get the Julian day corresponding to the date of this calendar.
     @since 2.0

     @return the Julian day corresponding to the date and timezone of this calendar.
     */
    public long getJulianDay()
    {
        return div(getTimeInHereMillis() - JULIAN_EPOCH_MILLIS, ONE_DAY_MILLIS);
    }

    /**
     Get the Julian day corresponding to the date of this calendar.
     @since 2.0

     @return the UTC Julian day corresponding to the date of this calendar.
     */
    public long getUtcJulianDay()
    {
        return div(getTimeInMillis() - JULIAN_EPOCH_MILLIS, ONE_DAY_MILLIS);
    }
    /**
     Set the date of this calendar to the specified Julian day.
     @since 2.0

     @param julianDay the desired Julian day to be set as the date of this calendar.
     */
    public void setJulianDay(long julianDay)
    {
        setTimeInMillis(JULIAN_EPOCH_MILLIS + julianDay * ONE_DAY_MILLIS + mod(getTimeInMillis() /* - JULIAN_EPOCH_MILLIS*/, ONE_DAY_MILLIS));
    }

    /**
     Sets the date of this calendar object to the specified
     Persian date fields
     @since 1.0

     @param persianYear the Persian year.
     @param persianMonth the Persian month (zero-based).
     @param persianDay the Persian day of month.

     */
    public void setPersianDate(int persianYear, int persianMonth, int persianDay) {
        this.persianYear = persianYear;
        this.persianMonth = persianMonth;
        this.persianDay = persianDay;
        setJulianDay(pj(persianYear > 0? persianYear: persianYear + 1, persianMonth, persianDay));
    }

    /**
     * Calculate persian date from current Date and populates the corresponding
     * fields(persianYear, persianMonth, persianDay)
     */
    protected void calculatePersianDate() {
        long julianDay = getJulianDay();
        long r = jp(julianDay);
        persianYear = (int) y(r);
        persianMonth = m(r);
        persianDay = d(r);
    }

    public int getPersianYear() {
        return this.persianYear;
    }

    /**
     *
     * @return The Persian month (zero-based).
     */
    public int getPersianMonth() {
        return this.persianMonth;
    }

    public int getPersianDay() {
        return this.persianDay;
    }

    public long getTimeInHereMillis() {
        return getTimeInMillis() + (long)getTimeZone().getRawOffset();
    }

    /**
     *
     * Determines if the given year is a leap year in persian calendar. Returns
     * true if the given year is a leap year.
     *
     * @return boolean
     */
    public boolean isPersianLeapYear() {
        return PersianCalendarUtils.isPersianLeapYear(this.persianYear);
    }

    public String getPersianMonthName() {
        return PersianCalendarConstants.persianMonthNames[this.persianMonth];
    }

    /**
     *
     * @return String Name of the day in week
     */
    public String getPersianWeekDayName() {
        switch (get(DAY_OF_WEEK)) {
            case SATURDAY:
                return PersianCalendarConstants.persianWeekDays[0];
            case SUNDAY:
                return PersianCalendarConstants.persianWeekDays[1];
            case MONDAY:
                return PersianCalendarConstants.persianWeekDays[2];
            case TUESDAY:
                return PersianCalendarConstants.persianWeekDays[3];
            case WEDNESDAY:
                return PersianCalendarConstants.persianWeekDays[4];
            case THURSDAY:
                return PersianCalendarConstants.persianWeekDays[5];
            default:
                return PersianCalendarConstants.persianWeekDays[6];
        }

    }

    /**
     *
     * @return String of Persian Date ex: شنبه 01 خرداد 1361
     */
    public String getPersianLongDate() {
        return getPersianWeekDayName() + "  " + formatToMilitary(this.persianDay) + "  " + getPersianMonthName() + "  " + this.persianYear;
    }

    public String getPersianLongDateAndTime() {
        return getPersianLongDate() + " \u0633\u0627\u0639\u062A " + formatToMilitary(get(HOUR_OF_DAY)) + ":" + formatToMilitary(get(MINUTE)) + ":" + formatToMilitary(get(SECOND));
    }

    /**
     *
     * @return String of persian date formatted by
     *         'YYYY[delimiter]mm[delimiter]dd' default delimiter is '/'
     */
    public String getPersianShortDate() {
        // calculatePersianDate();
        return "" + formatToMilitary(this.persianYear) + delimiter + formatToMilitary(getPersianMonth() + 1) + delimiter + formatToMilitary(this.persianDay);
    }

    public String getPersianShortDateTime() {
        return "" + formatToMilitary(this.persianYear) + delimiter + formatToMilitary(getPersianMonth() + 1) + delimiter + formatToMilitary(this.persianDay) + " " + formatToMilitary(this.get(HOUR_OF_DAY)) + ":" + formatToMilitary(get(MINUTE))
                + ":" + formatToMilitary(get(SECOND));
    }

    private String formatToMilitary(int i) {
        return (i <= 9) ? "0" + i : String.valueOf(i);
    }

    public String getDelimiter() {
        return delimiter;
    }

    /**
     * assign delimiter to use as a separator of date fields.
     *
     * @param delimiter
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public String toString() {
        String str = super.toString();
        return str.substring(0, str.length() - 1) + ",PersianDate=" + getPersianShortDate() + "]";
    }

    /**
     * add specific amout of fields to the current date for now doesnt handle
     * before 1 farvardin hejri (before epoch)
     *
     * @param field
     * @param amount
     *            <pre>
     *  Usage:
     *  {@code
     *  addPersianDate(Calendar.YEAR, 2);
     *  addPersianDate(Calendar.MONTH, 3);
     *  }
     * </pre>
     *
     *            You can also use Calendar.HOUR_OF_DAY,Calendar.MINUTE,
     *            Calendar.SECOND, Calendar.MILLISECOND etc
     */
    public void addPersianDate(int field, int amount) {
        if (amount == 0) {
            return; // Do nothing!
        }

        if (field < 0 || field >= ZONE_OFFSET) {
            throw new IllegalArgumentException();
        }

        if (field == YEAR) {
            setPersianDate(this.persianYear + amount, getPersianMonth(), this.persianDay);
            return;
        } else if (field == MONTH) {
            setPersianDate(this.persianYear + ((getPersianMonth() + amount) / 12), (getPersianMonth() + amount) % 12, this.persianDay);
            return;
        }
        add(field, amount);
        calculatePersianDate();
    }

    @Override
    public void set(int field, int value) {
        super.set(field, value);
        calculatePersianDate();
    }

    @Override
    public void setTimeInMillis(long millis) {
        super.setTimeInMillis(millis);
        calculatePersianDate();
    }

    @Override
    public void setTimeZone(TimeZone zone) {
        super.setTimeZone(zone);
        calculatePersianDate();
    }

}
