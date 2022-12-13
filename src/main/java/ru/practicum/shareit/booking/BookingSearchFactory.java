package ru.practicum.shareit.booking;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BookingSearchFactory {

    static Map<String, BookingSearch> searchMap = new HashMap<>();

    static  {
        searchMap.put("ALL", new SearchAll());
        searchMap.put("CURRENT", new SearchCurrent());
        searchMap.put("PAST", new SearchPast());
        searchMap.put("FUTURE", new SearchFuture());
        searchMap.put("WAITING", new SearchWaiting());
        searchMap.put("REJECTED", new SearchRejected());
    }



    public static Optional<BookingSearch> getSearchMethod(String operator) {
        return Optional.ofNullable(searchMap.get(operator));
    }
}
