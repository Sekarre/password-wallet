package com.example.passwordwallet.util;

import java.time.LocalDateTime;
import java.util.Comparator;

public class DateUtil {

    public static final String JSON_DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm:ss";

    public static final Comparator<LocalDateTime> localDateTimeComparator = LocalDateTime::compareTo;
}
